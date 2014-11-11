package user.theovercaste.overdecompiler.parsers.methodparsers;

import java.io.*;
import java.util.*;

import user.theovercaste.overdecompiler.attributes.*;
import user.theovercaste.overdecompiler.attributes.LineNumberTableAttribute.LineNumberTableValue;
import user.theovercaste.overdecompiler.codeinternals.ClassPath;
import user.theovercaste.overdecompiler.codeinternals.MethodFlag;
import user.theovercaste.overdecompiler.datahandlers.ClassData;
import user.theovercaste.overdecompiler.datahandlers.MethodData;
import user.theovercaste.overdecompiler.exceptions.*;
import user.theovercaste.overdecompiler.instructions.Instruction;
import user.theovercaste.overdecompiler.instructions.Instructions;
import user.theovercaste.overdecompiler.parserdata.ParsedClass;
import user.theovercaste.overdecompiler.parserdata.ParsedMethod;
import user.theovercaste.overdecompiler.parserdata.method.*;
import user.theovercaste.overdecompiler.parsers.methodblockparsers.*;
import user.theovercaste.overdecompiler.parsers.methodblockparsers.MethodBlockParser.ScanState;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;

public class JavaMethodParser extends AbstractMethodParser {
    private static final ImmutableList<? extends MethodBlockParser> methodBlockParsers = ImmutableList.of(new MethodBlockParserIf());

    @Override
    public void parseMethodActions(ClassData fromClass, ParsedClass toClass, MethodData origin, ParsedMethod target) throws InvalidConstantPoolPointerException, InvalidAttributeException {
        Optional<CodeAttribute> code = AttributeTypes.getWrappedAttribute(origin.getAttributes(), fromClass.getConstantPool(), CodeAttribute.class);
        if (!code.isPresent()) {
            return;
        }
        Optional<LineNumberTableAttribute> lineNumberTable = AttributeTypes.getWrappedAttribute(origin.getAttributes(), fromClass.getConstantPool(), LineNumberTableAttribute.class);
        HashMap<Integer, Integer> lineNumberMap = new HashMap<>();
        if (lineNumberTable.isPresent()) {
            for (LineNumberTableValue value : lineNumberTable.get().getTable()) {
                lineNumberMap.put(value.getStartPc(), value.getLineNumber());
            }
        }
        try (DataInputStream din = new DataInputStream(new ByteArrayInputStream(code.get().getCode()))) {
            ArrayList<Instruction> instructionList = new ArrayList<>();
            int instructionIndexCounter = 0;
            int instructionByteIndexCounter = 0;
            for (int opcode = din.read(); opcode >= 0; opcode = din.read()) {
                Instruction.Factory f = Instructions.getFactory(opcode);
                f.setByteIndex(instructionByteIndexCounter);
                f.setInstructionIndex(instructionIndexCounter);
                if (lineNumberMap.containsKey(instructionByteIndexCounter)) {
                    f.setLineNumber(lineNumberMap.get(instructionByteIndexCounter));
                } else {
                    f.setHasLineNumber(false);
                }
                Instruction i = f.load(opcode, din);
                instructionList.add(i);
                instructionByteIndexCounter += i.getByteSize();
                instructionByteIndexCounter++;
                instructionIndexCounter++;
            }
            List<MethodBlockContainer.Member> members = new ArrayList<>(instructionList.size());
            for (Instruction i : instructionList) {
                members.add(new MethodBlockContainer.Member(i, null));
            }
            MethodBlockContainer dummyBaseContainer = new MethodBlockContainer(members) {
                @Override
                public MethodBlock toMethodBlock(List<MethodMember> members, Stack<MethodActionPointer> pointerStack) {
                    throw new UnsupportedOperationException();
                }
            };
            List<MethodMember> initialMembers = new ArrayList<>();
            MethodDecompileContext decompileContext = new MethodDecompileContext();
            if(!target.getFlags().contains(MethodFlag.STATIC)) {
                decompileContext.setHasThis(true);
            }
            for(ClassPath argument : target.getArguments()) {
                decompileContext.addParameter(argument);
            }
            for (MethodMember member : parseMethodBlockInstructions(fromClass, dummyBaseContainer, decompileContext, initialMembers)) {
                target.addMember(member);
            }
        } catch (IOException | InvalidInstructionException e) {
            e.printStackTrace();
        } catch (InstructionParsingException e) {
            e.printStackTrace();
        }
    }

    private List<MethodMember> parseMethodBlockInstructions(ClassData fromClass, MethodBlockContainer container, MethodDecompileContext decompileContext, List<MethodMember> initialMembers) throws InstructionParsingException {
        List<MethodMember> members = new ArrayList<MethodMember>(container.getMembers().size());
        if(initialMembers != null) {
            members.addAll(initialMembers);
        }
        runParsersOnBlock(container); // Split the container's instructions into inner blocks.
        for (MethodBlockContainer.Member member : container.getMembers()) {
            if (member.isContainer()) {
                List<MethodMember> parsedSubList = parseMethodBlockInstructions(fromClass, member.getContainer(), decompileContext, null); // Call recursively to parse the nested inner blocks generated by runParsersOnBlock.
                MethodBlock block = member.getContainer().toMethodBlock(parsedSubList, null); //TODO pointer stack
                members.add(block);
            } else if (member.isInstruction()) {
                Instruction i = member.getInstruction();
                i.modifyStack(decompileContext);
                if (i.isAction()) {
                    MethodAction action = i.getAction(fromClass, decompileContext);
                    members.add(action);
                    if (action.isGetter()) {
                        decompileContext.pushActionPointer(new MethodActionPointer(decompileContext.getAndIncrementCurrentPointerIndex()));
                    }
                }
            }
            // }
        }
        int currentVariableIndex = 0;
        ListIterator<MethodMember> memberIterator = members.listIterator();
        while (memberIterator.hasNext()) {
            MethodMember member = memberIterator.next();
            if (member instanceof MethodAction && !(member instanceof MethodActionLoadParameter)) {
                MethodAction action = (MethodAction) member;
                if (action.isGetter()) {
                   memberIterator.remove();
                   memberIterator.add(new MethodActionSetVariable(currentVariableIndex++, action, action.getClassType(), true)); //TODO logic involving first reference, checking types
                }
            }
        }
        return members;
    }

    public Iterable<? extends MethodBlockParser> getMethodBlockParsers( ) {
        return methodBlockParsers;
    }

    /**
     * This method runs all of the block parsers on this block, splitting it up into smaller blocks. It must be run before any MethodActions are parsed.
     * The reason we have to use this convoluted method is because there isn't a 1-1 relationship between Instructions and MethodBlocks like there are for Instructions and MethodActions.
     * 
     * @param container the unparsed container to be split
     */
    public void runParsersOnBlock(MethodBlockContainer container) {
        List<MethodBlockContainer.Member> members = container.getMembers();
        ScanState currentState = ScanState.NO_MATCH;
        MethodBlockParser currentParser = null;
        List<MethodBlockContainer.Member> currentBlockMembers = new ArrayList<>(); // This is a buffer of the members to be added if they are within a parsing block.
        ListIterator<MethodBlockContainer.Member> memberIterator = members.listIterator();
        while (memberIterator.hasNext()) {
            int iteratorIndex = memberIterator.nextIndex();
            MethodBlockContainer.Member member = memberIterator.next();
            ListIterator<MethodBlockContainer.Member> parserIterator = members.listIterator(iteratorIndex); // Duplicate the iterator so that the parser can check if it's starting.
            if (member.isInstruction()) { // A block should be only instructions at this point.
                if (ScanState.SCAN_STARTED.equals(currentState)) {
                    currentParser.parse(parserIterator);
                    ScanState parserState = currentParser.getState();
                    switch (parserState) {
                        case SCAN_ENDED: // The current block was parsed, and a new block of the same type didn't start.
                            currentState = ScanState.SCAN_ENDED;
                            memberIterator.previous();
                            memberIterator.add(new MethodBlockContainer.Member(null, currentParser.createContainer(currentBlockMembers)));
                            memberIterator.next();
                            currentBlockMembers.clear();
                            currentParser.reset();
                            break;
                        case SCAN_STARTED: // The current block was parsed, and a new block of the same start immediately started again
                            throw new IllegalStateException("The parser state was set to SCAN_STARTED when only SCAN_ENDED was expected.");
                        case NO_MATCH:
                            currentBlockMembers.add(member); // Remove this from the current block...
                            memberIterator.remove(); // And add it to the inner block.
                            break;
                        default:
                            throw new RuntimeException("The parser had a state that was unknown! (" + parserState + ")");
                    }
                } else {
                    for (MethodBlockParser parser : getMethodBlockParsers()) {
                        parser.parse(parserIterator);
                        ScanState parserState = parser.getState();
                        if (ScanState.SCAN_STARTED.equals(parserState)) {
                            currentParser = parser;
                            currentState = ScanState.SCAN_STARTED;

                            break;
                        } else if (ScanState.SCAN_ENDED.equals(parserState)) {
                            throw new IllegalStateException("The parser state was set to SCAN_ENDED when only SCAN_STARTED was expected.");
                        }
                    }
                }
            }
            else if (member.isContainer()) {
                throw new RuntimeException("A member was a container before it's parent's instructions were parsed.");
            }
            else {
                throw new RuntimeException("A member wasn't an instruction or a container: " + member);
            }
        }
    }
}

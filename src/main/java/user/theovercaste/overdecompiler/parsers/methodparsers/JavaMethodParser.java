package user.theovercaste.overdecompiler.parsers.methodparsers;

import java.io.*;
import java.util.*;

import user.theovercaste.overdecompiler.attributes.*;
import user.theovercaste.overdecompiler.attributes.LineNumberTableAttribute.LineNumberTableValue;
import user.theovercaste.overdecompiler.datahandlers.ClassData;
import user.theovercaste.overdecompiler.datahandlers.MethodData;
import user.theovercaste.overdecompiler.exceptions.*;
import user.theovercaste.overdecompiler.instructions.Instruction;
import user.theovercaste.overdecompiler.instructions.Instructions;
import user.theovercaste.overdecompiler.parserdata.ParsedClass;
import user.theovercaste.overdecompiler.parserdata.ParsedMethod;
import user.theovercaste.overdecompiler.parserdata.method.MethodBlock;
import user.theovercaste.overdecompiler.parserdata.method.MethodMember;
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
                // TODO add block code
                Instruction i = f.load(opcode, din);
                System.out.println("Instruction: " + i.getClass().getName());
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
                public MethodBlock toMethodBlock(List<MethodMember> parsedSubList, Stack<MethodMember> memberStack) {
                    throw new UnsupportedOperationException();
                }
            };
            for (MethodMember member : parseMethodBlockInstructions(fromClass, dummyBaseContainer)) {
                target.addAction(member);
            }
        } catch (IOException | InvalidInstructionException e) {
            e.printStackTrace();
        } catch (InstructionParsingException e) {
            e.printStackTrace();
        }
    }

    private List<MethodMember> parseMethodBlockInstructions(ClassData fromClass, MethodBlockContainer container) throws InstructionParsingException {
        List<MethodMember> ret = new ArrayList<MethodMember>(container.getMembers().size());
        Stack<MethodMember> memberStack = new Stack<MethodMember>();
        parseMethodBlockContainer(container);
        for (MethodBlockContainer.Member member : container.getMembers()) {
            if (member.isContainer()) {
                List<MethodMember> parsedSubList = parseMethodBlockInstructions(fromClass, member.getContainer());
                MethodBlock block = member.getContainer().toMethodBlock(parsedSubList, memberStack);
                ret.addAll(memberStack);
                memberStack.clear();
                ret.add(block);
            } else if (member.isInstruction()) {
                Instruction i = member.getInstruction();
                System.out.println("Found instruction: " + i.getClass().getName() + ": " + i.isAction());
                // i.modifyStack(memberStack);
                if (i.isAction()) {
                    memberStack.push(i.getAction(fromClass, memberStack));
                }
            }
        }
        ret.addAll(memberStack);
        return ret;
    }

    public Iterable<? extends MethodBlockParser> getMethodBlockParsers( ) {
        return methodBlockParsers;
    }

    public void parseMethodBlockContainer(MethodBlockContainer block) {
        List<MethodBlockContainer.Member> members = block.getMembers();
        ScanState currentState = ScanState.NO_MATCH;
        MethodBlockParser currentParser = null;
        List<MethodBlockContainer.Member> currentBlockMembers = new ArrayList<>();
        ListIterator<MethodBlockContainer.Member> listIterator = members.listIterator();
        while (listIterator.hasNext()) {
            int i = listIterator.nextIndex();
            MethodBlockContainer.Member member = listIterator.next();
            ListIterator<MethodBlockContainer.Member> parserListIterator = members.listIterator(i);
            if (member.isInstruction()) {
                if (ScanState.SCAN_STARTED.equals(currentState)) {
                    currentParser.parse(parserListIterator);
                    ScanState parserState = currentParser.getState();
                    switch (parserState) {
                        case SCAN_ENDED: // The current block was parsed, and a new block of the same type didn't start.
                            currentState = ScanState.SCAN_ENDED;
                            for (MethodBlockContainer.Member m : currentBlockMembers) {
                                System.out.println("Member in if block: " + m.getInstruction().getClass().getName());
                            }
                            listIterator.previous();
                            listIterator.add(new MethodBlockContainer.Member(null, currentParser.createContainer(currentBlockMembers)));
                            listIterator.next();
                            currentBlockMembers.clear();
                            currentParser.reset();
                            break;
                        case SCAN_STARTED: // The current block was parsed, and a new block of the same start immediately started again
                            throw new IllegalStateException("The parser state was set to SCAN_STARTED when only SCAN_ENDED was expected.");
                        case NO_MATCH:
                            currentBlockMembers.add(member);
                            listIterator.remove();
                            break;
                        default:
                            throw new RuntimeException("The parser had a state that was unknown! (" + parserState + ")");
                    }
                } else {
                    for (MethodBlockParser parser : getMethodBlockParsers()) {
                        parser.parse(parserListIterator);
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

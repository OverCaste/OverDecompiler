package user.theovercaste.overdecompiler.parsers.javaparser.methodblockparsers;

import java.util.List;
import java.util.ListIterator;

import user.theovercaste.overdecompiler.parsers.javaparser.methodparsers.MethodBlockContainer;
import user.theovercaste.overdecompiler.parsers.javaparser.methodparsers.MethodBlockContainer.Member;

public interface MethodBlockParser {
    public static enum ScanState {
        /** This parser didn't match the specified index */
        NO_MATCH,
        /** This parser found the start of a block, and is creating a new block. Use this parser exclusively */
        SCAN_STARTED,
        /** This parser found the end of a block, and a new block has been created. */
        SCAN_ENDED;
    }

    /**
     * Parse the following instruction(s) to see if our block matches.
     * 
     * @param listIterator an iterator to be traversed checking for the start of this method block.
     */
    public void parse(ListIterator<MethodBlockContainer.Member> listIterator);

    /**
     * Get the state of this method block parser after the previous parser step.
     * 
     * @return {@link ScanState#NO_MATCH} if this parser wasn't already scanning, and there the block header was found.<br>
     *         {@link ScanState#SCAN_STARTED} if this parser wasn't already scanning, and the block header was found.<br>
     *         {@link ScanState#SCAN_ENDED} if the instruction passed the end of this specific block.
     */
    public ScanState getState( );

    /**
     * @param instructions The parsed members to be put in this container after the MethodBlock is created.
     * @return A new MethodBlockC
     */
    public MethodBlockContainer createContainer(List<Member> instructions);

    public void reset( );
}

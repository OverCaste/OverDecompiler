package user.theovercaste.overdecompiler.parsers.methodblockparsers;

import java.util.List;
import java.util.ListIterator;

import user.theovercaste.overdecompiler.parsers.methodparsers.*;
import user.theovercaste.overdecompiler.parsers.methodparsers.MethodBlockContainer.Member;

public interface MethodBlockParser {
    public static enum ScanState {
        /**This parser didn't match the specified index*/
        NO_MATCH,
        /**This parser found the start of a block, and is creating a new block. Use this parser exclusively*/
        SCAN_STARTED,
        /**This parser found the end of a block, and a new block has been created.*/
        SCAN_ENDED;
    }
    
    public ScanState parse(AbstractMethodParser subParser, ListIterator<MethodBlockContainer.Member> listIterator);
    
    public int getBlockHeaderCount( );
    public MethodBlockContainer createContainer(List<Member> instructions);
    public void reset( );
}

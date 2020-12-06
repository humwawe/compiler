package com.ts.parser.ast;

import com.ts.parser.util.ParseException;
import com.ts.parser.util.PeekTokenIterator;

public class Block extends Stmt {
    public Block() {
        super(ASTNodeTypes.BLOCK, "block");
    }

    public static ASTNode parse(PeekTokenIterator it) throws ParseException {
        it.nextMatch("{");
        Block block = new Block();
        ASTNode stmt;
        while ((stmt = Stmt.parseStmt(it)) != null) {
            block.addChild(stmt);
        }
        it.nextMatch("}");
        return block;

    }


}

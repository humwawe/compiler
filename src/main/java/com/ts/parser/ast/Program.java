package com.ts.parser.ast;

import com.ts.parser.util.ParseException;
import com.ts.parser.util.PeekTokenIterator;

public class Program extends Block {
    public Program() {
        super();
    }

    public static ASTNode parse(PeekTokenIterator it) throws ParseException {
        Program block = new Program();
        ASTNode stmt;
        while ((stmt = Stmt.parseStmt(it)) != null) {
            block.addChild(stmt);
        }
        return block;

    }
}

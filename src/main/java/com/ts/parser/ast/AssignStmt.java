package com.ts.parser.ast;


import com.ts.parser.util.ParseException;
import com.ts.parser.util.PeekTokenIterator;

public class AssignStmt extends Stmt {

    public AssignStmt() {
        super(ASTNodeTypes.ASSIGN_STMT, "assign");
    }

    public static ASTNode parse(PeekTokenIterator it) throws ParseException {
        var stmt = new AssignStmt();
        var tkn = it.peek();
        var factor = Factor.parse(it);
        if (factor == null) {
            throw new ParseException(tkn);
        }
        stmt.addChild(factor);
        var lexeme = it.nextMatch("=");
        var expr = Expr.parse(it);
        stmt.addChild(expr);
        stmt.setLexeme(lexeme);
        return stmt;

    }

}

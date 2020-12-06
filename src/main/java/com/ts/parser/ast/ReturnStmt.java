package com.ts.parser.ast;

import com.ts.lexer.Token;
import com.ts.parser.util.ParseException;
import com.ts.parser.util.PeekTokenIterator;

public class ReturnStmt extends Stmt {
    public ReturnStmt() {
        super(ASTNodeTypes.RETURN_STMT, "return");
    }

    public static ASTNode parse(PeekTokenIterator it) throws ParseException {
        Token lexeme = it.nextMatch("return");
        ASTNode expr = Expr.parse(it);

        ReturnStmt stmt = new ReturnStmt();
        stmt.setLexeme(lexeme);
        if (expr != null) {
            stmt.addChild(expr);
        }
        return stmt;
    }


}

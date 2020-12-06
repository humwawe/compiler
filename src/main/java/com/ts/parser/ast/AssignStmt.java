package com.ts.parser.ast;


import com.ts.lexer.Token;
import com.ts.parser.util.ParseException;
import com.ts.parser.util.PeekTokenIterator;

public class AssignStmt extends Stmt {

    public AssignStmt() {
        super(ASTNodeTypes.ASSIGN_STMT, "assign");
    }

    public static ASTNode parse(PeekTokenIterator it) throws ParseException {
        AssignStmt stmt = new AssignStmt();
        Token tkn = it.peek();
        ASTNode factor = Factor.parse(it);
        if (factor == null) {
            throw new ParseException(tkn);
        }
        stmt.addChild(factor);
        Token lexeme = it.nextMatch("=");
        ASTNode expr = Expr.parse(it);
        stmt.addChild(expr);
        stmt.setLexeme(lexeme);
        return stmt;

    }

}

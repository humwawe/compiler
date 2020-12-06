package com.ts.parser.ast;

import com.ts.lexer.Token;
import com.ts.parser.util.ParseException;
import com.ts.parser.util.PeekTokenIterator;

/**
 * 语句
 */
public abstract class Stmt extends ASTNode {
    public Stmt(ASTNodeTypes type, String label) {
        super(type, label);
    }

    public static ASTNode parseStmt(PeekTokenIterator it) throws ParseException {
        if (!it.hasNext()) {
            return null;
        }
        Token token = it.next();
        Token lookahead = it.peek();
        it.putBack();

        if (token.isVariable() && lookahead != null && "=".equals(lookahead.getValue())) {
            return AssignStmt.parse(it);
        } else if ("var".equals(token.getValue())) {
            return DeclareStmt.parse(it);
        } else if ("func".equals(token.getValue())) {
            return FunctionDeclareStmt.parse(it);
        } else if ("return".equals(token.getValue())) {
            return ReturnStmt.parse(it);
        } else if ("if".equals(token.getValue())) {
            return IfStmt.parse(it);
        } else if ("{".equals(token.getValue())) {
            return Block.parse(it);
        } else {
            return Expr.parse(it);
        }

    }


}

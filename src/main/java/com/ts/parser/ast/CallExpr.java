package com.ts.parser.ast;

import com.ts.parser.util.ParseException;
import com.ts.parser.util.PeekTokenIterator;

public class CallExpr extends Expr {
    public CallExpr() {
        super();
        this.label = "call";
        this.type = ASTNodeTypes.CALL_EXPR;

    }

    public static ASTNode parse(ASTNode factor, PeekTokenIterator it) throws ParseException {
        CallExpr expr = new CallExpr();
        expr.addChild(factor);

        it.nextMatch("(");
        ASTNode p = null;
        while ((p = Expr.parse(it)) != null) {
            expr.addChild(p);
            if (!")".equals(it.peek().getValue())) {
                it.nextMatch(",");
            }
        }
        it.nextMatch(")");
        return expr;
    }
}

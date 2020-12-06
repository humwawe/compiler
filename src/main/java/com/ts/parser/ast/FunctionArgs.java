package com.ts.parser.ast;

import com.ts.lexer.Token;
import com.ts.parser.util.ParseException;
import com.ts.parser.util.PeekTokenIterator;

public class FunctionArgs extends ASTNode {
    public FunctionArgs() {
        super();
        this.label = "args";
    }

    public static ASTNode parse(PeekTokenIterator it) throws ParseException {

        FunctionArgs args = new FunctionArgs();

        while (it.peek().isType()) {
            Token type = it.next();
            Variable variable = (Variable) Factor.parse(it);
            variable.setTypeLexeme(type);
            args.addChild(variable);

            if (!")".equals(it.peek().getValue())) {
                it.nextMatch(",");
            }
        }

        return args;
    }


}

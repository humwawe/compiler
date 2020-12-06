package com.ts.parser.ast;


import com.ts.lexer.Token;
import com.ts.lexer.TokenType;
import com.ts.parser.util.PeekTokenIterator;

public class Factor extends ASTNode {
    public Factor(Token token) {
        super();
        this.lexeme = token;
        this.label = token.getValue();
    }

    public static ASTNode parse(PeekTokenIterator it) {
        var token = it.peek();
        var type = token.getType();

        if (type == TokenType.VARIABLE) {
            it.next();
            return new Variable(token);
        } else if (token.isScalar()) {
            it.next();
            return new Scalar(token);
        }
        return null;
    }
}

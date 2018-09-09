package demo.parser;

import demo.lexer.Lexer;
import demo.lexer.LexerUtil;

/**
 * @author hum
 */
public class BasicParser {
    private Lexer lexer;
    private boolean isLegalStatement = true;

    public BasicParser(Lexer lexer) {
        this.lexer = lexer;
    }

    public void statements() {
        // statements -> expression ; | expression ; statements
        expression();

        if (lexer.match(LexerUtil.SEMI)) {
            lexer.advance();
        } else {
            isLegalStatement = false;
            System.out.println("line " + lexer.getYyLineNo() + ": Missing semicolon");
            return;
        }
        if (!lexer.match(LexerUtil.EOI)) {
            statements();
        }
        if (isLegalStatement) {
            System.out.println("The statement is legal");
        }
    }

    private void expression() {
        // expression -> term expression'
        term();
        exprPrime();
    }


    private void term() {
        // term -> factor term'
        factor();
        termPrime();
    }

    private void exprPrime() {
        // expression' -> PLUS term expression' | '空'
        if (lexer.match(LexerUtil.PLUS)) {
            lexer.advance();
            term();
            exprPrime();
        } else if (lexer.match(LexerUtil.UNKNOWN_SYMBOL)) {
            lexer.advance();
            isLegalStatement = false;
            System.out.println("line " + lexer.getYyLineNo() + ": unknown symbol");
        }
    }

    private void termPrime() {
        // term' -> * factor term' | '空'
        if (lexer.match(LexerUtil.TIMES)) {
            lexer.advance();
            factor();
            termPrime();
        }
    }

    private void factor() {
        // factor -> NUM_OR_ID | LP expression RP
        if (lexer.match(LexerUtil.NUM_OR_ID)) {
            lexer.advance();
        } else if (lexer.match(LexerUtil.LP)) {
            lexer.advance();
            expression();
            if (lexer.match(LexerUtil.RP)) {
                lexer.advance();
            } else {
                // 有左括号但没有右括号，错误
                isLegalStatement = false;
                System.out.println("line: " + lexer.getYyLineNo() + " Missing )");
            }
        } else {
            // 不是数字，解析出错
            isLegalStatement = false;
            System.out.println("illegal statements");
        }
    }
}

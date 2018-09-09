package demo.parser;


import demo.lexer.Lexer;
import demo.lexer.LexerUtil;

/**
 * basic->improve 递归转循环
 * @author hum
 */
public class ImprovedParser {

    private Lexer lexer;
    private boolean isLegalStatement = true;

    public ImprovedParser(Lexer lexer) {
        this.lexer = lexer;
    }

    public void statements() {
        while (!lexer.match(LexerUtil.EOI)) {
            expression();

            if (lexer.match(LexerUtil.SEMI)) {
                lexer.advance();
            } else {
                isLegalStatement = false;
                System.out.println("line " + lexer.getYyLineNo() + ": Missing semicolon");
            }
        }


        if (isLegalStatement) {
            System.out.println("The statement is legal");
        }
    }

    private void expression() {
        term();
        while (lexer.match(LexerUtil.PLUS)) {
            lexer.advance();
            term();
        }
        if (lexer.match(LexerUtil.UNKNOWN_SYMBOL)) {
            lexer.advance();
            isLegalStatement = false;
            System.out.println("line " + lexer.getYyLineNo() + ": unknown symbol");
        }
    }


    private void term() {
        factor();
        while (lexer.match(LexerUtil.TIMES)) {
            lexer.advance();
            factor();
        }
    }

    private void factor() {
        if (lexer.match(LexerUtil.NUM_OR_ID)) {
            lexer.advance();
        } else if (lexer.match(LexerUtil.LP)) {
            lexer.advance();
            expression();
            if (lexer.match(LexerUtil.RP)) {
                lexer.advance();
            } else {
                isLegalStatement = false;
                System.out.println("line: " + lexer.getYyLineNo() + " Missing )");
            }
        } else {
            isLegalStatement = false;
            System.out.println("illegal statements");
        }
    }

}


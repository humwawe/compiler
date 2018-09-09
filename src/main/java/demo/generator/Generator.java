package demo.generator;


import demo.lexer.Lexer;
import demo.lexer.LexerUtil;

/**
 * @author hum
 */
public class Generator {
    private Lexer lexer;

    private String[] names = {"t0", "t1", "t2", "t3", "t4", "t5", "t6", "t7"};
    private int nameP = 0;

    public Generator(Lexer lexer) {
        this.lexer = lexer;
    }

    private String newName() {
        if (nameP >= names.length) {
            System.out.println("Expression too complex: " + lexer.getYyLineNo());
            System.exit(1);
        }

        String reg = names[nameP];
        nameP++;
        return reg;
    }

    private void freeNames(String s) {
        if (nameP > 0) {
            names[nameP] = s;
            nameP--;
        } else {
            System.out.println("(Internal error) Name stack underflow: " + lexer.getYyLineNo());
        }
    }


    public void statements() {
        String tempVar = newName();
        expression(tempVar);

        while (lexer.match(LexerUtil.EOI)) {
            expression(tempVar);
            freeNames(tempVar);

            if (lexer.match(LexerUtil.SEMI)) {
                lexer.advance();
            } else {
                System.out.println("Inserting missing semicolon: " + lexer.getYyLineNo());
            }
        }
    }

    private void expression(String tempVar) {
        String tempVar2;
        term(tempVar);
        while (lexer.match(LexerUtil.PLUS)) {
            lexer.advance();
            tempVar2 = newName();
            term(tempVar2);
            System.out.println(tempVar + " += " + tempVar2);
            freeNames(tempVar2);
        }
    }


    private void term(String tempVar) {
        String tempVar2;
        factor(tempVar);
        while (lexer.match(LexerUtil.TIMES)) {
            lexer.advance();
            tempVar2 = newName();
            factor(tempVar2);
            System.out.println(tempVar + " *= " + tempVar2);
            freeNames(tempVar2);
        }

    }

    private void factor(String tempVar) {
        if (lexer.match(LexerUtil.NUM_OR_ID)) {
            System.out.println(tempVar + " = " + lexer.getYyText());
            lexer.advance();
        } else if (lexer.match(LexerUtil.LP)) {
            lexer.advance();
            expression(tempVar);
            if (lexer.match(LexerUtil.RP)) {
                lexer.advance();
            } else {
                System.out.println("Miss matched parenthesis: " + lexer.getYyLineNo());
            }
        } else {
            System.out.println("Number or identifier expected: " + lexer.getYyLineNo());
        }
    }
}


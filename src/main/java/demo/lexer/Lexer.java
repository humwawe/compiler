package demo.lexer;


public class Lexer {
    private int lookAhead = -1;


    private String yyText;
    private int yyLineNo = 1;

    private String current;

    public Lexer() {
        current = LexerUtil.readInput();
    }

    public Lexer(String current) {
        this.current = current;
    }

    private boolean isAlNum(char c) {
        return Character.isAlphabetic(c) || Character.isDigit(c);
    }

    private int lex() {
        for (int i = 0; i < current.length(); i++) {
            int yyLength = 0;
            yyText = current.substring(0, 1);
            switch (current.charAt(i)) {
                case ';':
                    current = current.substring(1);
                    ++yyLineNo;
                    return LexerUtil.SEMI;
                case '+':
                    current = current.substring(1);
                    return LexerUtil.PLUS;
                case '*':
                    current = current.substring(1);
                    return LexerUtil.TIMES;
                case '(':
                    current = current.substring(1);
                    return LexerUtil.LP;
                case ')':
                    current = current.substring(1);
                    return LexerUtil.RP;

                case '\n':
                case '\t':
                case ' ':
                    current = current.substring(1);
                    break;

                default:
                    if (!isAlNum(current.charAt(i))) {
                        current = current.substring(1);
                        return LexerUtil.UNKNOWN_SYMBOL;
                    } else {
                        while (i < current.length() && isAlNum(current.charAt(i))) {
                            i++;
                            yyLength++;
                        }
                        yyText = current.substring(0, yyLength);
                        current = current.substring(yyLength);
                        return LexerUtil.NUM_OR_ID;
                    }
            }
        }
        return LexerUtil.EOI;

    }

    public boolean match(int token) {
        if (lookAhead == -1) {
            lookAhead = lex();
        }
        return token == lookAhead;
    }

    public void advance() {
        lookAhead = lex();
    }

    public void runLexer() {
        while (!match(LexerUtil.EOI)) {
            System.out.println("Token: " + LexerUtil.token(lookAhead) + " ,Symbol: " + yyText);
            advance();
        }
    }


    public int getYyLineNo() {
        return yyLineNo;
    }

    public String getYyText() {
        return yyText;
    }
}

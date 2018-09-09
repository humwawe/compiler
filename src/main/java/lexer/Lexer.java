package lexer;

import java.util.Scanner;


public class Lexer {
    public static final int EOI = 0;
    public static final int SEMI = 1;
    public static final int PLUS = 2;
    public static final int TIMES = 3;
    public static final int LP = 4;
    public static final int RP = 5;
    public static final int NUM_OR_ID = 6;

    private int lookAhead = -1;

    private String yyText = "";
    private int yyLineNo = 0;

    private String current;

    private boolean isAlnum(char c) {
        return Character.isAlphabetic(c) || Character.isDigit(c);
    }

    private void redInput() {
        lookAhead = -1;
        current = "";
        StringBuilder inputBuf = new StringBuilder();

        Scanner s = new Scanner(System.in);
        while (true) {
            String line = s.nextLine();
            if ("end".equals(line)) {
                break;
            }
            inputBuf.append(line);
        }

        if (inputBuf.length() == 0) {
            current = "";
        }
        current = inputBuf.toString().trim();
        ++yyLineNo;
    }

    private int lex() {
        for (int i = 0; i < current.length(); i++) {
            int yyLength = 0;
            yyText = current.substring(0, 1);
            switch (current.charAt(i)) {
                case ';':
                    current = current.substring(1);
                    return SEMI;
                case '+':
                    current = current.substring(1);
                    return PLUS;
                case '*':
                    current = current.substring(1);
                    return TIMES;
                case '(':
                    current = current.substring(1);
                    return LP;
                case ')':
                    current = current.substring(1);
                    return RP;

                case '\n':
                case '\t':
                case ' ':
                    current = current.substring(1);
                    break;

                default:
                    if (!isAlnum(current.charAt(i))) {
                        System.out.println("Ignoring illegal input: " + current.charAt(i));
                    } else {
                        while (i < current.length() && isAlnum(current.charAt(i))) {
                            i++;
                            yyLength++;
                        }
                        yyText = current.substring(0, yyLength);
                        current = current.substring(yyLength);
                        return NUM_OR_ID;
                    }
                    break;
            }
        }
        return EOI;

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
        redInput();
        while (!match(EOI)) {
            System.out.println("Token: " + token() + " ,Symbol: " + yyText);
            advance();
        }
    }

    private String token() {
        String token = "";
        switch (lookAhead) {
            case EOI:
                token = "EOI";
                break;
            case PLUS:
                token = "PLUS";
                break;
            case TIMES:
                token = "TIMES";
                break;
            case NUM_OR_ID:
                token = "NUM_OR_ID";
                break;
            case SEMI:
                token = "SEMI";
                break;
            case LP:
                token = "LP";
                break;
            case RP:
                token = "RP";
                break;
            default:
        }

        return token;
    }
}

package demo.lexer;

import java.util.Scanner;

public class LexerUtil {
    public static final int EOI = 0;
    public static final int SEMI = 1;
    public static final int PLUS = 2;
    public static final int TIMES = 3;
    public static final int LP = 4;
    public static final int RP = 5;
    public static final int NUM_OR_ID = 6;
    public static final int UNKNOWN_SYMBOL = 7;

    public static String readInput() {

        StringBuilder inputBuf = new StringBuilder();

        Scanner s = new Scanner(System.in);
        while (true) {
            String line = s.nextLine();
            if ("end".equals(line)) {
                break;
            }
            inputBuf.append(line);
        }
        return inputBuf.toString().trim();
    }

    public static String token(int lookAhead) {
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

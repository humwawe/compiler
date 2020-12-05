package com.ts.lexer;


import com.ts.common.AlphabetHelper;
import com.ts.common.PeekIterator;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

public class Lexer {

    public List<Token> analyse(PeekIterator<Character> it) throws LexicalException {
        List<Token> tokens = new ArrayList<>();

        while (it.hasNext()) {
            char c = it.next();

            if (c == 0) {
                break;
            }
            char lookahead = it.peek();

            if (c == ' ' || c == '\n') {
                continue;
            }

            // 删除注释
            if (c == '/') {
                if (lookahead == '/') {
                    while (it.hasNext() && (c = it.next()) != '\n') {
                    }
                    ;
                    continue;
                } else if (lookahead == '*') {
                    it.next();//多读一个* 避免/*/通过
                    boolean valid = false;
                    while (it.hasNext()) {
                        char p = it.next();
                        if (p == '*' && it.peek() == '/') {
                            it.next();
                            valid = true;
                            break;
                        }
                    }
                    if (!valid) {
                        throw new LexicalException("comments not match");
                    }
                    continue;
                }
            }

            if (c == '{' || c == '}' || c == '(' || c == ')') {
                tokens.add(new Token(TokenType.BRACKET, c + ""));
                continue;
            }

            if (c == '"' || c == '\'') {
                it.putBack();
                tokens.add(Token.makeString(it));
                continue;
            }

            if (AlphabetHelper.isLetter(c)) {
                it.putBack();
                ;
                tokens.add(Token.makeVarOrKeyword(it));
                continue;
            }


            if (AlphabetHelper.isNumber(c)) {
                it.putBack();
                tokens.add(Token.makeNumber(it));
                continue;
            }


            if ((c == '+' || c == '-' || c == '.') && AlphabetHelper.isNumber(lookahead)) {
                Token lastToken = tokens.size() == 0 ? null : tokens.get(tokens.size() - 1);

                if (lastToken == null || !lastToken.isValue() || lastToken.isOperator()) {
                    it.putBack();
                    tokens.add(Token.makeNumber(it));
                    continue;
                }
            }

            if (AlphabetHelper.isOperator(c)) {
                it.putBack();
                tokens.add(Token.makeOp(it));
                continue;
            }

            throw new LexicalException(c);
        } // end while
        return tokens;
    }

    public List<Token> analyse(Stream source) throws LexicalException {
        PeekIterator<Character> it = new PeekIterator<Character>(source, (char) 0);
        return this.analyse(it);
    }

    /**
     * 从源代码文件加载并解析
     */
    public static List<Token> fromFile(String src) throws FileNotFoundException, UnsupportedEncodingException, LexicalException {
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(src)), "UTF-8"));

        /**
         * 利用BufferedReader每次读取一行
         */
        Iterator<Character> it = new Iterator<Character>() {
            private String line = null;
            private int cursor = 0;

            private void readLine() throws IOException {
                if (line == null || cursor == line.length()) {
                    line = br.readLine();
                    cursor = 0;
                }
            }

            @Override
            public boolean hasNext() {
                try {
                    readLine();
                    return line != null;
                } catch (IOException e) {
                    return false;
                }
            }

            @Override
            public Character next() {
                try {
                    readLine();
                    return line != null ? line.charAt(cursor++) : null;
                } catch (IOException e) {
                    return null;
                }
            }
        };

        PeekIterator<Character> peekIt = new PeekIterator<>(it, '\0');
        Lexer lexer = new Lexer();
        return lexer.analyse(peekIt);

    }
}

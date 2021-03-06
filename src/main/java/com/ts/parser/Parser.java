package com.ts.parser;

import com.ts.common.PeekIterator;
import com.ts.lexer.Lexer;
import com.ts.lexer.Token;
import com.ts.lexer.exception.LexicalException;
import com.ts.parser.ast.ASTNode;
import com.ts.parser.ast.Program;
import com.ts.parser.util.ParseException;
import com.ts.parser.util.PeekTokenIterator;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.List;

public class Parser {
    public static ASTNode parse(String source) throws LexicalException, ParseException {
        Lexer lexer = new Lexer();
        List<Token> tokens = lexer.analyse(new PeekIterator<>(source.chars().mapToObj(x -> (char) x), '\0'));
        return Program.parse(new PeekTokenIterator(tokens.stream()));
    }

    public static ASTNode fromFile(String file) throws FileNotFoundException, UnsupportedEncodingException, LexicalException, ParseException {
        List<Token> tokens = Lexer.fromFile(file);
        return Program.parse(new PeekTokenIterator(tokens.stream()));
    }
}

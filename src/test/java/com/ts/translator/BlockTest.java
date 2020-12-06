package com.ts.translator;

import com.ts.lexer.exception.LexicalException;
import com.ts.parser.Parser;
import com.ts.parser.util.ParseException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BlockTest {

    @Test
    public void test() throws LexicalException, ParseException {

        var source = "var a = 1\n" +
                "{\n" +
                "var b = a * 100\n" +
                "}\n" +
                "{\n" +
                "var b = a * 100\n" +
                "}\n";

        var ast = Parser.parse(source);

        var translator = new Translator();

        var program = translator.translate(ast);


        assertEquals("a = 1\n" +
                "p1 = a * 100\n" +
                "b = p1\n" +
                "p1 = a * 100\n" +
                "b = p1", program.toString());


    }


}
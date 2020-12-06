package com.ts.translator;

import com.ts.lexer.exception.LexicalException;
import com.ts.parser.Parser;
import com.ts.parser.util.ParseException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StaticTableTests {


    @Test
    public void staticTest() throws LexicalException, ParseException {
        var source = "if(a) { a = 1 } else { b = a + 1 * 5 }";
        var astNode = Parser.parse(source);
        var translator = new Translator();
        var program = translator.translate(astNode);

        assertEquals(2, program.getStaticSymbolTable().size());
    }
}

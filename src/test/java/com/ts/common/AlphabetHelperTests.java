package com.ts.common;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AlphabetHelperTests {

    @Test
    public void test() {

        assertTrue(AlphabetHelper.isLetter('a'));
        assertFalse(AlphabetHelper.isLetter('*'));
        assertTrue(AlphabetHelper.isLiteral('a'));
        assertTrue(AlphabetHelper.isLiteral('_'));
        assertTrue(AlphabetHelper.isLiteral('9'));
        assertFalse(AlphabetHelper.isLiteral('*'));
        assertFalse(AlphabetHelper.isLetter('*'));
        assertTrue(AlphabetHelper.isNumber('1'));
        assertTrue(AlphabetHelper.isNumber('9'));
        assertFalse(AlphabetHelper.isNumber('x'));
        assertTrue(AlphabetHelper.isOperator('&'));
        assertTrue(AlphabetHelper.isOperator('*'));
        assertTrue(AlphabetHelper.isOperator('+'));
        assertTrue(AlphabetHelper.isOperator('/'));
        assertTrue(AlphabetHelper.isOperator('='));
        assertTrue(AlphabetHelper.isOperator(','));
        assertFalse(AlphabetHelper.isOperator('a'));

    }
}

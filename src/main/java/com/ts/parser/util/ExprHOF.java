package com.ts.parser.util;

import com.ts.parser.ast.ASTNode;

// HOF: High order function
@FunctionalInterface
public interface ExprHOF {

    ASTNode hoc() throws ParseException;

}

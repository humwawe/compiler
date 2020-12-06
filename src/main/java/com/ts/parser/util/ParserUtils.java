package com.ts.parser.util;

import com.ts.parser.ast.ASTNode;
import com.ts.parser.ast.Factor;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ParserUtils {
    // Prefix
    // Postfix
    public static String toPostfixExpression(ASTNode node) {
        if (node instanceof Factor) {
            return node.getLexeme().getValue();
        }

        List<String> prts = new ArrayList<>();
        for (ASTNode child : node.getChildren()) {
            prts.add(toPostfixExpression(child));
        }
        String lexemeStr = node.getLexeme() != null ? node.getLexeme().getValue() : "";
        if (lexemeStr.length() > 0) {
            return StringUtils.join(prts, " ") + " " + lexemeStr;
        } else {
            return StringUtils.join(prts, " ");
        }
    }

    public static String toBFSString(ASTNode root, int max) {
        LinkedList<ASTNode> queue = new LinkedList<>();
        List<String> list = new ArrayList<>();
        queue.add(root);

        int c = 0;
        while (queue.size() > 0 && c++ < max) {
            ASTNode node = queue.poll();
            list.add(node.getLabel());
            queue.addAll(node.getChildren());
        }
        return StringUtils.join(list, " ");
    }
}

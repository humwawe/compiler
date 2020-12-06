package com.ts.translator;

import com.ts.parser.ast.ASTNode;

import java.util.Hashtable;

public class ExprOptimizer {

    private Hashtable<String, ASTNode> nodes = new Hashtable<>();

    public void optimize(ASTNode node) {
        if (node.isValueType()) {
            node.setProp("hashStr", node.getLexeme().getValue());
        } else {
            StringBuilder hash = new StringBuilder(node.getLexeme().getValue());
            for (ASTNode child : node.getChildren()) {
                optimize(child);
                hash.append("|").append(child.getProp("hashStr"));
            }
            node.setProp("hashStr", hash.toString());
        }
        if (nodes.containsKey(node.getProp("hashStr"))) {
            node.replace(nodes.get(node.getProp("hashStr")));
        } else {
            nodes.put((String) node.getProp("hashStr"), node);
        }
    }

}

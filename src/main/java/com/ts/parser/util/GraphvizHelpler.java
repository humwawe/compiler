package com.ts.parser.util;

import com.ts.parser.ast.ASTNode;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

public class GraphvizHelpler {

    HashMap<ASTNode, String> nodeLabels = new HashMap<>();
    HashSet<String> edgeSet = new HashSet<>();
    int i = 0;


    private String visNode(ASTNode node) {
        nodeLabels.put(node, "v" + ++i);
        return String.format("%s[label=\"%s\"]\n",
                "v" + i,
                node.getLabel()
        );
    }

    private String visEdge(ASTNode a, ASTNode b) {
        String edgeStr = String.format("\"%s\" -> \"%s\"\n",
                nodeLabels.get(a),
                nodeLabels.get(b)
        );
        if (!this.edgeSet.contains(edgeStr)) {
            edgeSet.add(edgeStr);
            return edgeStr;
        }
        return "";
    }

    public String toDot(ASTNode root) {
        LinkedList<ASTNode> queue = new LinkedList<ASTNode>();
        queue.add(root);
        StringBuilder str = new StringBuilder();
        while (queue.size() > 0) {
            ASTNode node = queue.poll();
            if (!nodeLabels.containsKey(node)) {
                str.append(visNode(node));
            }
            for (ASTNode child : node.getChildren()) {
                if (!nodeLabels.containsKey(child)) {
                    str.append(visNode(child));
                }

                str.append(visEdge(node, child));
                queue.add(child);
            }
        }
        return str.toString();
    }

}

package com.ts.parser.ast;

import com.ts.lexer.Token;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class ASTNode {

    protected ArrayList<ASTNode> children = new ArrayList<>();
    protected ASTNode parent;

    /* 关键信息 */
    protected Token lexeme; // 词法单元
    protected String label; // 备注(标签)
    protected ASTNodeTypes type; // 类型


    private final HashMap<String, Object> props = new HashMap<>();

    public ASTNode() {
    }

    public ASTNode(ASTNodeTypes type, String label) {
        this.type = type;
        this.label = label;
    }

    public ASTNode getChild(int index) {
        if (index >= this.children.size()) {
            return null;
        }
        return this.children.get(index);
    }

    public void addChild(ASTNode node) {
        node.parent = this;
        children.add(node);
    }

    public Token getLexeme() {
        return lexeme;
    }

    public List<ASTNode> getChildren() {
        return children;
    }


    public void setLexeme(Token token) {
        this.lexeme = token;
    }

    public void setLabel(String s) {
        this.label = s;
    }

    public ASTNodeTypes getType() {
        return this.type;
    }

    public void setType(ASTNodeTypes type) {
        this.type = type;
    }

    public void print(int indent) {
        if (indent == 0) {
            System.out.println("print:" + this);
        }

        System.out.println(StringUtils.leftPad(" ", indent * 2) + label);
        for (ASTNode child : children) {
            child.print(indent + 1);
        }
    }


    public String getLabel() {
        return this.label;
    }

    public void replaceChild(int i, ASTNode node) {
        this.children.set(i, node);
    }

    public HashMap<String, Object> props() {
        return this.props;
    }

    public Object getProp(String key) {
        if (!this.props.containsKey(key)) {
            return null;
        }
        return this.props.get(key);
    }

    public void setProp(String key, Object value) {
        this.props.put(key, value);
    }


    public boolean isValueType() {
        return this.type == ASTNodeTypes.VARIABLE || this.type == ASTNodeTypes.SCALAR;
    }

    public void replace(ASTNode node) {
        if (this.parent != null) {
            int idx = this.parent.children.indexOf(this);
            this.parent.children.set(idx, node);
            //this.parent = null;
            //this.children = null;
        }
    }
}

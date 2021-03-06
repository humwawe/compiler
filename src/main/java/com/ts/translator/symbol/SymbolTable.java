package com.ts.translator.symbol;

import com.ts.lexer.Token;
import com.ts.lexer.TokenType;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SymbolTable {
    private SymbolTable parent = null;
    private final List<SymbolTable> children;
    private final List<Symbol> symbols;
    private int tempIndex = 0;
    private int offsetIndex = 0;
    private int level = 0;

    public SymbolTable() {
        this.children = new ArrayList<>();
        this.symbols = new ArrayList<>();
    }

    public void addSymbol(Symbol symbol) {
        this.symbols.add(symbol);
        symbol.setParent(this);
    }

    public Symbol cloneFromSymbolTree(Token lexeme, int layerOffset) {
        Optional<Symbol> _symbol = this.symbols.stream()
                .filter(x -> x.lexeme.getValue().equals(lexeme.getValue()))
                .findFirst();
        if (_symbol.isPresent()) {
            Symbol symbol = _symbol.get().copy();
            symbol.setLayerOffset(layerOffset);
            return symbol;
        }
        if (this.parent != null) {
            return this.parent.cloneFromSymbolTree(lexeme, layerOffset + 1);
        }
        return null;
    }

    public boolean exists(Token lexeme) {
        Optional<Symbol> _symbol = this.symbols.stream().filter(x -> x.lexeme.getValue().equals(lexeme.getValue())).findFirst();
        if (_symbol.isPresent()) {
            return true;
        }
        if (this.parent != null) {
            return this.parent.exists(lexeme);
        }
        return false;
    }

    public Symbol createSymbolByLexeme(Token lexeme) {
        Symbol symbol;
        if (lexeme.isScalar()) {
            symbol = Symbol.createImmediateSymbol(lexeme);
            this.addSymbol(symbol);
        } else {
            Optional<Symbol> _symbol = this.symbols.stream().filter(x -> x.getLexeme().getValue().equals(lexeme.getValue())).findFirst();
            if (!_symbol.isPresent()) {
                symbol = cloneFromSymbolTree(lexeme, 0);
                if (symbol == null) {
                    symbol = Symbol.createAddressSymbol(lexeme, this.offsetIndex++);
                }
                this.addSymbol(symbol);
            } else {
                symbol = _symbol.get();
            }

        }
        return symbol;
    }

    public Symbol createVariable() {
        Token lexeme = new Token(TokenType.VARIABLE, "p" + this.tempIndex++);
        Symbol symbol = Symbol.createAddressSymbol(lexeme, this.offsetIndex++);
        this.addSymbol(symbol);
        return symbol;
    }

    public void addChild(SymbolTable child) {
        child.parent = this;
        child.level = this.level + 1;
        this.children.add(child);
    }

    public int localSize() {
        return this.offsetIndex;
    }

    public List<Symbol> getSymbols() {
        return this.symbols;
    }

    public List<SymbolTable> getChildren() {
        return this.children;
    }


    public void createLabel(String label, Token lexeme) {
        Symbol labelSymbol = Symbol.createLabelSymbol(label, lexeme);
        this.addSymbol(labelSymbol);

    }
}

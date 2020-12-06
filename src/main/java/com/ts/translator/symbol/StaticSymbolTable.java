package com.ts.translator.symbol;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class StaticSymbolTable {

    private Hashtable<String, Symbol> offsetMap;
    private int offsetCounter = 0;
    private List<Symbol> symbols;


    public StaticSymbolTable() {
        symbols = new ArrayList<>();
        offsetMap = new Hashtable<>();
    }

    public void add(Symbol symbol) {
        String lexval = symbol.getLexeme().getValue();
        if (!offsetMap.containsKey(lexval)) {
            offsetMap.put(lexval, symbol);
            symbol.setOffset(offsetCounter++);
            symbols.add(symbol);
        } else {
            Symbol sameSymbol = offsetMap.get(lexval);
            symbol.setOffset(sameSymbol.offset);
        }
    }

    public int size() {
        return this.symbols.size();
    }

    @Override
    public String toString() {
        List<String> list = new ArrayList<String>();
        for (int i = 0; i < this.symbols.size(); i++) {
            list.add(i + ":" + this.symbols.get(i).toString());
        }
        return StringUtils.join(list, "\n");
    }

    public List<Symbol> getSymbols() {
        return this.symbols;
    }
}
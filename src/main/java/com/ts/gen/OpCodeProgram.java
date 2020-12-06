package com.ts.gen;

import com.ts.translator.TAProgram;
import com.ts.translator.symbol.Symbol;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class OpCodeProgram {

    Integer entry = null;
    List<Instruction> instructions = new ArrayList<>();
    Hashtable<Integer, String> comments = new Hashtable<>();

    public void add(Instruction i) {
        this.instructions.add(i);
    }

    @Override
    public String toString() {
        List<String> prts = new ArrayList<>();
        for (int i = 0; i < instructions.size(); i++) {
            if (this.comments.containsKey(i)) {
                prts.add("#" + this.comments.get(i));
            }
            String str = instructions.get(i).toString();
            if (this.entry != null && i == this.entry) {
                str = "MAIN:" + str;
            }
            prts.add(str);
        }
        return StringUtils.join(prts, "\n");
    }

    public List<Integer> toByteCodes() {
        List<Integer> codes = new ArrayList<>();

        for (Instruction instruction : instructions) {
            codes.add(instruction.toByteCode());
        }
        return codes;

    }

    public void setEntry(int entry) {
        this.entry = entry;
    }

    public void addComment(String comment) {
        this.comments.put(this.instructions.size(), comment);
    }

    public Integer getEntry() {
        return this.entry;

    }

    public List<Integer> getStaticArea(TAProgram taProgram) {
        List<Integer> list = new ArrayList<>();
        for (Symbol symbol : taProgram.getStaticSymbolTable().getSymbols()) {
            list.add(Integer.parseInt(symbol.getLexeme().getValue()));
        }
        return list;
    }
}

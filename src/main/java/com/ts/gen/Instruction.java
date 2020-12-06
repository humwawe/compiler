package com.ts.gen;

import com.ts.gen.exception.GeneratorException;
import com.ts.gen.operand.*;
import com.ts.translator.symbol.Symbol;
import com.ts.translator.symbol.SymbolType;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class Instruction {


    private static final int MASK_OPCODE = 0xfc000000;
    private static final int MASK_R0 = 0x03e00000;
    private static final int MASK_R1 = 0x001f0000;
    private static final int MASK_R2 = 0x0000f800;
    private static final int MASK_OFFSET0 = 0x03ffffff;
    private static final int MASK_OFFSET1 = 0x001fffff;
    private static final int MASK_OFFSET2 = 0x000007ff;
    private final OpCode code;
    List<Operand> opList = new ArrayList<>();

    public Instruction(OpCode code) {
        this.code = code;
    }

    public static Instruction jump(OpCode code, int offset) {
        Instruction i = new Instruction(code);
        i.opList.add(new Offset(offset));
        return i;

    }


    public static Instruction offsetInstruction(OpCode code, Register r1, Register r2, Offset offset) {
        Instruction i = new Instruction(code);
        i.opList.add(r1);
        i.opList.add(r2);
        i.opList.add(offset);
        return i;

    }

    public static Instruction loadToRegister(Register target, Symbol arg) {
        // 转成整数，目前只支持整数，其他需要大家自己扩展
        if (arg.getType() == SymbolType.ADDRESS_SYMBOL) {
            return offsetInstruction(OpCode.LW, target, Register.SP, new Offset(-arg.getOffset()));
        } else if (arg.getType() == SymbolType.IMMEDIATE_SYMBOL) {
            return offsetInstruction(OpCode.LW, target, Register.STATIC, new Offset(arg.getOffset()));
        }
        throw new NotImplementedException("Cannot load type " + arg.getType() + " symbol to register");
    }

    public static Instruction saveToMemory(Register source, Symbol arg) {
        return offsetInstruction(OpCode.SW, source, Register.SP, new Offset(-arg.getOffset()));
    }

    public static Instruction bne(Register a, Register b, String label) {
        Instruction i = new Instruction(OpCode.BNE);
        i.opList.add(a);
        i.opList.add(b);
        i.opList.add(new Label(label));
        return i;
    }

    public static Instruction register(OpCode code, Register a, Register b, Register c) {
        Instruction i = new Instruction(code);
        i.opList.add(a);
        if (b != null) {
            i.opList.add(b);
        }
        if (c != null) {
            i.opList.add(c);
        }
        return i;
    }


    public static Instruction immediate(OpCode code, Register r, ImmediateNumber number) {
        Instruction i = new Instruction(code);
        i.opList.add(r);
        i.opList.add(number);
        return i;
    }


    public OpCode getOpCode() {
        return this.code;
    }

    @Override
    public String toString() {
        String s = this.code.toString();

        List<String> prts = new ArrayList<>();
        for (Operand op : this.opList) {
            prts.add(op.toString());
        }
        return s + " " + StringUtils.join(prts, " ");
    }

    public static Instruction fromByCode(int code) throws GeneratorException {

        byte byteOpcode = (byte) ((code & MASK_OPCODE) >>> 26);
        OpCode opCode = OpCode.fromByte(byteOpcode);
        Instruction i = new Instruction(opCode);

        switch (opCode.getType()) {
            case IMMEDIATE: {
                int reg = (code & MASK_R0) >> 21;
                int number = code & MASK_OFFSET1;
                i.opList.add(Register.fromAddr(reg));
                i.opList.add(new ImmediateNumber(number));
                break;
            }
            case REGISTER: {
                int r1Addr = (code & MASK_R0) >> 21;
                int r2Addr = (code & MASK_R1) >> 16;
                int r3Addr = (code & MASK_R2) >> 11;
                Register r1 = Register.fromAddr(r1Addr);

                Register r2 = null;
                if (r2Addr != 0) {
                    r2 = Register.fromAddr(r2Addr);
                }
                Register r3 = null;
                if (r3Addr != 0) {
                    r3 = Register.fromAddr(r3Addr);
                }
                i.opList.add(r1);
                if (r2 != null) {
                    i.opList.add(r2);
                }
                if (r3 != null) {
                    i.opList.add(r3);
                }
                break;
            }
            case JUMP: {
                int offset = code & MASK_OFFSET0;
                i.opList.add(Offset.decodeOffset(offset));
                break;
            }
            case OFFSET: {
                int r1Addr = (code & MASK_R0) >> 21;
                int r2Addr = (code & MASK_R1) >> 16;
                int offset = code & MASK_OFFSET2;
                i.opList.add(Register.fromAddr(r1Addr));
                i.opList.add(Register.fromAddr(r2Addr));
                i.opList.add(Offset.decodeOffset(offset));
                break;
            }
            default:
                break;
        }

        return i;

    }

    public Integer toByteCode() {
        int code = 0;
        int x = this.code.getValue();
        code |= x << 26;
        switch (this.code.getType()) {
            case IMMEDIATE: {
                Register r0 = (Register) this.opList.get(0);

                code |= r0.getAddr() << 21;
                code |= ((ImmediateNumber) this.opList.get(1)).getValue();
                return code;
            }
            case REGISTER: {
                Register r1 = (Register) this.opList.get(0);
                code |= r1.getAddr() << 21;
                if (this.opList.size() > 1) {
                    code |= ((Register) this.opList.get(1)).getAddr() << 16;
                    if (this.opList.size() > 2) {
                        byte r2 = ((Register) this.opList.get(2)).getAddr();
                        code |= r2 << 11;
                    }
                }
                break;
            }
            case JUMP:
                if (this.opList.size() > 0) {
                    code |= ((Offset) this.opList.get(0)).getEncodedOffset();
                }
                break;

            case OFFSET:
                Register r1 = (Register) this.opList.get(0);
                Register r2 = (Register) this.opList.get(1);
                Offset offset = (Offset) this.opList.get(2);
                code |= r1.getAddr() << 21;
                code |= r2.getAddr() << 16;
                code |= offset.getEncodedOffset();
                break;
            default:
                break;

        }
        return code;
    }

    public Operand getOperand(int index) {
        return this.opList.get(index);
    }
}

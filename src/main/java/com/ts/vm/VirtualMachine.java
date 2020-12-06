package com.ts.vm;

import com.ts.gen.Instruction;
import com.ts.gen.exception.GeneratorException;
import com.ts.gen.operand.ImmediateNumber;
import com.ts.gen.operand.Offset;
import com.ts.gen.operand.Register;

import java.util.List;

public class VirtualMachine {

    int[] registers = new int[31];
    int[] memory = new int[4096];
    int endProgramSection;
    int startProgram;

    /**
     * 初始化
     */
    public VirtualMachine(List<Integer> staticArea, List<Integer> opcodes, Integer entry) {

        int i = 0;
        /**
         * 静态区
         */
        for (; i < staticArea.size(); i++) {
            memory[i] = staticArea.get(i);
        }

        /**
         * 程序区
         */
        int j = i;
        startProgram = i;
        int mainStart = entry + i;
        for (; i < opcodes.size() + j; i++) {
            memory[i] = opcodes.get(i - j);
        }
        registers[Register.PC.getAddr()] = i - 3;
        endProgramSection = i;

        /**
         * 栈指针
         */
        registers[Register.SP.getAddr()] = 4095;
    }

    private int fetch() {
        int PC = registers[Register.PC.getAddr()];
        return memory[PC];
    }

    private Instruction decode(int code) throws GeneratorException {
        return Instruction.fromByCode(code);
    }

    private void exec(Instruction instruction) {

        byte code = instruction.getOpCode().getValue();
        System.out.println("exec:" + instruction);

        switch (code) {
            case 0x01: { // ADD
                Register r0 = (Register) instruction.getOperand(0);
                Register r1 = (Register) instruction.getOperand(1);
                Register r2 = (Register) instruction.getOperand(2);
                registers[r0.getAddr()] = registers[r1.getAddr()] + registers[r2.getAddr()];
                break;
            }
            case 0x09:
            case 0x02: { // SUB
                Register r0 = (Register) instruction.getOperand(0);
                Register r1 = (Register) instruction.getOperand(1);
                Register r2 = (Register) instruction.getOperand(2);
                registers[r0.getAddr()] = registers[r1.getAddr()] - registers[r2.getAddr()];
                break;
            }
            case 0x03: { // MULT
                Register r0 = (Register) instruction.getOperand(0);
                Register r1 = (Register) instruction.getOperand(1);
                registers[Register.LO.getAddr()] = registers[r0.getAddr()] * registers[r1.getAddr()];
                break;
            }
            case 0x05: { // ADDI
                Register r0 = (Register) instruction.getOperand(0);
                ImmediateNumber r1 = (ImmediateNumber) instruction.getOperand(1);
                registers[r0.getAddr()] += r1.getValue();
                break;
            }
            case 0x06: { // SUBI
                Register r0 = (Register) instruction.getOperand(0);
                ImmediateNumber r1 = (ImmediateNumber) instruction.getOperand(1);
                registers[r0.getAddr()] -= r1.getValue();
                break;
            }
//            case 0x07: // MULTI
//                break;
            case 0x08: { // MFLO
                Register r0 = (Register) instruction.getOperand(0);
                registers[r0.getAddr()] = registers[Register.LO.getAddr()];
                break;
            }
            case 0x10: { // SW
                Register r0 = (Register) instruction.getOperand(0);
                Register r1 = (Register) instruction.getOperand(1);
                Offset offset = (Offset) instruction.getOperand(2);
                int R1VAL = registers[r1.getAddr()];
                memory[(int) (R1VAL + offset.getOffset())] = registers[r0.getAddr()];
                break;
            }
            case 0x11: { //LW
                Register r0 = (Register) instruction.getOperand(0);
                Register r1 = (Register) instruction.getOperand(1);
                Offset offset = (Offset) instruction.getOperand(2);
                int R1VAL = registers[r1.getAddr()];
                registers[r0.getAddr()] = memory[(int) (R1VAL + offset.getOffset())];
                break;
            }
            case 0x15: { // BNE
                Register r0 = (Register) instruction.getOperand(0);
                Register r1 = (Register) instruction.getOperand(1);
                Offset offset = (Offset) instruction.getOperand(2);
                if (registers[r0.getAddr()] != registers[r1.getAddr()]) {
                    registers[Register.PC.getAddr()] = offset.getOffset() + startProgram - 1;
                }
                break;
            }
            case 0x20: { // JUMP
                Offset r0 = (Offset) instruction.getOperand(0);
                registers[Register.PC.getAddr()] = r0.getOffset() + startProgram - 1;
                break;
            }
            case 0x21: { // JR
                Offset r0 = (Offset) instruction.getOperand(0);
                // 将返回地址存入ra
                registers[Register.RA.getAddr()] = registers[Register.PC.getAddr()];
                registers[Register.PC.getAddr()] = r0.getOffset() + startProgram - 1;

                break;
            }
            case 0x22: { // RETURN
                if (instruction.getOperand(0) != null) {
                    // match返回值
                }
                int spVal = registers[Register.SP.getAddr()];
                registers[Register.PC.getAddr()] = memory[spVal];
                break;
            }
            default:
                break;

        }

    }

    public boolean runOneStep() throws GeneratorException {
        int code = fetch();
        Instruction instruction = decode(code);
        exec(instruction);
        registers[Register.PC.getAddr()] += 1;
        System.out.println(registers[Register.PC.getAddr()] + "|" + endProgramSection);
        return registers[Register.PC.getAddr()] < endProgramSection;
    }

    public void run() throws GeneratorException {

        // 模拟CPU循环
        //   fetch: 获取指令
        //   decode: 解码
        //   exec: 执行
        //   PC++
        while (runOneStep()) ;
    }


    public int[] getMemroy() {
        return memory;
    }

    public int[] getRegisters() {
        return registers;
    }


    public int getSpMemory(int offset) {
        int sp = registers[Register.SP.getAddr()];
        return memory[sp + offset];
    }
}

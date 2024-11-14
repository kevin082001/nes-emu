package instructions;

/*
    Created 2024-11-14
 */
public abstract class Instruction {
    //protected String name;
    protected AddressingMode addressingMode;
    protected int opcode;
    protected int bytes;
    protected int cycles;

    /*protected String getName() {
        return name;
    }*/

    protected AddressingMode getAddressingMode() {
        return addressingMode;
    }

    protected int getOpcode() {
        return opcode;
    }

    protected int getBytes() {
        return bytes;
    }

    protected int getCycles() {
        return cycles;
    }
}
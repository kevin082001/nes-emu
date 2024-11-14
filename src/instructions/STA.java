package instructions;

/*
    Created 2024-11-14
 */
public class STA extends Instruction {
    private AddressingMode addressingMode;
    private int opcode;
    private int bytes;
    private int cycles;

    public STA(AddressingMode addressingMode) {
        if (addressingMode == null) {
            throw new IllegalArgumentException("Instruction AddressingMode must not be null");
        }

        this.addressingMode = addressingMode;
        switch (addressingMode) {
            case ZEROPAGE:
                opcode = 0x85;
                bytes = 2;
                cycles = 3;
                break;
            case ZEROPAGE_X:
                opcode = 0x95;
                bytes = 2;
                cycles = 4;
                break;
            case ABSOLUTE:
                opcode = 0x8D;
                bytes = 3;
                cycles = 4;
                break;
            case ABSOLUTE_X:
                opcode = 0x9D;
                bytes = 3;
                cycles = 5;
                break;
            case ABSOLUTE_Y:
                opcode = 0x99;
                bytes = 3;
                cycles = 5;
                break;
            case INDIRECT_X:
                opcode = 0x81;
                bytes = 2;
                cycles = 6;
                break;
            case INDIRECT_Y:
                opcode = 0x91;
                bytes = 2;
                cycles = 6;
                break;
            default:
                throw new IllegalStateException("Invalid addressingMode was specified.");
        }
    }
}

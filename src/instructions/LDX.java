package instructions;

/*
    Created 2024-11-15
 */
public class LDX extends Instruction {
    private AddressingMode addressingMode;
    private int opcode;
    private int bytes;
    private int cycles;

    public LDX(AddressingMode addressingMode, boolean pageCrossed) {
        if (addressingMode == null) {
            throw new IllegalArgumentException("Instruction AddressingMode must not be null");
        }

        this.addressingMode = addressingMode;
        switch (addressingMode) {
            case IMMEDIATE:
                opcode = 0xA2;
                bytes = 2;
                cycles = 2;
                break;
            case ZEROPAGE:
                opcode = 0xA6;
                bytes = 2;
                cycles = 3;
                break;
            case ZEROPAGE_Y:
                opcode = 0xB6;
                bytes = 2;
                cycles = 4;
                break;
            case ABSOLUTE:
                opcode = 0xAE;
                bytes = 3;
                cycles = 4;
                break;
            case ABSOLUTE_Y:
                opcode = 0xBE;
                bytes = 3;
                if (pageCrossed) {
                    cycles = 5;
                } else {
                    cycles = 4;
                }
                break;
            default:
                throw new IllegalStateException("Invalid addressingMode was specified.");
        }
    }
}

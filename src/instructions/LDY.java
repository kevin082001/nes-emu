package instructions;

/*
    Created 2024-11-15
 */
public class LDY extends Instruction {
    private AddressingMode addressingMode;
    private int opcode;
    private int bytes;
    private int cycles;

    public LDY(AddressingMode addressingMode, boolean pageCrossed) {
        if (addressingMode == null) {
            throw new IllegalArgumentException("Instruction AddressingMode must not be null");
        }

        this.addressingMode = addressingMode;
        switch (addressingMode) {
            case IMMEDIATE:
                opcode = 0xA0;
                bytes = 2;
                cycles = 2;
                break;
            case ZEROPAGE:
                opcode = 0xA4;
                bytes = 2;
                cycles = 3;
                break;
            case ZEROPAGE_X:
                opcode = 0xB4;
                bytes = 2;
                cycles = 4;
                break;
            case ABSOLUTE:
                opcode = 0xAC;
                bytes = 3;
                cycles = 4;
                break;
            case ABSOLUTE_X:
                opcode = 0xBC;
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

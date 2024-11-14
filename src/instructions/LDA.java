package instructions;

/*
    Created 2024-11-14
 */
public class LDA extends Instruction {
    //private final String name;
    private AddressingMode addressingMode;
    private int opcode;
    private int bytes;
    private int cycles;

    public LDA(AddressingMode addressingMode, boolean pageCrossed) {
        if (addressingMode == null) {
            throw new IllegalArgumentException("Instruction AddressingMode must not be null");
        }

        //super.[propertyname] = value? instead of this.[propertyname] = value
        this.addressingMode = addressingMode;
        switch (addressingMode) {
            case IMMEDIATE:
                opcode = 0xA9;
                bytes = 2;
                cycles = 2;
                break;
            case ZEROPAGE:
                opcode = 0xA5;
                bytes = 2;
                cycles = 3;
                break;
            case ZEROPAGE_X:
                opcode = 0xB5;
                bytes = 2;
                cycles = 4;
                break;
            case ABSOLUTE:
                opcode = 0xAD;
                bytes = 3;
                cycles = 4;
                break;
            case ABSOLUTE_X:
                opcode = 0xBD;
                bytes = 3;
                if (pageCrossed) {
                    cycles = 5;
                } else {
                    cycles = 4;
                }
                break;
            case ABSOLUTE_Y:
                opcode = 0xB9;
                bytes = 3;
                if (pageCrossed) {
                    cycles = 5;
                } else {
                    cycles = 4;
                }
                break;
            case INDIRECT_X:
                opcode = 0xA1;
                bytes = 2;
                cycles = 6;
                break;
            case INDIRECT_Y:
                opcode = 0xB1;
                bytes = 2;
                if (pageCrossed) {
                    cycles = 6;
                } else {
                    cycles = 5;
                }
                break;
            default:
                throw new IllegalStateException("Invalid addressingMode was specified.");
        }
    }
}

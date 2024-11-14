package instructions;

/*
    Created 2024-11-15
 */
public class STX extends Instruction {
    private AddressingMode addressingMode;
    private int opcode;
    private int bytes;
    private int cycles;

    public STX(AddressingMode addressingMode) {
        if (addressingMode == null) {
            throw new IllegalArgumentException("Instruction AddressingMode must not be null");
        }

        this.addressingMode = addressingMode;
        switch (addressingMode) {
            case ZEROPAGE:
                opcode = 0x86;
                bytes = 2;
                cycles = 3;
                break;
            case ZEROPAGE_Y:
                opcode = 0x96;
                bytes = 2;
                cycles = 4;
                break;
            case ABSOLUTE:
                opcode = 0x8E;
                bytes = 3;
                cycles = 4;
                break;
            default:
                throw new IllegalStateException("Invalid addressingMode was specified.");
        }
    }
}

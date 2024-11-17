/*
    Created 2024-11-13
 */
public class CPU {
    private CpuRAM ram = new CpuRAM();

    //TODO rewrite this logic some time (now, the number can get VERY big)
    // Before instruction --> increment cycle counter --> tell the PPU and APU also to run (PPU: cycles*3)
    // After instruction is finished --> decrement
    private long cycles;
    //private int clocks;

    //Registers and program counter
    private int A;
    private int X;
    private int Y;
    private int S;
    private int P; //status register
    private int PC;

    //See: https://www.nesdev.org/wiki/Status_flags for info
    //Flags / flag bits
    private final int NEGATIVE_BIT = 7;
    private final int OVERFLOW_BIT = 6;
    private final int BREAK_BIT = 4;
    private final int DECIMAL_BIT = 3;
    private final int INTERRUPT_BIT = 2;
    private final int ZERO_BIT = 1;
    private final int CARRY_BIT = 0;

    private int negativeFlag = (1 << NEGATIVE_BIT);
    private int overflowFlag = (1 << OVERFLOW_BIT);
    private int breakFlag = (1 << BREAK_BIT);
    private int decimalFlag = (1 << DECIMAL_BIT);
    private int interruptFlag = (1 << INTERRUPT_BIT);
    private int zeroFlag = (1 << ZERO_BIT);
    private int carryFlag = 0;


    //Instructions

    //TODO write method to parse instruction to know which mode is used
    // Example: "LDA #$00" is immediate because of the # sign (actual value 00)
    //          "LDA $00" means that the value of memory location $00 is loaded (NOT actual value 00)

    //TODO write methods for instructions (instead of own classes)
    // maybe only check for certain opcodes
    // Example:
    public void executeInstruction(int opcode, int value) {
        switch (opcode) {
            case 0xA9:
                lda_immediate(value);
                cycles += 2;
                break;
            case 0xA5:
                lda_zeropage(value);
                cycles += 3;
                break;
            case 0xB5:
                lda_zeropage_x(value);
                cycles += 4;
                break;
            case 0xAD:
                lda_absolute(value);
                cycles += 4;
                break;
            case 0xBD:
                lda_absolute_x(value);
                cycles += 4; //TODO if page crossed it's 5 cycles
                break;
            case 0xB9:
                lda_absolute_y(value);
                cycles += 4; //TODO if page crossed it's 5 cycles
                break;
            case 0xA1:
                lda_indirect_x(value);
                cycles += 6;
                break;
            case 0xB1:
                lda_indirect_y(value);
                cycles += 5; //TODO if page crossed it's 6 cycles
                break;
            case 0xA2:
                ldx_immediate(value);
                cycles += 2;
                break;
            case 0xA6:
                ldx_zeropage(value);
                cycles += 3;
                break;
            case 0xB6:
                ldx_zeropage_y(value);
                cycles += 4;
                break;
            case 0xAE:
                ldx_absolute(value);
                cycles += 4;
                break;
            case 0xBE:
                ldx_absolute_y(value);
                cycles += 4; //TODO if page crossed it's 5 cycles
                break;
            case 0xA0:
                ldy_immediate(value);
                cycles += 2;
                break;
            case 0xA4:
                ldy_zeropage(value);
                cycles += 3;
                break;
            case 0xB4:
                ldy_zeropage_x(value);
                cycles += 4;
                break;
            case 0xAC:
                ldy_absolute(value);
                cycles += 4;
                break;
            case 0xBC:
                ldy_absolute_x(value);
                cycles += 4; //TODO if page crossed it's 5 cycles
                break;
            case 0x85:
                sta_zeropage(value);
                cycles += 3;
                break;
            case 0x95:
                sta_zeropage_x(value);
                cycles += 4;
                break;
            case 0x8D:
                sta_absolute(value);
                cycles += 4;
                break;
            case 0x9D:
                sta_absolute_x(value);
                cycles += 5;
                break;
            case 0x99:
                sta_absolute_y(value);
                cycles += 5;
                break;
            case 0x81:
                sta_indirect_x(value);
                cycles += 6;
                break;
            case 0x91:
                sta_indirect_y(value);
                cycles += 6;
                break;
            case 0x86:
                stx_zeropage(value);
                cycles += 3;
                break;
            case 0x96:
                stx_zeropage_y(value);
                cycles += 4;
                break;
            case 0x8E:
                stx_absolute(value);
                cycles += 4;
                break;
            case 0x84:
                sty_zeropage(value);
                cycles += 3;
                break;
            case 0x94:
                sty_zeropage_x(value);
                cycles += 4;
                break;
            case 0x8C:
                sty_absolute(value);
                cycles += 4;
                break;
        }
    }

    // -- LDA --

    private void lda_immediate(int value) {
        A = value;
        setZeroFlag(A);
        setNegativeFlag(A);
    }

    private void lda_zeropage(int addr) {
        A = ram.read(addr);
        setZeroFlag(A);
        setNegativeFlag(A);
    }

    private void lda_zeropage_x(int addr) {
        //X has value $0F --> "LDA $10, X" --> $0F is added to $10 --> Value of location $001F is loaded into A
        int _addr = addr + X;
        if (_addr > 0xFF) {
            _addr -= 0x100;
        }
        A = ram.read(_addr);
        setZeroFlag(A);
        setNegativeFlag(A);
    }

    private void lda_absolute(int addr) {
        A = ram.read(addr);
        setZeroFlag(A);
        setNegativeFlag(A);
    }

    private void lda_absolute_x(int addr) {
        int _addr = addr + X;
        A = ram.read(_addr);
        setZeroFlag(A);
        setNegativeFlag(A);
    }

    private void lda_absolute_y(int addr) {
        int _addr = addr + Y;
        A = ram.read(_addr);
        setZeroFlag(A);
        setNegativeFlag(A);
    }

    private void lda_indirect_x(int addr) {
        //Not sure if this logic is correct
        int _addr = addr + X;
        if (_addr > 0xFF) {
            _addr -= 0x100;
        }
        A = ram.read(_addr);
        setZeroFlag(A);
        setNegativeFlag(A);
    }

    private void lda_indirect_y(int addr) {
        //Not sure if this logic is correct
        int _addr = addr + Y;
        A = ram.read(_addr);
        setZeroFlag(A);
        setNegativeFlag(A);
    }

    // -- LDX --

    private void ldx_immediate(int value) {
        X = value;
        setZeroFlag(X);
        setNegativeFlag(X);
    }

    private void ldx_zeropage(int addr) {
        X = ram.read(addr);
        setZeroFlag(X);
        setNegativeFlag(X);
    }

    private void ldx_zeropage_y(int addr) {
        int _addr = addr + Y;
        if (_addr > 0xFF) {
            _addr -= 0x100;
        }
        X = ram.read(_addr);
        setZeroFlag(X);
        setNegativeFlag(X);
    }

    private void ldx_absolute(int addr) {
        X = ram.read(addr);
        setZeroFlag(X);
        setNegativeFlag(X);
    }

    private void ldx_absolute_y(int addr) {
        int _addr = addr + Y;
        X = ram.read(_addr);
        setZeroFlag(X);
        setNegativeFlag(X);
    }

    // -- LDY --

    private void ldy_immediate(int value) {
        Y = value;
        setZeroFlag(Y);
        setNegativeFlag(Y);
    }

    private void ldy_zeropage(int addr) {
        Y = ram.read(addr);
        setZeroFlag(Y);
        setNegativeFlag(Y);
    }

    private void ldy_zeropage_x(int addr) {
        int _addr = addr + X;
        if (_addr > 0xFF) {
            _addr -= 0x100;
        }
        Y = ram.read(_addr);
        setZeroFlag(Y);
        setNegativeFlag(Y);
    }

    private void ldy_absolute(int addr) {
        Y = ram.read(addr);
        setZeroFlag(Y);
        setNegativeFlag(Y);
    }

    private void ldy_absolute_x(int addr) {
        int _addr = addr + X;
        Y = ram.read(_addr);
        setZeroFlag(Y);
        setNegativeFlag(Y);
    }

    // -- STA --

    private void sta_zeropage(int addr) {
        ram.write(addr, A);
    }

    private void sta_zeropage_x(int addr) {
        int _addr = addr + X;
        if (_addr > 0xFF) {
            _addr -= 0x100;
        }
        ram.write(_addr, A);
    }

    private void sta_absolute(int addr) {
        ram.write(addr, A);
    }

    private void sta_absolute_x(int addr) {
        int _addr = addr + X;
        ram.write(_addr, A);
    }

    private void sta_absolute_y(int addr) {
        int _addr = addr + Y;
        ram.write(_addr, A);
    }

    private void sta_indirect_x(int addr) {
        int _addr = addr + X;
        if (_addr > 0xFF) {
            _addr -= 0x100;
        }
        ram.write(_addr, A);
    }

    private void sta_indirect_y(int addr) {
        int _addr = addr + Y;
        ram.write(_addr, A);
    }

    // -- STX --

    private void stx_zeropage(int addr) {
        ram.write(addr, X);
    }

    private void stx_zeropage_y(int addr) {
        int _addr = addr + Y;
        if (_addr > 0xFF) {
            _addr -= 0x100;
        }
        ram.write(_addr, X);
    }

    private void stx_absolute(int addr) {
        ram.write(addr, X);
    }

    // -- STY --

    private void sty_zeropage(int addr) {
        ram.write(addr, Y);
    }

    private void sty_zeropage_x(int addr) {
        int _addr = addr + X;
        if (_addr > 0xFF) {
            _addr -= 0x100;
        }
        ram.write(_addr, Y);
    }

    private void sty_absolute(int addr) {
        ram.write(addr, Y);
    }


    private void setZeroFlag(int value) {
        zeroFlag = value == 0 ? 1 : 0;
    }

    private void setNegativeFlag(int value) {
        String binary = String.format("%8s", Integer.toBinaryString(value)).replace(' ', '0'); //8-bit binary
        negativeFlag = binary.charAt(0) == '1' ? 1 : 0;
    }

    public int read_a() {
        return A;
    }

    public int read_x() {
        return X;
    }

    public int read_y() {
        return Y;
    }

    public long get_cycles() {
        return cycles;
    }

    public CpuRAM getRam() {
        return ram;
    }
}

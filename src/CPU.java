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
    //private int stack;
    private int stackPointer; //stack is from $0100 to $01FF
    private StatusRegister status; //status register
    private int PC;
    private boolean pageCrossed;

    private byte[] prgRom;
    private byte[] chrRom;

    private final byte[] romFile;

    public CPU(int prgRom, int chrRom, byte[] romFile) {
        this.prgRom = new byte[prgRom];
        this.chrRom = new byte[chrRom];
        this.romFile = romFile;
        this.status = new StatusRegister();

        //when pushing to the stack:
        this.stackPointer = 0xFF;
        PC = 16;
        //reset(); //TODO reset() is broken
    }

    public void increment_pc(int bytes) {
        PC = (PC + bytes) & 0xFFFF;
    }

    public void reset() {
        // Read initial program counter value from reset vector
        int prgSize = prgRom.length;

        //int low = ram.read(prgRom[prgSize - 4]);
        //int hi = ram.read(prgRom[prgSize - 3]);

        System.out.println(romFile[romFile.length - 4]);
        System.out.println(romFile[romFile.length - 3]);

        int low = romFile[romFile.length - 4];
        int hi = romFile[romFile.length - 3];

        //int low = ram.read(0xFFFC);
        //int hi = ram.read(0xFFFD);

        PC = (hi << 8) | low;
    }

    //Instructions

    public void executeInstruction(int opcode, int value) {
        //TODO cycle / clock speed management

        switch (opcode) {
            case 0x69:
                adc_immediate(value);
                cycles += 2;
                increment_pc(2);
                break;
            case 0x65:
                adc_zeropage(value);
                cycles += 3;
                increment_pc(2);
                break;
            case 0x75:
                adc_zeropage_x(value);
                cycles += 4;
                increment_pc(2);
                break;
            case 0x6D:
                adc_absolute(value);
                cycles += 4;
                increment_pc(3);
                break;
            case 0x7D:
                adc_absolute_x(value);
                cycles += 4 + (pageCrossed ? 1 : 0);
                increment_pc(3);
                break;
            case 0x79:
                adc_absolute_y(value);
                cycles += 4 + (pageCrossed ? 1 : 0);
                increment_pc(3);
                break;
            case 0x61:
                adc_indirect_x(value);
                cycles += 6;
                increment_pc(2);
                break;
            case 0x71:
                adc_indirect_y(value);
                cycles += 5 + (pageCrossed ? 1 : 0);
                increment_pc(2);
                break;
            case 0x29:
                and_immediate(value);
                cycles += 2;
                increment_pc(2);
                break;
            case 0x25:
                and_zeropage(value);
                cycles += 3;
                increment_pc(2);
                break;
            case 0x35:
                and_zeropage_x(value);
                cycles += 4;
                increment_pc(2);
                break;
            case 0x2D:
                and_absolute(value);
                cycles += 4;
                increment_pc(3);
                break;
            case 0x3D:
                and_absolute_x(value);
                cycles += 4 + (pageCrossed ? 1 : 0);
                increment_pc(3);
                break;
            case 0x39:
                and_absolute_y(value);
                cycles += 4 + (pageCrossed ? 1 : 0);
                increment_pc(3);
                break;
            case 0x21:
                and_indirect_x(value);
                cycles += 6;
                increment_pc(2);
                break;
            case 0x31:
                and_indirect_y(value);
                cycles += 5 + (pageCrossed ? 1 : 0);
                increment_pc(2);
                break;
            case 0x0A:
                asl_accumulator();
                cycles += 2;
                increment_pc(1);
                break;
            case 0x06:
                asl_zeropage(value);
                cycles += 5;
                increment_pc(2);
                break;
            case 0x16:
                asl_zeropage_x(value);
                cycles += 6;
                increment_pc(2);
                break;
            case 0x0E:
                asl_absolute(value);
                cycles += 6;
                increment_pc(3);
                break;
            case 0x1E:
                asl_absolute_x(value);
                cycles += 7;
                increment_pc(3);
                break;
            case 0x90:
                bcc(value);
                //TODO cycles: 2 (3 if branch taken, 4 if page crossed)
                break;
            case 0xB0:
                bcs(value);
                //TODO cycles: 2 (3 if branch taken, 4 if page crossed)
                break;
            case 0xF0:
                beq(value);
                break;
            case 0x24:
                bit_zeropage(value);
                cycles += 3;
                increment_pc(2);
                break;
            case 0x2C:
                bit_absolute(value);
                cycles += 4;
                increment_pc(3);
                break;
            case 0x30:
                bmi(value);
                break;
            case 0xD0:
                bne(value);
                break;
            case 0x10:
                bpl(value);
                break;
            case 0x00:
                brk();
                cycles += 7;
                increment_pc(2);
                break;
            case 0x50:
                bvc(value);
                break;
            case 0x70:
                bvs(value);
                break;
            case 0x18:
                clc();
                cycles += 2;
                increment_pc(2);
                break;
            case 0xD8:
                cld();
                cycles += 2;
                increment_pc(1);
                break;
            case 0x58:
                cli();
                cycles += 2;
                increment_pc(1);
                break;
            case 0xB8:
                clv();
                cycles += 2;
                increment_pc(1);
                break;
            case 0xC9:
                cmp_immediate(value);
                cycles += 2;
                increment_pc(2);
                break;
            case 0xC5:
                cmp_zeropage(value);
                cycles += 3;
                increment_pc(2);
                break;
            case 0xD5:
                cmp_zeropage_x(value);
                cycles += 4;
                increment_pc(2);
                break;
            case 0xCD:
                cmp_absolute(value);
                cycles += 4;
                increment_pc(3);
                break;
            case 0xDD:
                cmp_absolute_x(value);
                cycles += 4; //TODO if page crossed it's 5 cycles
                increment_pc(3);
                break;
            case 0xD9:
                cmp_absolute_y(value);
                cycles += 4; //TODO if page crossed it's 5 cycles
                increment_pc(3);
                break;
            case 0xC1:
                cmp_indirect_x(value);
                cycles += 6;
                increment_pc(2);
                break;
            case 0xD1:
                cmp_indirect_y(value);
                cycles += 5; //TODO if page crossed it's 6 cycles
                increment_pc(2);
                break;
            case 0xE0:
                cpx_immediate(value);
                cycles += 2;
                increment_pc(2);
                break;
            case 0xE4:
                cpx_zeropage(value);
                cycles += 3;
                increment_pc(2);
                break;
            case 0xEC:
                cpx_absolute(value);
                cycles += 4;
                increment_pc(3);
                break;
            case 0xC0:
                cpy_immediate(value);
                cycles += 2;
                increment_pc(2);
                break;
            case 0xC4:
                cpy_zeropage(value);
                cycles += 3;
                increment_pc(2);
                break;
            case 0xCC:
                cpy_absolute(value);
                cycles += 4;
                increment_pc(3);
                break;
            case 0xC6:
                dec_zeropage(value);
                cycles += 5;
                increment_pc(2);
                break;
            case 0xD6:
                dec_zeropage_x(value);
                cycles += 6;
                increment_pc(2);
                break;
            case 0xCE:
                dec_absolute(value);
                cycles += 6;
                increment_pc(3);
                break;
            case 0xDE:
                dec_absolute_x(value);
                cycles += 7;
                increment_pc(3);
                break;
            case 0xCA:
                dex();
                cycles += 2;
                increment_pc(1);
                break;
            case 0x88:
                dey();
                cycles += 2;
                increment_pc(1);
                break;
            case 0xA9:
                lda_immediate(value);
                cycles += 2;
                increment_pc(2);
                break;
            case 0xA5:
                lda_zeropage(value);
                cycles += 3;
                increment_pc(2);
                break;
            case 0xB5:
                lda_zeropage_x(value);
                cycles += 4;
                increment_pc(2);
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
                cycles += (5 + (pageCrossed ? 1 : 0)); //TODO if page crossed it's 6 cycles
                break;
            case 0xA2:
                ldx_immediate(value);
                cycles += 2;
                increment_pc(2);
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
                increment_pc(2);
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
            case 0x78:
                sei();
                cycles += 2;
                increment_pc(1);
                break;
            case 0x85:
                sta_zeropage(value);
                cycles += 3;
                increment_pc(2);
                break;
            case 0x95:
                sta_zeropage_x(value);
                cycles += 4;
                break;
            case 0x8D:
                sta_absolute(value);
                cycles += 4;
                increment_pc(3);
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
                increment_pc(2);
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
                increment_pc(2);
                break;
            case 0x94:
                sty_zeropage_x(value);
                cycles += 4;
                break;
            case 0x8C:
                sty_absolute(value);
                cycles += 4;
                break;
            case 0x4C:
                jmp_absolute(value); //TODO implement correctly
                cycles += 3;
                increment_pc(3);
                break;
            case 0x6C:
                jmp_indirect(value);
                cycles += 5;
                increment_pc(3);
            case 0xE8:
                inx();
                cycles += 2;
                increment_pc(1);
                break;
            case 0xAA:
                tax();
                cycles += 2;
                increment_pc(1);
                break;
            case 0xA8:
                tay();
                cycles += 2;
                increment_pc(1);
                break;
            case 0xBA:
                tsx();
                cycles += 2;
                increment_pc(1);
                break;
            case 0x8A:
                txa();
                cycles += 2;
                increment_pc(1);
                break;
            case 0x98:
                tya();
                cycles += 2;
                increment_pc(1);
                break;
            case 0x9A:
                txs();
                cycles += 2;
                increment_pc(1);
                break;
            default:
                System.out.println("opcode is either invalid or not implemented yet.");
                break;
        }
    }

    // -- ADC --

    private void adc_immediate(int value) {
        int result = value + A + (status.isCarryFlagSet() ? 1 : 0);
        A = result;
        setCarryFlag(value);
        setZeroFlag(value);
        setOverflowFlag(value, result);
        setNegativeFlag(value);
    }

    private void adc_zeropage(int addr) {
        int value = ram.read(addr);
        int result = value + A + (status.isCarryFlagSet() ? 1 : 0);
        A = result;
        setCarryFlag(value);
        setZeroFlag(value);
        setOverflowFlag(value, result);
        setNegativeFlag(value);
    }

    private void adc_zeropage_x(int addr) {
        int _addr = addr + X;
        if (_addr > 0xFF) {
            _addr -= 0x100;
        }
        int value = ram.read(_addr);
        int result = value + A + (status.isCarryFlagSet() ? 1 : 0);
        A = result;
        setCarryFlag(value);
        setZeroFlag(value);
        setOverflowFlag(value, result);
        setNegativeFlag(value);
    }

    private void adc_absolute(int addr) {
        int value = ram.read(addr);
        int result = value + A + (status.isCarryFlagSet() ? 1 : 0);
        A = result;
        setCarryFlag(value);
        setZeroFlag(value);
        setOverflowFlag(value, result);
        setNegativeFlag(value);
    }

    private void adc_absolute_x(int addr) {
        int _addr = addr + X;
        int value = ram.read(_addr);
        int result = value + A + (status.isCarryFlagSet() ? 1 : 0);
        A = result;
        setCarryFlag(value);
        setZeroFlag(value);
        setOverflowFlag(value, result);
        setNegativeFlag(value);
    }

    private void adc_absolute_y(int addr) {
        int _addr = addr + Y;
        int value = ram.read(_addr);
        int result = value + A + (status.isCarryFlagSet() ? 1 : 0);
        A = result;
        setCarryFlag(value);
        setZeroFlag(value);
        setOverflowFlag(value, result);
        setNegativeFlag(value);
    }

    private void adc_indirect_x(int addr) {
        int _addr = addr + X;
        if (_addr > 0xFF) {
            _addr -= 0x100;
        }
        int value = ram.read(_addr);
        int result = value + A + (status.isCarryFlagSet() ? 1 : 0);
        A = result;
        setCarryFlag(value);
        setZeroFlag(value);
        setOverflowFlag(value, result);
        setNegativeFlag(value);
    }

    private void adc_indirect_y(int addr) {
        int _addr = addr + Y;
        int value = ram.read(_addr);
        int result = value + A + (status.isCarryFlagSet() ? 1 : 0);
        A = result;
        setCarryFlag(value);
        setZeroFlag(value);
        setOverflowFlag(value, result);
        setNegativeFlag(value);
    }

    // -- AND --

    private void and_immediate(int value) {
        A &= value;
        setZeroFlag(A);
        setNegativeFlag(A);
    }

    private void and_zeropage(int addr) {
        int value = ram.read(addr);
        A &= value;
        setZeroFlag(A);
        setNegativeFlag(A);
    }

    private void and_zeropage_x(int addr) {
        int _addr = addr + X;
        if (_addr > 0xFF) {
            _addr -= 0x100;
        }
        int value = ram.read(_addr);
        A &= value;
        setZeroFlag(A);
        setNegativeFlag(A);
    }

    private void and_absolute(int addr) {
        int value = ram.read(addr);
        A &= value;
        setZeroFlag(A);
        setNegativeFlag(A);
    }

    private void and_absolute_x(int addr) {
        int _addr = addr + X;
        int value = ram.read(_addr);
        A &= value;
        setZeroFlag(A);
        setNegativeFlag(A);
    }

    private void and_absolute_y(int addr) {
        int _addr = addr + Y;
        int value = ram.read(_addr);
        A &= value;
        setZeroFlag(A);
        setNegativeFlag(A);
    }

    private void and_indirect_x(int addr) {
        int _addr = addr + X;
        if (_addr > 0xFF) {
            _addr -= 0x100;
        }
        int value = ram.read(_addr);
        A &= value;
        setZeroFlag(A);
        setNegativeFlag(A);
    }

    private void and_indirect_y(int addr) {
        int _addr = addr + Y;
        int value = ram.read(_addr);
        A &= value;
        setZeroFlag(A);
        setNegativeFlag(A);
    }

    // -- ASL --

    private void asl_accumulator() {
        int bit7 = (int) Integer.toBinaryString(A).charAt(0);
        A <<= 1;
        status.setCarryFlagSet(bit7 == 1);
        setZeroFlag(A);
        setNegativeFlag(A);
    }

    private void asl_zeropage(int addr) {
        int value = ram.read(addr);
        int bit7 = (int) Integer.toBinaryString(value).charAt(0);
        value <<= 1;
        ram.write(addr, value);

        status.setCarryFlagSet(bit7 == 1);
        setZeroFlag(value);
        setNegativeFlag(value);
    }

    private void asl_zeropage_x(int addr) {
        int _addr = addr + X;
        if (_addr > 0xFF) {
            _addr -= 0x100;
        }
        int value = ram.read(_addr);
        int bit7 = (int) Integer.toBinaryString(value).charAt(0);
        value <<= 1;
        ram.write(_addr, value);

        status.setCarryFlagSet(bit7 == 1);
        setZeroFlag(value);
        setNegativeFlag(value);
    }

    private void asl_absolute(int addr) {
        int value = ram.read(addr);
        int bit7 = (int) Integer.toBinaryString(value).charAt(0);
        value <<= 1;
        ram.write(addr, value);

        status.setCarryFlagSet(bit7 == 1);
        setZeroFlag(value);
        setNegativeFlag(value);
    }

    private void asl_absolute_x(int addr) {
        int _addr = addr + X;
        int value = ram.read(_addr);
        int bit7 = (int) Integer.toBinaryString(value).charAt(0);
        value <<= 1;
        ram.write(_addr, value);

        status.setCarryFlagSet(bit7 == 1);
        setZeroFlag(value);
        setNegativeFlag(value);
    }

    // -- BCC --

    private void bcc(int addr) {
        int value = ram.read(addr);
        PC += 2;
        cycles += 2;
        if (!status.isCarryFlagSet()) {
            cycles++;
            //branch(addr);
            branch(value);
        }
    }

    // -- BCS --

    private void bcs(int addr) {
        int value = ram.read(addr);
        increment_pc(2);
        cycles += 2;
        if (status.isCarryFlagSet()) {
            cycles++;
            //branch(addr);
            branch(value);
        }
    }

    // -- BEQ --

    private void beq(int addr) {
        int value = ram.read(addr);
        increment_pc(2);
        cycles += 2;
        if (status.isZeroFlagSet()) {
            cycles++;
            branch(value);
        }
    }

    // -- BIT --
    private void bit_zeropage(int addr) {
        int value = ram.read(addr);
        int result = A & value;
        //String binary = Integer.toBinaryString(result);
        String binary = Integer.toBinaryString(value);

        setZeroFlag(result);
        status.setOverflowFlagSet((int) binary.charAt(1) == 1);
        status.setNegativeFlagSet((int) binary.charAt(0) == 1);
    }

    private void bit_absolute(int addr) {
        int value = ram.read(addr);
        int result = A & value;
        //String binary = Integer.toBinaryString(result);
        String binary = Integer.toBinaryString(value);

        setZeroFlag(result);
        status.setOverflowFlagSet((int) binary.charAt(1) == 1);
        status.setNegativeFlagSet((int) binary.charAt(0) == 1);
    }

    // -- BMI --

    private void bmi(int addr) {
        int value = ram.read(addr);
        increment_pc(2);
        cycles += 2;
        if (status.isNegativeFlagSet()) {
            cycles++;
            branch(value);
        }
    }

    // -- BNE --

    private void bne(int addr) {
        int value = ram.read(addr);
        increment_pc(2);
        cycles += 2;
        if (!status.isZeroFlagSet()) {
            cycles++;
            branch(value);
        }
    }

    // -- BPL --

    private void bpl(int addr) {
        int value = ram.read(addr);
        increment_pc(2);
        cycles += 2;
        if (!status.isNegativeFlagSet()) {
            cycles++;
            branch(value);
        }
    }

    // -- BRK --

    private void brk() {
        //TODO implement
    }

    // -- BVC --

    private void bvc(int addr) {
        int value = ram.read(addr);
        increment_pc(2);
        cycles += 2;
        if (!status.isOverflowFlagSet()) {
            cycles++;
            branch(value);
        }
    }

    // -- BVS --

    private void bvs(int addr) {
        int value = ram.read(addr);
        increment_pc(2);
        cycles += 2;
        if (status.isOverflowFlagSet()) {
            cycles++;
            branch(value);
        }
    }

    // -- Clear flags (CLC, CLD, CLI, CLV) --

    private void clc() {
        status.setCarryFlagSet(false);
    }

    private void cld() {
        status.setDecimalFlagSet(false);
    }

    private void cli() {
        status.setInterruptFlagSet(false);
    }

    private void clv() {
        status.setOverflowFlagSet(false);
    }

    // -- CMP --

    private void cmp_immediate(int value) {
        int result = A - value;
        int bit7 = (int) Integer.toBinaryString(result).charAt(0);
        status.setCarryFlagSet(A >= value);
        status.setZeroFlagSet(A == value);
        status.setNegativeFlagSet(bit7 == 1);
    }

    private void cmp_zeropage(int addr) {
        int value = ram.read(addr);
        int result = A - value;
        int bit7 = (int) Integer.toBinaryString(result).charAt(0);
        status.setCarryFlagSet(A >= value);
        status.setZeroFlagSet(A == value);
        status.setNegativeFlagSet(bit7 == 1);
    }

    private void cmp_zeropage_x(int addr) {
        int _addr = addr + Y;
        if (_addr > 0xFF) {
            _addr -= 0x100;
        }
        int value = ram.read(_addr);
        int result = A - value;
        int bit7 = (int) Integer.toBinaryString(result).charAt(0);
        status.setCarryFlagSet(A >= value);
        status.setZeroFlagSet(A == value);
        status.setNegativeFlagSet(bit7 == 1);
    }

    private void cmp_absolute(int addr) {
        int value = ram.read(addr);
        int result = A - value;
        int bit7 = (int) Integer.toBinaryString(result).charAt(0);
        status.setCarryFlagSet(A >= value);
        status.setZeroFlagSet(A == value);
        status.setNegativeFlagSet(bit7 == 1);
    }

    private void cmp_absolute_x(int addr) {
        int _addr = addr + X;
        int value = ram.read(_addr);
        int result = A - value;
        int bit7 = (int) Integer.toBinaryString(result).charAt(0);
        status.setCarryFlagSet(A >= value);
        status.setZeroFlagSet(A == value);
        status.setNegativeFlagSet(bit7 == 1);
    }

    private void cmp_absolute_y(int addr) {
        int _addr = addr + Y;
        int value = ram.read(_addr);
        int result = A - value;
        int bit7 = (int) Integer.toBinaryString(result).charAt(0);
        status.setCarryFlagSet(A >= value);
        status.setZeroFlagSet(A == value);
        status.setNegativeFlagSet(bit7 == 1);
    }

    private void cmp_indirect_x(int addr) {
        int _addr = addr + X;
        if (_addr > 0xFF) {
            _addr -= 0x100;
        }
        int value = ram.read(_addr);
        int result = A - value;
        int bit7 = (int) Integer.toBinaryString(result).charAt(0);
        status.setCarryFlagSet(A >= value);
        status.setZeroFlagSet(A == value);
        status.setNegativeFlagSet(bit7 == 1);
    }

    private void cmp_indirect_y(int addr) {
        int _addr = addr + Y;
        int value = ram.read(_addr);
        int result = A - value;
        int bit7 = (int) Integer.toBinaryString(result).charAt(0);
        status.setCarryFlagSet(A >= value);
        status.setZeroFlagSet(A == value);
        status.setNegativeFlagSet(bit7 == 1);
    }

    // -- CPX --

    private void cpx_immediate(int value) {
        int result = X - value;
        int bit7 = (int) Integer.toBinaryString(result).charAt(0);
        status.setCarryFlagSet(X >= value);
        status.setZeroFlagSet(X == value);
        status.setNegativeFlagSet(bit7 == 1);
    }

    private void cpx_zeropage(int addr) {
        int value = ram.read(addr);
        int result = X - value;
        int bit7 = (int) Integer.toBinaryString(result).charAt(0);
        status.setCarryFlagSet(X >= value);
        status.setZeroFlagSet(X == value);
        status.setNegativeFlagSet(bit7 == 1);
    }

    private void cpx_absolute(int addr) {
        int value = ram.read(addr);
        int result = X - value;
        int bit7 = (int) Integer.toBinaryString(result).charAt(0);
        status.setCarryFlagSet(X >= value);
        status.setZeroFlagSet(X == value);
        status.setNegativeFlagSet(bit7 == 1);
    }

    // -- CPY --

    private void cpy_immediate(int value) {
        int result = Y - value;
        int bit7 = (int) Integer.toBinaryString(result).charAt(0);
        status.setCarryFlagSet(Y >= value);
        status.setZeroFlagSet(Y == value);
        status.setNegativeFlagSet(bit7 == 1);
    }

    private void cpy_zeropage(int addr) {
        int value = ram.read(addr);
        int result = Y - value;
        int bit7 = (int) Integer.toBinaryString(result).charAt(0);
        status.setCarryFlagSet(Y >= value);
        status.setZeroFlagSet(Y == value);
        status.setNegativeFlagSet(bit7 == 1);
    }

    private void cpy_absolute(int addr) {
        int value = ram.read(addr);
        int result = Y - value;
        int bit7 = (int) Integer.toBinaryString(result).charAt(0);
        status.setCarryFlagSet(Y >= value);
        status.setZeroFlagSet(Y == value);
        status.setNegativeFlagSet(bit7 == 1);
    }

    // -- DEC --

    private void dec_zeropage(int addr) {
        int value = ram.read(addr);
        int result = value - 1;
        int bit7 = (int) Integer.toBinaryString(result).charAt(0);
        ram.write(addr, result);

        setZeroFlag(result);
        status.setNegativeFlagSet(bit7 == 1);
    }

    private void dec_zeropage_x(int addr) {
        int _addr = addr + X;
        if (_addr > 0xFF) {
            _addr -= 0x100;
        }
        int value = ram.read(_addr);
        int result = value - 1;
        int bit7 = (int) Integer.toBinaryString(result).charAt(0);
        ram.write(addr, result);

        setZeroFlag(result);
        status.setNegativeFlagSet(bit7 == 1);
    }

    private void dec_absolute(int addr) {
        int value = ram.read(addr);
        int result = value - 1;
        int bit7 = (int) Integer.toBinaryString(result).charAt(0);
        ram.write(addr, result);

        setZeroFlag(result);
        status.setNegativeFlagSet(bit7 == 1);
    }

    private void dec_absolute_x(int addr) {
        int _addr = addr + X;
        int value = ram.read(_addr);
        int result = value - 1;
        int bit7 = (int) Integer.toBinaryString(result).charAt(0);
        ram.write(addr, result);

        setZeroFlag(result);
        status.setNegativeFlagSet(bit7 == 1);
    }

    // -- DEX, DEY --

    private void dex() {
        X--;
        int bit7 = (int) Integer.toBinaryString(X).charAt(0);

        status.setZeroFlagSet(X == 0);
        status.setNegativeFlagSet(bit7 == 1);
    }

    private void dey() {
        Y--;
        int bit7 = (int) Integer.toBinaryString(Y).charAt(0);

        status.setZeroFlagSet(Y == 0);
        status.setNegativeFlagSet(bit7 == 1);
    }

    // -- LDA --

    private void lda_immediate(int value) {
        A = value;
        setZeroFlag(A);
        setNegativeFlag(A);

        //System.out.println("LDA #$" + Integer.toHexString(value));
    }

    private void lda_zeropage(int addr) {
        A = ram.read(addr);
        setZeroFlag(A);
        setNegativeFlag(A);
    }

    private void lda_zeropage_x(int addr) {
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
        int _addr = addr + X;
        if (_addr > 0xFF) {
            _addr -= 0x100;
        }
        A = ram.read(_addr);
        setZeroFlag(A);
        setNegativeFlag(A);
    }

    private void lda_indirect_y(int addr) {
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

        //System.out.println("LDX #$" + Integer.toHexString(value));
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

        //System.out.println("LDY #$" + Integer.toHexString(value));
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

    // -- SEI --

    private void sei() {
        status.setInterruptFlagSet(true);
    }

    // -- STA --

    private void sta_zeropage(int addr) {
        ram.write(addr, A);

        //System.out.println("STA $" + Integer.toHexString(addr));
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

        //System.out.println("STX $" + Integer.toHexString(addr));
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

        //System.out.println("STY $" + Integer.toHexString(addr));
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

    // -- JMP --

    private void jmp_absolute(int addr) {
        PC = addr; //TODO see nesdev reference for correct implementation
    }

    private void jmp_indirect(int addr) {
        PC = addr; //TODO see nesdev reference for correct implementation
    }

    // -- INX --

    private void inx() {
        X++;
        setZeroFlag(X);
        setNegativeFlag(X);
    }

    // -- TAX, TAY, TSX, TXA, TYA, TXS

    private void tax() {
        X = A;
        setZeroFlag(X);
        setNegativeFlag(X);
    }

    private void tay() {
        Y = A;
        setZeroFlag(Y);
        setNegativeFlag(Y);
    }

    private void tsx() {
        X = pop();
        setZeroFlag(X);
        setNegativeFlag(X);
    }

    private void txa() {
        A = X;
        setZeroFlag(A);
        setNegativeFlag(A);
    }

    private void tya() {
        A = Y;
        setZeroFlag(A);
        setNegativeFlag(A);
    }

    private void txs() {
        push(X);
    }


    // -- Status flags --

    private void setCarryFlag(int value) {
        status.setCarryFlagSet(value > 0xFF);
    }

    private void setZeroFlag(int value) {
        //zeroFlag = value == 0 ? 1 : 0;
        status.setZeroFlagSet(value == 0);
    }

    private void setNegativeFlag(int value) {
        String binary = String.format("%8s", Integer.toBinaryString(value)).replace(' ', '0'); //8-bit binary
        status.setNegativeFlagSet(binary.charAt(0) == '1');
        //negativeFlag = binary.charAt(0) == '1' ? 1 : 0;
    }

    private void setOverflowFlag(int value, int result) {
        boolean overflow = (((A ^ value) & 0x80) == 0)
                && (((A ^ result) & 0x80) != 0);

        status.setOverflowFlagSet(overflow);
    }

    // -- Branch --

    private void branch(int value) {
        //In halfnes project, it's implemented that way. I don't know if this fits this project
        PC = ((byte) ram.read(PC++)) + PC;
    }

    // -- Stack operations --
    private void push(int value) {
        ram.write((0x0100 + stackPointer), value);
        stackPointer = (stackPointer - 1) & 0xFF;
    }

    private int pop() {
        stackPointer = (stackPointer + 1) & 0xFF;
        return ram.read((0x0100 + stackPointer));
    }

    // -- Getters and setters --

    public int read_a() {
        return A;
    }

    public int read_x() {
        return X;
    }

    public int read_y() {
        return Y;
    }

    public int read_pc() {
        return PC;
    }

    public void set_pc(int value) {
        PC = value;
    }

    public long get_cycles() {
        return cycles;
    }

    public CpuRAM getRam() {
        return ram;
    }
}

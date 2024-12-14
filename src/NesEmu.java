import java.io.File;
import java.nio.file.Path;

/*
    Created 2024-11-14
 */
public class NesEmu {
    private static byte[] rom;
    //private static int nextByte = 0; //index of next byte to read in ROM
    private static CPU cpu;

    public static void main(String[] args) {
        //TODO
        // --> Add tests
        // --> Add frontend (Java Swing)

        //test_cpu();

        //File romFile = Path.of("test.nes").toFile();
        //File romFile = Path.of("test2.nes").toFile();
        //File romFile = Path.of("test3.nes").toFile();
        //File romFile = Path.of("playground.nes").toFile();
        File romFile = Path.of("smb1.nes").toFile();
        parseRom(romFile);
    }

    private static void test_cpu() {
        System.out.println("Testing CPU...");

        CPUTest cpuTest = new CPUTest();

        //LDA
        cpuTest.test_lda_immediate();
        cpuTest.test_lda_zeropage();
        cpuTest.test_lda_zeropage_x();
        cpuTest.test_lda_zeropage_x_wraparound();
        cpuTest.test_lda_absolute();
        cpuTest.test_lda_absolute_x();
        cpuTest.test_lda_absolute_y();
        cpuTest.test_lda_indirect_x();
        cpuTest.test_lda_indirect_x_wraparound();
        cpuTest.test_lda_indirect_y();

        //LDX
        cpuTest.test_ldx_immediate();
        cpuTest.test_ldx_zeropage();
        cpuTest.test_ldx_zeropage_y();
        cpuTest.test_ldx_zeropage_y_wraparound();
        cpuTest.test_ldx_absolute();
        cpuTest.test_ldx_absolute_y();

        //LDY
        cpuTest.test_ldy_immediate();
        cpuTest.test_ldy_zeropage();
        cpuTest.test_ldy_zeropage_x();
        cpuTest.test_ldy_zeropage_x_wraparound();
        cpuTest.test_ldy_absolute();
        cpuTest.test_ldy_absolute_x();


        cpuTest.printTestResults();
    }

    private static void parseRom(File romFile) {
        if (romFile == null) {
            return;
        }

        rom = RomLoader.extractBytesFromFile(romFile);
        //RomLoader.parseRom(rom);

        if (RomLoader.checkSignature(rom)) {
            Header header = RomLoader.parseHeader(rom);

            cpu = new CPU(header.getPrgSize(), header.getChrSize(), rom);
            int pc = cpu.getPC();
            while ((rom[pc] & 0xFF) != 0x00) {
                pc = cpu.getPC();
                System.out.println();
                System.out.println("PC: " + pc + " ($" + Integer.toHexString(pc) + ")");
                System.out.println("Reading opcode: " + Integer.toHexString(rom[pc] & 0xFF));
                int opcode = rom[pc] & 0xFF;

                switch (opcode) {
                    case 0x08, 0x0A, 0x28, 0x2A, 0x38, 0x40, 0x48, 0x4A, 0x58, 0x60,
                         0x68, 0x6A, 0x78, 0x88, 0x98, 0x9A, 0xAA, 0xA8, 0xB8, 0xBA,
                         0xC8, 0xCA, 0xD8, 0xE8, 0xEA, 0xF8:
                        //These opcodes are 1 byte wide
                        cpu.executeInstruction(opcode, 0);
                        break;
                    case 0x00, 0x01, 0x05, 0x06, 0x09, 0x10, 0x11, 0x15, 0x16, 0x18,
                         0x21, 0x24, 0x25, 0x26, 0x29, 0x30, 0x31, 0x35, 0x36, 0x41,
                         0x45, 0x46, 0x49, 0x50, 0x51, 0x55, 0x56, 0x61, 0x65, 0x66,
                         0x69, 0x76, 0x70, 0x71, 0x75, 0x81, 0x84, 0x85, 0x86, 0x90,
                         0x91, 0x94, 0x95, 0x96, 0xA0, 0xA2, 0xA4, 0xA9, 0xB0, 0xB4,
                         0xC0, 0xC1, 0xC4, 0xC5, 0xC6, 0xC9, 0xD0, 0xD1, 0xD5, 0xD6,
                         0xE0, 0xE1, 0xE4, 0xE5, 0xE6, 0xE9, 0xF0, 0xF1, 0xF5, 0xF6:
                        //These opcodes are 2 bytes wide
                        cpu.executeInstruction(opcode, rom[pc + 1]);
                        break;
                    case 0x0D, 0x0E, 0x19, 0x1D, 0x1E, 0x20, 0x2C, 0x2D, 0x2E, 0x39,
                         0x3D, 0x3E, 0x4C, 0x4E, 0x59, 0x5D, 0x5E, 0x6C, 0x6D, 0x6E,
                         0x79, 0x7D, 0x7E, 0x8C, 0x8D, 0x8E, 0x99, 0x9D, 0xAC, 0xAD,
                         0xAE, 0xBC, 0xBE, 0xCC, 0xCD, 0xCE, 0xD9, 0xDD, 0xDE, 0xEC,
                         0xED, 0xEE, 0xF9, 0xFD, 0xFE:
                        //These opcodes are 3 bytes wide
                        int low = rom[pc + 1];
                        int hi = rom[pc + 2];
                        int addr = (hi << 8) | low;
                        cpu.executeInstruction(opcode, addr);
                        break;
                    default:
                        System.out.println("opcode not implemented or invalid");
                        System.exit(69);
                }
            }
        }
    }
}
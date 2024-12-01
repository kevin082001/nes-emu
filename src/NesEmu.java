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
            int pc = cpu.read_pc();
            while ((rom[pc] & 0xFF) != 0x00) {
                pc = cpu.read_pc();
                System.out.println();
                System.out.println("PC: " + pc + " ($" + Integer.toHexString(pc) + ")");
                System.out.println("Reading opcode: " + Integer.toHexString(rom[pc] & 0xFF));
                int opcode = rom[pc] & 0xFF;

                switch (opcode) {
                    case 0x0A:
                    case 0x58:
                    case 0x78:
                    case 0x88:
                    case 0x8A:
                    case 0x98:
                    case 0x9A:
                    case 0xAA:
                    case 0xA8:
                    case 0xB8:
                    case 0xBA:
                    case 0xC8:
                    case 0xCA:
                    case 0xD8:
                    case 0xE8:
                        //These instructions are 1 byte wide
                        cpu.executeInstruction(opcode, 0);
                        break;
                    case 0x00:
                    case 0x06:
                    case 0x10:
                    case 0x16:
                    case 0x18:
                    case 0x21:
                    case 0x24:
                    case 0x25:
                    case 0x29:
                    case 0x30:
                    case 0x31:
                    case 0x35:
                    case 0x50:
                    case 0x61:
                    case 0x65:
                    case 0x69:
                    case 0x70:
                    case 0x71:
                    case 0x75:
                    case 0x84:
                    case 0x85:
                    case 0x86:
                    case 0x90:
                    case 0xA0:
                    case 0xA2:
                    case 0xA9:
                    case 0xB0:
                    case 0xC0:
                    case 0xC1:
                    case 0xC4:
                    case 0xC5:
                    case 0xC6:
                    case 0xC9:
                    case 0xD0:
                    case 0xD1:
                    case 0xD5:
                    case 0xD6:
                    case 0xE0:
                    case 0xE4:
                    case 0xF0:
                        //These instructions are 2 bytes wide
                        cpu.executeInstruction(opcode, rom[pc + 1]);
                        break;
                    case 0x0E:
                    case 0x1E:
                    case 0x2C:
                    case 0x2D:
                    case 0x39:
                    case 0x3D:
                    case 0x4C:
                    case 0x6C:
                    case 0x6D:
                    case 0x79:
                    case 0x7D:
                    case 0x8D:
                    case 0xCC:
                    case 0xCD:
                    case 0xCE:
                    case 0xD9:
                    case 0xDD:
                    case 0xDE:
                    case 0xEC:
                        //These instructions are 3 bytes wide
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
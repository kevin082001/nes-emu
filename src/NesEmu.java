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

        test_cpu();

        //File romFile = Path.of("test.nes").toFile();
        //File romFile = Path.of("test2.nes").toFile();
        //File romFile = Path.of("test3.nes").toFile();
        //parseRom(romFile);
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

            cpu = new CPU(header.getPrgSize(), header.getChrSize());
            int pc = cpu.read_pc();
            while ((rom[pc] & 0xFF) != 0x00) {
                pc = cpu.read_pc();
                System.out.println(pc);
                int opcode = rom[pc] & 0xFF;

                switch (opcode) {
                    case 0xE8:
                        //These instructions are 1 byte wide
                        cpu.executeInstruction(opcode, 0);
                        break;
                    case 0x84:
                    case 0x85:
                    case 0x86:
                    case 0xA0:
                    case 0xA2:
                    case 0xA9:
                        //These instructions are 2 bytes wide
                        cpu.executeInstruction(opcode, rom[pc + 1]);
                        break;
                    case 0x4C:
                    case 0x6C:
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
import java.io.File;
import java.nio.file.Path;

/*
    Created 2024-11-14
 */
public class NesEmu {
    private static byte[] rom;
    private static int nextByte = 0; //index of next byte to read in ROM
    private static CPU cpu = new CPU();

    public static void main(String[] args) {
        //TODO
        // --> Add tests
        // --> Add frontend (Java Swing)

        //test_cpu();

        //File romFile = Path.of("test.nes").toFile();
        File romFile = Path.of("test2.nes").toFile();
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
        cpuTest.test_ldx_absolute();
        cpuTest.test_ldx_absolute_y();

        //LDY


        cpuTest.printTestResults();
    }

    private static void parseRom(File romFile) {
        if (romFile == null) {
            return;
        }

        rom = RomLoader.extractBytesFromFile(romFile);
        //RomLoader.parseRom(rom);

        if (RomLoader.checkSignature(rom)) {
            Header header = RomLoader.parseHeader(rom); //TODO do something with the header

            nextByte = 16;
            while ((rom[nextByte] & 0xFF) != 0x00) {
                int opcode = rom[nextByte] & 0xFF;

                switch (opcode) {
                    case 0x84:
                    case 0x85:
                    case 0x86:
                    case 0xA0:
                    case 0xA2:
                    case 0xA9:
                        cpu.executeInstruction(opcode, rom[nextByte + 1]);
                        nextByte += 2;
                        break;
                    default:
                        System.out.println("EOF / invalid opcode / opcode not implemented");
                }
            }
        }
    }
}
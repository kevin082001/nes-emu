import java.util.ArrayList;
import java.util.List;

public class CPUTest {
    private CPU cpu;
    private CpuRAM ram;

    private int testsRun = 0;
    private int testsPassed = 0;
    private int testsFailed = 0;
    List<String> errorMessages = new ArrayList<>();

    public CPUTest() {
        cpu = new CPU(16, 8, null);
        ram = cpu.getRam();
    }

    public void test_lda_immediate() {
        int pcBefore = cpu.getPC();
        int expected = 0x5F;
        cpu.executeInstruction(0xA9, expected);
        int a = cpu.getA();
        int pcAfter = cpu.getPC();

        assertEquals(a, expected, "lda_immediate");
        assertEquals(pcAfter, (pcBefore + 2), "lda_immediate_programcounter");
    }

    public void test_lda_zeropage() {
        int pcBefore = cpu.getPC();
        //Pre-write memory location
        ram.write(0xF3, 0x50);

        // LDA $00F3
        int addr = 0xF3;
        cpu.executeInstruction(0xA5, addr);
        int a = cpu.getA();
        int expected = ram.read(addr);
        int pcAfter = cpu.getPC();

        assertEquals(a, expected, "lda_zeropage");
        assertEquals(pcAfter, (pcBefore + 2), "lda_zeropage_programcounter");
    }

    public void test_lda_zeropage_x() {
        //Pre-write memory location
        ram.write(0xF3, 0xFF);

        //We have to load X first
        cpu.executeInstruction(0xA2, 0x13);

        //LDA $00B5,X ( E0 + 13 = F3 )
        int pcBefore = cpu.getPC();
        cpu.executeInstruction(0xB5, 0xE0);

        int a = cpu.getA();
        int expected = ram.read(0xF3);
        int pcAfter = cpu.getPC();

        assertEquals(a, expected, "lda_zeropage_x");
        assertEquals(pcAfter, (pcBefore + 2), "lda_zeropage_x_programcounter");
    }

    public void test_lda_zeropage_x_wraparound() {
        //Pre-write memory location
        ram.write(0x20, 0x69);

        //We have to load X first
        cpu.executeInstruction(0xA2, 0xF0);

        //LDA $00B5,X ( $30 + $F0 = $0120 )
        cpu.executeInstruction(0xB5, 0x30);

        int a = cpu.getA();
        int expected = ram.read(0x20);

        assertEquals(a, expected, "lda_zeropage_x_wraparound");
    }

    public void test_lda_absolute() {
        //Pre-write memory location
        ram.write(0xACDC, 0xA7);

        //LDA $ACDC
        cpu.executeInstruction(0xAD, 0xACDC);

        int a = cpu.getA();
        int expected = ram.read(0xACDC);

        assertEquals(a, expected, "lda_absolute");
    }

    public void test_lda_absolute_x() {
        //Pre-write memory location
        ram.write(0xADAC, 0xA6);

        //We have to load X first
        cpu.executeInstruction(0xA2, 0x24);

        //LDA $AD88,X ($AD88 + $0024 = $ADAC)
        cpu.executeInstruction(0xBD, 0xAD88);

        int a = cpu.getA();
        int expected = ram.read(0xADAC);

        assertEquals(a, expected, "lda_absolute_x");
    }

    public void test_lda_absolute_y() {
        //Pre-write memory location
        ram.write(0xADAC, 0xA6);

        //We have to load Y first
        cpu.executeInstruction(0xA0, 0x25);

        //LDA $AD87, Y ($AD87 + $0025 = $ADAC)
        cpu.executeInstruction(0xB9, 0xAD87);

        int a = cpu.getA();
        int expected = ram.read(0xADAC);

        assertEquals(a, expected, "lda_absolute_y");
    }

    public void test_lda_indirect_x() {
        //Pre-write memory location
        ram.write(0xD2, 0xA6);

        //We have to load X first
        cpu.executeInstruction(0xA2, 0x25);

        //LDA ($AD, X) ($AD + $25 = $D2)
        cpu.executeInstruction(0xA1, 0xAD);

        int a = cpu.getA();
        int expected = ram.read(0xD2);

        assertEquals(a, expected, "lda_indirect_x");
    }

    public void test_lda_indirect_x_wraparound() {
        //Pre-write memory location
        ram.write(0x03, 0xA6);

        //We have to load X first
        cpu.executeInstruction(0xA2, 0x04);

        //LDA ($FF, X) ($FF + $04 = $03 with wraparound)
        cpu.executeInstruction(0xA1, 0xFF);

        int a = cpu.getA();
        int expected = ram.read(0x03);

        assertEquals(a, expected, "lda_indirect_x_wraparound");
    }

    public void test_lda_indirect_y() {
        //Pre-write memory location
        ram.write(0xA010, 0x55);

        //We have to load Y first
        cpu.executeInstruction(0xA0, 0x10);

        //LDA ($A000), Y ($A000 + $0010 = $A010)
        cpu.executeInstruction(0xB1, 0xA000);

        int a = cpu.getA();
        int expected = ram.read(0xA010);

        assertEquals(a, expected, "lda_indirect_y");
    }

    // -- LDX --

    public void test_ldx_immediate() {
        // LDX #$B3
        int expected = 0xB3;
        cpu.executeInstruction(0xA2, expected);
        int x = cpu.getX();

        assertEquals(x, expected, "ldx_immediate");
    }

    public void test_ldx_zeropage() {
        //Pre-write RAM
        ram.write(0xF3, 0x55);

        cpu.executeInstruction(0xA6, 0xF3);

        int x = cpu.getX();
        int expected = ram.read(0xF3);

        assertEquals(x, expected, "ldx_zeropage");
    }

    public void test_ldx_zeropage_y() {
        //Pre-write RAM
        ram.write(0x70, 0x55);

        //Load Y first
        cpu.executeInstruction(0xA0, 0x10);

        ram.write(0x70, 0x66);

        cpu.executeInstruction(0xB6, 0x60);

        int x = cpu.getX();
        int expected = ram.read(0x70);

        assertEquals(x, expected, "ldx_zeropage_y");
    }

    public void test_ldx_zeropage_y_wraparound() {
        //Pre-write RAM
        ram.write(0x10, 0x55);

        //Load Y first
        cpu.executeInstruction(0xA0, 0x80);

        ram.write(0x10, 0x66);

        cpu.executeInstruction(0xB6, 0x90);

        int x = cpu.getX();
        int expected = ram.read(0x10);

        assertEquals(x, expected, "ldx_zeropage_y_wraparound");
    }

    public void test_ldx_absolute() {
        //Pre-write RAM
        ram.write(0x8080, 0x77);

        cpu.executeInstruction(0xAE, 0x8080);

        int x = cpu.getX();
        int expected = ram.read(0x8080);

        assertEquals(x, expected, "ldx_absolute");
    }

    public void test_ldx_absolute_y() {
        //Pre-write RAM
        ram.write(0x8099, 0x77);

        //Load Y first
        cpu.executeInstruction(0xA0, 0x19);

        cpu.executeInstruction(0xAE, 0x8080);

        int x = cpu.getX();
        int expected = ram.read(0x8099);

        assertEquals(x, expected, "ldx_absolute_y");
    }

    // -- LDY --

    public void test_ldy_immediate() {
        int expected = 0x6F;
        cpu.executeInstruction(0xA0, expected);
        int y = cpu.getY();
        assertEquals(y, expected, "ldy_immediate");
    }

    public void test_ldy_zeropage() {

    }

    public void test_ldy_zeropage_x() {

    }

    public void test_ldy_zeropage_x_wraparound() {

    }

    public void test_ldy_absolute() {

    }

    public void test_ldy_absolute_x() {

    }


    public void printTestResults() {
        System.out.println(testsRun + " tests run (" + testsPassed + " passed / " + testsFailed + " failed)");

        if (!errorMessages.isEmpty()) {
            System.out.println("------------------");
            System.out.println("---   ERRORS   ---");
            System.out.println("------------------");
            System.out.println();
            for (String error : errorMessages) {
                System.out.println(error);
            }
        }
    }

    private void assertEquals(int actual, int expected, String instruction) {
        testsRun++;
        if (actual == expected) {
            testsPassed++;
        } else {
            testsFailed++;
            formatErrorMsg(instruction, Integer.toHexString(expected), Integer.toHexString(actual));
        }
    }

    private void formatErrorMsg(String instruction, String expected, String actual) {
        errorMessages.add("FAIL: " + instruction + " (Value should be 0x" + expected + " but was 0x" + actual + ")");
    }
}
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
        cpu = new CPU();
        ram = cpu.getRam();
    }

    public void test_lda_immediate() {
        // LDA #$5F
        int expected = 0x5F;
        cpu.executeInstruction(0xA9, expected);
        int a = cpu.read_a();

        testsRun++;
        if (a == expected) {
            testsPassed++;
        } else {
            testsFailed++;
            formatErrorMsg("lda_immediate", Integer.toHexString(expected), Integer.toHexString(a));
        }
    }

    public void test_lda_zeropage() {
        //Pre-write memory location
        ram.write(0xF3, 0x50);

        // LDA $00F3
        int addr = 0xF3;
        cpu.executeInstruction(0xA5, addr);
        int a = cpu.read_a();
        int expected = ram.read(addr);

        testsRun++;
        if (a == expected) {
            testsPassed++;
        } else {
            testsFailed++;
            formatErrorMsg("lda_zeropage", Integer.toHexString(expected), Integer.toHexString(a));
        }
    }

    public void test_lda_zeropage_x() {
        //Pre-write memory location
        ram.write(0xF3, 0xFF);

        //We have to load X first
        cpu.executeInstruction(0xA2, 0x13);

        //LDA $00B5,X ( E0 + 13 = F3 )
        cpu.executeInstruction(0xB5, 0xE0);

        int a = cpu.read_a();
        int expected = ram.read(0xF3);

        testsRun++;
        if (a == expected) {
            testsPassed++;
        } else {
            testsFailed++;
            formatErrorMsg("lda_zeropage_x", Integer.toHexString(expected), Integer.toHexString(a));
        }
    }

    public void test_lda_zeropage_x_wraparound() {
        //Pre-write memory location
        ram.write(0x20, 0x69);

        //We have to load X first
        cpu.executeInstruction(0xA2, 0xF0);

        //LDA $00B5,X ( $30 + $F0 = $0120 )
        cpu.executeInstruction(0xB5, 0x30);

        int a = cpu.read_a();
        int expected = ram.read(0x20);

        testsRun++;
        if (a == expected) {
            testsPassed++;
        } else {
            testsFailed++;
            formatErrorMsg("lda_zeropage_x_wraparound", Integer.toHexString(expected), Integer.toHexString(a));
        }
    }

    public void test_lda_absolute() {
        //Pre-write memory location
        ram.write(0xACDC, 0xA7);

        //LDA $ACDC
        cpu.executeInstruction(0xAD, 0xACDC);

        int a = cpu.read_a();
        int expected = ram.read(0xACDC);

        testsRun++;
        if (a == expected) {
            testsPassed++;
        } else {
            testsFailed++;
            formatErrorMsg("lda_absolute", Integer.toHexString(expected), Integer.toHexString(a));
        }
    }

    //TODO test rest of LDA

    public void test_ldx_immediate() {
        // LDX #$B3
        int expected = 0xB3;
        cpu.executeInstruction(0xA2, expected);
        int x = cpu.read_x();

        testsRun++;
        if (x == expected) {
            testsPassed++;
        } else {
            testsFailed++;
            formatErrorMsg("ldx_immediate", Integer.toHexString(expected), Integer.toHexString(x));
        }
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

    private void formatErrorMsg(String instruction, String expected, String actual) {
        errorMessages.add("FAIL: " + instruction + " (Value should be 0x" + expected + " but was 0x" + actual + ")");
    }
}
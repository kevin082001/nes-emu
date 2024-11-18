import java.util.ArrayList;
import java.util.List;

/*
    Created 2024-11-14
 */
public class NesEmu {
    public static void main(String[] args) {
        //TODO
        // --> Add tests
        // --> Add frontend (Java Swing)

        test_cpu();
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
}
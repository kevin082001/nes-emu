package cpu;

/*
    Created 2024-11-13
 */
public class CPU {
    //Memory map (see https://www.nesdev.org/wiki/CPU_memory_map for info)
    private CpuRAM ram;
    private int A, X, Y, S; //registers
    private int PC; //program counter
    private boolean zeroFlag, negativeFlag;
}

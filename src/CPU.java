/*
    Created 2024-11-13
 */
public class CPU {
    //Memory map (see https://www.nesdev.org/wiki/CPU_memory_map for info)
    private CpuRAM ram;
    private int A, X, Y, S; //registers
    private int PC; //program counter

    //See: https://www.nesdev.org/wiki/Status_flags for info
    private Flag[] flags = initFlags();
    /*private boolean[] flags = new boolean[8]; // Arragement: NV1BDIZC
    private boolean carryFlag, zeroFlag, overflowFlag, negativeFlag, interruptDisableFlag, decimalFlag;*/


    public int getA() {
        return A;
    }

    public int getX() {
        return X;
    }

    public int getY() {
        return Y;
    }

    public int getS() {
        return S;
    }

    public int getPC() {
        return PC;
    }

    private Flag[] initFlags() {
        Flag[] flags = new Flag[8];
        for (Flag f : Flag.values()) {
            flags[f.getBit()] = f;
        }

        return flags;
    }
}

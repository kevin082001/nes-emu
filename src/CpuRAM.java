/*
    Created 2024-11-14
 */
public class CpuRAM {
    //See: https://www.nesdev.org/wiki/CPU_memory_map

    private int[] internal = new int[2048]; //The first 2KB ($0000 - $07FF)
    private int[] internal_mirror1 = new int[2048]; // $0800 - $0FFF
    private int[] internal_mirror2 = new int[2048]; // $1000 - $17FF
    private int[] internal_mirror3 = new int[2048]; // $1800 - $1FFF

    private PPU ppu; // $2000 - $2007 , $2008 - §3FFF (mirrors, repeated every 8 bytes)
    private APU apu; // $4000 - $4017 , $4018 - $401F

    //Cartridge RAM / ROM and mapper registers
    private int[] cartridgeSpace = new int[49120]; // $4020 - $FFFF


    // Write / Read memory
    public void write(int addr, int value) {
        if (addr >= 0x0000 && addr <= 0x07FF) {
            internal[addr] = value;
        } else if (addr >= 0x0800 && addr <= 0x0FFF) {
            int _addr = addr - 0x0800;
            internal_mirror1[_addr] = value;
        } else if (addr >= 0x1000 && addr <= 0x17FF) {
            int _addr = addr - 0x1000;
            internal_mirror2[_addr] = value;
        } else if (addr >= 0x1800 && addr <= 0x1FFF) {
            int _addr = addr - 0x1800;
            internal_mirror3[_addr] = value;
        } else if (addr >= 0x4020 && addr <= 0xFFFF) {
            int _addr = addr - 0x4020;
            cartridgeSpace[_addr] = value;
        }
    }

    public int read(int addr) {
        if (addr >= 0x0000 && addr <= 0x07FF) {
            return internal[addr];
        } else if (addr >= 0x0800 && addr <= 0x0FFF) {
            int _addr = addr - 0x0800;
            return internal_mirror1[_addr];
        } else if (addr >= 0x1000 && addr <= 0x17FF) {
            int _addr = addr - 0x1000;
            return internal_mirror2[_addr];
        } else if (addr >= 0x1800 && addr <= 0x1FFF) {
            int _addr = addr - 0x1800;
            return internal_mirror3[_addr];
        } else if (addr >= 0x4020 && addr <= 0xFFFF) {
            int _addr = addr - 0x4020;
            return cartridgeSpace[_addr];
        }

        return 0;
    }
}

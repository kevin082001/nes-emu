/*
    Created 2024-11-14
 */
public class CpuRAM {
    //See: https://www.nesdev.org/wiki/CPU_memory_map

    private int[] internal = new int[2048]; //The first 2KB ($0000 - $07FF)
    private int[] interal_mirror1 = new int [2048]; // $0800 - $0FFF
    private int[] interal_mirror2 = new int [2048]; // $1000 - $17FF
    private int[] interal_mirror3 = new int [2048]; // $1800 - $1FFF

    //private int[] ppu_registers = new int[8]; // $2000 - $2007
    private PPU ppu; // $2000 - $2007 , $2008 - §3FFF (mirrors, repeated every 8 bytes)
    private APU apu; // $4000 - $4017 , $4018 - $401F

    private int[] cartridgeRAM = new int[49120]; // $4020 - $FFFF
}

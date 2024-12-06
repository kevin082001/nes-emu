/*
    Created 2024-12-06
 */
public class Stack {
    private int sp;
    private CpuRAM ram;

    public Stack(CpuRAM ram) {
        this.sp = 0xFF;
        this.ram = ram;
    }

    public void push(int value) {
        ram.write((0x0100 + sp), value);
        sp = (sp - 1) & 0xFF;
    }

    public int pop() {
        sp = (sp + 1) & 0xFF;
        return ram.read((0x0100 + sp));
    }
}
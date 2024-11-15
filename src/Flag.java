public enum Flag {
    CARRY(0, false),
    ZERO(1, false),
    INTERRUPT_DISABLE(2, false),
    DECIMAL(3, false),
    B(4, false),
    ONE(5, false),
    OVERFLOW(6, false),
    NEGATIVE(7, false);


    private final int bit;
    private boolean active;

    Flag(int bit, boolean active) {
        this.bit = bit;
        this.active = active;
    }

    public int getBit() {
        return bit;
    }

    public Flag getByBit(int bit) {
        if (bit < 0 || bit > 7) {
            return null;
        }

        for (Flag f : values()) {
            if (f.getBit() == bit) {
                return f;
            }
        }

        return null;
    }

    public boolean isActive() {
        return active;
    }
}

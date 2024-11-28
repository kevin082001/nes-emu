/*
    Created 2024-11-26
 */
public class StatusRegister {
    private boolean carryFlagSet;
    private boolean zeroFlagSet;
    private boolean interruptFlagSet;
    private boolean decimalFlagSet;
    private boolean breakFlagSet;
    private boolean overflowFlagSet;
    private boolean negativeFlagSet;

    public StatusRegister() {
        this.carryFlagSet = false;
        this.zeroFlagSet = false;
        this.interruptFlagSet = false; //i think this should be true
        this.decimalFlagSet = false;
        this.breakFlagSet = false;
        this.overflowFlagSet = false;
        this.negativeFlagSet = false;
    }

    public boolean isCarryFlagSet() {
        return carryFlagSet;
    }

    public void setCarryFlagSet(boolean carryFlagSet) {
        this.carryFlagSet = carryFlagSet;
    }

    public boolean isZeroFlagSet() {
        return zeroFlagSet;
    }

    public void setZeroFlagSet(boolean zeroFlagSet) {
        this.zeroFlagSet = zeroFlagSet;
    }

    public boolean isInterruptFlagSet() {
        return interruptFlagSet;
    }

    public void setInterruptFlagSet(boolean interruptFlagSet) {
        this.interruptFlagSet = interruptFlagSet;
    }

    public boolean isDecimalFlagSet() {
        return decimalFlagSet;
    }

    public void setDecimalFlagSet(boolean decimalFlagSet) {
        this.decimalFlagSet = decimalFlagSet;
    }

    public boolean isBreakFlagSet() {
        return breakFlagSet;
    }

    public void setBreakFlagSet(boolean breakFlagSet) {
        this.breakFlagSet = breakFlagSet;
    }

    public boolean isOverflowFlagSet() {
        return overflowFlagSet;
    }

    public void setOverflowFlagSet(boolean overflowFlagSet) {
        this.overflowFlagSet = overflowFlagSet;
    }

    public boolean isNegativeFlagSet() {
        return negativeFlagSet;
    }

    public void setNegativeFlagSet(boolean negativeFlagSet) {
        this.negativeFlagSet = negativeFlagSet;
    }
}

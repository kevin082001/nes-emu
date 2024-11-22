public class Header {
    private int prg; //number of KB used for PRG data
    private int chr; //number of KB used for CHR data
    private int other; //TODO split up correctly (look into documentation)

    public Header(int prg, int chr) {
        this.prg = prg;
        this.chr = chr;
    }

    public int getPrg() {
        return prg;
    }

    public int getChr() {
        return chr;
    }
}

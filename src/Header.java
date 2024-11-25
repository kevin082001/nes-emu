public class Header {
    // --------- HEADER ---------
    //byte 4:      PRG ROM (16 KB units)
    //byte 5:      CHR ROM ( 8 KB units)
    //byte 6:      Mapper, mirroring, battery, trainer
    //byte 7:      Mapper, VS/Playchoice, NES 2.0
    //byte 8:      PRG-RAM (rarely used)
    //byte 9:      TV system (rarely used)
    //byte 10:     TV system, PRG-RAM presence (unofficial, rarely used)
    //bytes 11-15: Unused padding (should be filled with zero, but some rippers put their name across bytes 7 - 15)

    private final int prgSize; //number of KB used for PRG data
    private final int chrSize; //number of KB used for CHR data
    private int other; //TODO add the other parts (see NesDev wiki)

    public Header(int prgSize, int chrSize) {
        this.prgSize = prgSize;
        this.chrSize = chrSize;
    }

    public int getPrgSize() {
        return prgSize;
    }

    public int getChrSize() {
        return chrSize;
    }
}

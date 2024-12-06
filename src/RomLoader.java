import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/*
    Created 2024-11-22
 */
public class RomLoader {
    private static byte[] rom;
    private static int nextByte = 0; //index in the byte[]

    public static byte[] parseRom(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        //rom = bytes;
        return bytes;

    }

    public static byte[] extractBytesFromFile(File file) {
        if (file == null) {
            return null;
        }

        try {
            FileInputStream fis = new FileInputStream(file);
            return fis.readAllBytes();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Checks the first 4 bytes if they match the NES signature ("NES\x1A")
     *
     * @param bytes The ROM's bytes
     * @return Whether the first 4 bytes match the signature
     */
    public static boolean checkSignature(byte[] bytes) {
        return bytes[0] == 0x4E && bytes[1] == 0x45 && bytes[2] == 0x53 && bytes[3] == 0x1A;
    }

    public static Header parseHeader(byte[] bytes) {
        // --------- HEADER ---------
        //byte 4:      PRG ROM (16 KB units)
        //byte 5:      CHR ROM ( 8 KB units)
        //byte 6:      Mapper, mirroring, battery, trainer
        //byte 7:      Mapper, VS/Playchoice, NES 2.0
        //byte 8:      PRG-RAM (rarely used)
        //byte 9:      TV system (rarely used)
        //byte 10:     TV system, PRG-RAM presence (unofficial, rarely used)
        //bytes 11-15: Unused padding (should be filled with zero, but some rippers put their name across bytes 7 - 15)

        if (bytes == null) {
            return null;
        }

        int prgRom = bytes[4] * 16 * 1024; //16 * 1024 bytes = 16 KB
        int chrRom = bytes[5] * 8 * 1024; // 8 * 1024 bytes = 8 KB

        return new Header(prgRom, chrRom);
    }
}


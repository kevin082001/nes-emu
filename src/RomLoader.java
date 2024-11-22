import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

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
        //TODO implement
        return new Header(16, 8);
    }
}


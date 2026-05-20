package indexer;

import java.security.MessageDigest;
import java.io.*;

public class ChecksumCalculator {
    public String calculate(File file) {
        try {
            // java built-in cryptography class
            // load the SHA-256 hashing algorithm
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            try (InputStream is = new FileInputStream(file)) {
                byte[] buffer = new byte[8192]; // read 8192 byte chunks at a time
                int read;
                while ((read = is.read(buffer)) != -1) {
                    digest.update(buffer, 0, read); // feed the chunk to digest
                }
            }
            byte[] hash = digest.digest(); // returns the SHA-256 hash as a 32-byte array
            StringBuilder hex = new StringBuilder();
            for (byte b : hash) {
                hex.append(String.format("%02x", b)); // convert the hash to a hex string
            }
            return hex.toString();
        } catch (Exception e) {
            System.err.println("Could not compute checksum: " + file.getAbsolutePath());
            return null;
        }
    }
}
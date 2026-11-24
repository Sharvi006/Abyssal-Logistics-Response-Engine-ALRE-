package member4;

public class QuarantineCipher {
    private static final int SHIFT_KEY = 3;

    private QuarantineCipher() {
    }

    public static String scrambleId(String victimId) {
        if (victimId == null) {
            return "";
        }

        StringBuilder shifted = new StringBuilder(victimId.length());
        for (int i = 0; i < victimId.length(); i++) {
            char ch = victimId.charAt(i);
            shifted.append((char) (ch + SHIFT_KEY));
        }

        return shifted.reverse().toString();
    }

    public static String decryptId(String scrambledId) {
        if (scrambledId == null) {
            return "";
        }

        StringBuilder unreversed = new StringBuilder(scrambledId).reverse();
        StringBuilder decrypted = new StringBuilder(unreversed.length());

        for (int i = 0; i < unreversed.length(); i++) {
            char ch = unreversed.charAt(i);
            decrypted.append((char) (ch - SHIFT_KEY));
        }

        return decrypted.toString();
    }
}
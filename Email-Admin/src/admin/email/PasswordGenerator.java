package admin.email;
import java.security.SecureRandom;

public class PasswordGenerator {
        private static final String UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        private static final String LOWER = "abcdefghijklmnopqrstuvwxyz";
        private static final String DIGITS = "0123456789";
        private static final String SPECIAL = "!@#$%^&*()-_=+<>?/";

        private static final String ALL = UPPER + LOWER + DIGITS + SPECIAL;

        private static final SecureRandom random = new SecureRandom();

        public static String generatePassword(int length) {
            StringBuilder sb = new StringBuilder(length);

            // Ensure at least 1 char from each category
            sb.append(UPPER.charAt(random.nextInt(UPPER.length())));
            sb.append(LOWER.charAt(random.nextInt(LOWER.length())));
            sb.append(DIGITS.charAt(random.nextInt(DIGITS.length())));
            sb.append(SPECIAL.charAt(random.nextInt(SPECIAL.length())));

            // Fill remaining with all characters
            for (int i = 4; i < length; i++) {
                sb.append(ALL.charAt(random.nextInt(ALL.length())));
            }

            // Shuffle the string (so first 4 aren’t predictable)
            return shuffleString(sb.toString());
        }

        private static String shuffleString(String input) {
            char[] chars = input.toCharArray();
            for (int i = 0; i < chars.length; i++) {
                int j = random.nextInt(chars.length);
                // swap
                char temp = chars[i];
                chars[i] = chars[j];
                chars[j] = temp;
            }
            return new String(chars);
        }

    }



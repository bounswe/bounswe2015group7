package sculture;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

public class Utils {
    public static String password_hash(String password) {
        try {
            MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
            String salt = "tOFHOGEeuE";
            String passWithSalt = password + salt;
            byte[] passBytes = passWithSalt.getBytes();
            byte[] passHash = sha256.digest(passBytes);
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < passHash.length; i++) {
                sb.append(Integer.toString((passHash[i] & 0xff) + 0x100, 16).substring(1));
            }
            String generatedPassword = sb.toString();
            return generatedPassword;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String access_token_generate() {
        return UUID.randomUUID().toString();
    }

    public static boolean checkEmailSyntax(String email) {
        //TODO improve this
        if (email == null || email.isEmpty())
            return false;
        return true;
    }

    public static boolean checkPasswordSyntax(String password) {
        //TODO improve this
        if (password == null || password.isEmpty())
            return false;
        return true;
    }

    public static boolean checkUsernameSyntax(String username) {
        //TODO improve this
        if (username == null || username.isEmpty())
            return false;
        return true;
    }
}

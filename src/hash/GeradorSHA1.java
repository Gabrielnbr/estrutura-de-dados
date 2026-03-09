package src.hash;
import java.security.MessageDigest;

public class GeradorSHA1 {

    public static String gerar (String texto) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] hashBytes = md.digest(texto.getBytes());
            StringBuilder hashString = new StringBuilder();
            for (byte b : hashBytes){
                String hex = String.format("%02x", b);
                hashString.append(hex);
            }
            return hashString.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

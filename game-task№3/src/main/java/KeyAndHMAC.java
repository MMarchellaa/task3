import com.sun.org.apache.xerces.internal.impl.dv.util.HexBin;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class KeyAndHMAC {
    byte[] generateKeyAndHMAC(String[] args, int rand) throws NoSuchAlgorithmException, InvalidKeyException {
        SecureRandom random = new SecureRandom();
        byte[] seed = random.generateSeed(16);

        SecretKeySpec key = new SecretKeySpec(seed, "HmacSHA256");
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(key);

        System.out.println("HMAC: " + HexBin.encode(mac.doFinal(args[rand].getBytes(StandardCharsets.UTF_8))));
        return seed;
    }
}

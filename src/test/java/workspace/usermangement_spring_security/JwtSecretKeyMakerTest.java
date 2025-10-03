package workspace.usermangement_spring_security;

import io.jsonwebtoken.Jwts;
import jakarta.xml.bind.DatatypeConverter;
import org.junit.jupiter.api.Test;

import javax.crypto.SecretKey;

public class JwtSecretKeyMakerTest {
    @Test
    public void generateSecretKey(){
        SecretKey secretKey = Jwts.SIG.HS512.key().build();
        String encodedSecretKey = DatatypeConverter.printHexBinary(secretKey.getEncoded());
        System.out.println(encodedSecretKey);
    }
}

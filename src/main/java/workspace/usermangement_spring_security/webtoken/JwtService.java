package workspace.usermangement_spring_security.webtoken;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service
public class JwtService {

     private static final String SECRET = "8E15C5D1E6EF1A52133AA234A8ACC6057704D0891DE7C60525683DEEEE038FBF3ED820BCF851599600BE1A194B88C00C1A8B574C9BBD3133911759A1B0FF414C";

     private static final long VALIDITY = TimeUnit.MINUTES.toMillis(30);

     public SecretKey generateKey(){
         byte[] decodedKey = Base64.getDecoder().decode(SECRET);
         return Keys.hmacShaKeyFor(decodedKey);
     }

     public String generateToken(UserDetails userDetails){
         return Jwts.builder().
                 subject(userDetails.getUsername()).
                 issuedAt(Date.from(Instant.now())).
                 expiration(Date.from(Instant.now().plusMillis(VALIDITY))).
                 signWith(generateKey()).
                 compact();
     }

    public String extractUserName(String jwt) {
         Claims claims = getClaims(jwt);
        return claims.getSubject();
    }

    public Claims getClaims(String jwt){
        return Jwts.parser().
                verifyWith(generateKey()).
                build().
                parseSignedClaims(jwt).
                getPayload();
    }

    public boolean isTokenValid(String jwt) {
         Claims claims = getClaims(jwt);
         return claims.getExpiration().after(Date.from(Instant.now()));
    }
}

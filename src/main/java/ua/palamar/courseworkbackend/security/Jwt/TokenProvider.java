package ua.palamar.courseworkbackend.security.Jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import ua.palamar.courseworkbackend.entity.UserEntity;
import ua.palamar.courseworkbackend.service.UserDetailsService;

import javax.servlet.http.HttpServletRequest;
import java.sql.Time;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Component
public class TokenProvider {

    @Value("${secret.key}")
    private String secretKey;

    private final UserDetailsService userDetailsServiceImpl;

    @Autowired
    public TokenProvider(UserDetailsService userDetailsServiceImpl) {
        this.userDetailsServiceImpl = userDetailsServiceImpl;
    }


    public boolean validateToken (String token) {

        return !Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .getExpiration()
                .before(new Date());

    }

    public String generateToken (UserEntity userEntity) {
        Date now = new Date();
        Date expired = new Date(now.getTime() + TimeUnit.MINUTES.toMillis(15));

        return Jwts.builder()
                .setSubject(userEntity.getEmail())
                .setIssuedAt(now)
                .setExpiration(expired)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public String generateRefreshToken(UserEntity user) {
        Date now = new Date();
        Date validPeriod = new Date(now.getTime() + TimeUnit.DAYS.toMillis(30));
        return Jwts.builder()
                .setSubject(user.getEmail())
                .setExpiration(validPeriod)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public String resolveToken (HttpServletRequest request) {
        String token = request.getHeader("Authorization");

        if (token != null && token.startsWith("Bearer_")) {
            return token.substring("Bearer_".length());
        }

        return null;
    }

    public Authentication authentication (String token) {
        String email = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
        UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(email);
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String getEmailByToken (String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}

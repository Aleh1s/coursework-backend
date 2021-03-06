package ua.palamar.courseworkbackend.security.Jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import ua.palamar.courseworkbackend.entity.user.UserAccount;
import ua.palamar.courseworkbackend.exception.ApiRequestException;
import ua.palamar.courseworkbackend.service.userDetails.UserDetailsServiceImpl;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Component
public class TokenProvider {

    @Value("${secret.key}")
    private String secretKey;

    private final UserDetailsServiceImpl userDetailsServiceImpl;

    @Autowired
    public TokenProvider(UserDetailsServiceImpl userDetailsServiceImpl) {
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

    public String generateToken (UserAccount userAccount) {
        Date now = new Date();
        Date expired = new Date(now.getTime() + TimeUnit.DAYS.toMillis(15));

        return Jwts.builder()
                .setSubject(userAccount.getEmail())
                .setIssuedAt(now)
                .setExpiration(expired)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public String generateRefreshToken(UserAccount user) {
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
        if (token != null && !token.equals("")) {
            return Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        }

        throw new ApiRequestException("Token can not be empty or null");
    }

    public String getEmail(HttpServletRequest request) {
        String token = resolveToken(request);
        return getEmailByToken(token);
    }
}

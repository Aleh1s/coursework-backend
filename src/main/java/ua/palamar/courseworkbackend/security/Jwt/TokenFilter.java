package ua.palamar.courseworkbackend.security.Jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class TokenFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;

    @Autowired
    public TokenFilter(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        boolean isNotServicePath =
                !request.getServletPath().equals("/api/v1/authentication/authenticate")
                && !request.getServletPath().equals("/api/v1/authentication/refresh")
                && !request.getServletPath().equals("/api/v1/registration/register");

        if (isNotServicePath) {
            String token = tokenProvider.resolveToken(request);

            if (token != null && tokenProvider.validateToken(token)) {
                Authentication authentication = tokenProvider.authentication(token);

                if (authentication != null) {
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }

            }

        }
        filterChain.doFilter(request, response);
    }
}

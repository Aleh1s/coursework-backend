package ua.palamar.courseworkbackend.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ua.palamar.courseworkbackend.security.Jwt.TokenFilter;

import static ua.palamar.courseworkbackend.entity.user.permissions.UserPermission.*;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final TokenFilter tokenFilter;

    @Autowired
    public SecurityConfig(TokenFilter tokenFilter) {
        this.tokenFilter = tokenFilter;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors().and()
                .csrf().disable()
                .httpBasic().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(
                        HttpMethod.GET,
                        "/api/v1/advertisements/email/**"
                ).hasAuthority(ADVERTISEMENT_READ.getPermission())
                .antMatchers(
                        HttpMethod.GET,
                        "/api/v1/orders/email/**",
                        "/api/v1/orders/advertisement/**"
                ).hasAuthority(ORDER_READ.getPermission())
                .antMatchers(
                        HttpMethod.POST,
                        "/api/v1/advertisements/**"
                ).hasAuthority(ADVERTISEMENT_CREATE.getPermission())
                .antMatchers(
                        HttpMethod.POST,
                        "/api/v1/users/**"
                ).hasAuthority(USER_UPDATE.getPermission())
                .antMatchers(
                        HttpMethod.POST,
                        "/api/v1/orders/**"
                ).hasAuthority(ORDER_MAKE.getPermission())
                .antMatchers(
                        HttpMethod.PUT,
                        "/api/v1/orders/decline/**",
                        "/api/v1/orders/accept/**",
                        "/api/v1/orders/cancel/**",
                        "/api/v1/orders/change-status/**"
                ).hasAuthority(ORDER_CHANGE.getPermission())
                .antMatchers(
                        HttpMethod.PUT,
                        "/api/v1/users/**"
                ).hasAuthority(USER_UPDATE.getPermission())
                .antMatchers(
                        HttpMethod.DELETE,
                        "/api/v1/advertisements/**"
                ).hasAuthority(ADVERTISEMENT_DELETE.getPermission())
                .antMatchers(
                        "/api/v1/registration/**",
                        "/api/v1/authentication/**",
                        "/api/v1/feedback/**"
                ).permitAll()
                .antMatchers(
                        HttpMethod.GET,
                        "/api/v1/advertisements/**",
                        "/api/v1/advertisements/page/**",
                        "/api/v1/advertisements/query/**",
                        "/api/v1/advertisements/image/**",
                        "/api/v1/users/image/**",
                        "/api/v1/users/image/check/**"
                ).permitAll()
                .anyRequest().authenticated()
                .and().addFilterBefore(tokenFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

package ua.palamar.courseworkbackend.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ua.palamar.courseworkbackend.entity.user.permissions.UserPermission;
import ua.palamar.courseworkbackend.entity.user.permissions.UserRole;
import ua.palamar.courseworkbackend.security.Jwt.TokenFilter;

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
                .csrf().disable() // todo: watch about spring csrf configuration
                .httpBasic().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests()
                .antMatchers(
                        "/api/v1/authentication/**",
                        "/api/v1/registration/**",
                        "/api/v1/advertisements/**",
                        "/api/v1/feedback/**"
                ).permitAll()
                .antMatchers(
                        "/api/v1/advertisements/page/**"
                ).hasRole(UserRole.USER.name())
                .anyRequest()
                .authenticated().and()
                .addFilterBefore(tokenFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

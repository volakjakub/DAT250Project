package tech.dat250project.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import tech.dat250project.security.CustomUserDetailsService;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new CustomUserDetailsService();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable().authorizeRequests().antMatchers("/login", "/register", "/register/device", "/vote/device",
                        "/swagger-ui.html", "/swagger-ui/index.html", "/api-docs", "/swagger-ui/swagger-initializer.js",
                        "/swagger-ui/swagger-ui-bundle.js", "/swagger-ui/swagger-ui-bundl", "/swagger-ui/index.css", "/swagger-ui/swagger-ui.css",
                        "/swagger-ui/swagger-ui-standalone-preset.js", "/api-docs/swagger-config", "/swagger-ui/swagger-ui-stand", "/swagger-ui/favicon-32x32.png","/swagger-ui/favicon-16x16.png").permitAll()
                .anyRequest().authenticated()
                .and().logout().permitAll();

        http.headers().frameOptions().sameOrigin();

        return http.build();
    }
}

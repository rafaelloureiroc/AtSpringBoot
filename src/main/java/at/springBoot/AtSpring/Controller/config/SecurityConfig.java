package at.springBoot.AtSpring.Controller.config;

import static org.springframework.security.config.Customizer.withDefaults;

import java.util.function.Function;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    @Bean
    public InMemoryUserDetailsManager createUserDetailsManager() {
        UserDetails userDetails1 = createNewUser();

        return new InMemoryUserDetailsManager(userDetails1);
    }

    private UserDetails createNewUser() {
        Function<String, String> passwordEncoder = input -> passwordEncoder().encode(input);

        return User.builder()
                .passwordEncoder(passwordEncoder)
                .username("Professor")
                .password("Infnet")
                .roles("USER", "ADMIN")
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .anyRequest().authenticated()
                )
                .httpBasic(withDefaults())
                .csrf().disable()
                .headers().frameOptions().disable();

        return http.build();
    }

}
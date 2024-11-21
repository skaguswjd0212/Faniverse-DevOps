package fantastic.faniverse.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // CSRF 비활성화 (테스트 목적)
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers("/auth/**").permitAll() // 인증 없이 접근 허용
                                .requestMatchers("/users/register").permitAll() // 회원가입 인증 없이 접근 허용
                                //.anyRequest().authenticated() // 나머지 요청은 인증 필요
                                .anyRequest().permitAll()
                )
                .formLogin(withDefaults()) // 폼 로그인 활성화
                .httpBasic(withDefaults()); // 기본 인증 활성화

        return http.build();
    }
}

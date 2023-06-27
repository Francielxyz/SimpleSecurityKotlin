package simple.security.kotlin.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain

@Configuration
class WebSecurityConfigV2 {

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain = http.httpBasic()
        .and()
        .authorizeHttpRequests()
        .anyRequest()
        .authenticated()
        .and()
        .csrf().disable()
        .build()

    @Bean
    fun passwordEncoder(): PasswordEncoder? = BCryptPasswordEncoder()
}
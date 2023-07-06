package simple.security.kotlin.config

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import lombok.RequiredArgsConstructor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.authentication.logout.LogoutHandler
import simple.security.kotlin.model.enums.Role

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
class SecurityConfiguration {

    @Autowired
    private lateinit var jwtAuthFilter: JwtAuthenticationFilter

    @Autowired
    private lateinit var authenticationProvider: AuthenticationProvider

    @Autowired
    private lateinit var logoutHandler: LogoutHandler

    @Bean
    @Throws(Exception::class)
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf()
            .disable()
            .authorizeHttpRequests()
            .requestMatchers(
                "/api/v1/auth/**",
                "/v2/api-docs",
                "/v3/api-docs",
                "/v3/api-docs/**",
                "/swagger-resources",
                "/swagger-resources/**",
                "/configuration/ui",
                "/configuration/security",
                "/swagger-ui/**",
                "/webjars/**",
                "/swagger-ui.html"
            )
            .permitAll()
            .requestMatchers("/api/v1/management/**").hasAnyRole(Role.ADMIN.opcao)
            .anyRequest()
            .authenticated()
            .and()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authenticationProvider(authenticationProvider)
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter::class.java)
            .logout()
            .logoutUrl("/api/v1/auth/logout")
            .addLogoutHandler(logoutHandler)
            .logoutSuccessHandler {
                _: HttpServletRequest?,
                _: HttpServletResponse?,
                _: Authentication? -> SecurityContextHolder.clearContext() }
        return http.build()
    }
}
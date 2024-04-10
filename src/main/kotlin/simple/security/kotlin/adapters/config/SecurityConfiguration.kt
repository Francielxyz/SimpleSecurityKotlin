package simple.security.kotlin.adapters.config

import com.fasterxml.jackson.databind.ObjectMapper
import lombok.RequiredArgsConstructor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import simple.security.kotlin.adapters.config.exception.ExceptionSecurityHandler

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
class SecurityConfiguration {

    @Autowired
    private lateinit var jwtAuthFilter: JwtAuthenticationFilter

    @Autowired
    private lateinit var authenticationProvider: AuthenticationProvider

    @Bean
    @Throws(Exception::class)
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { it.disable() }
            .authorizeHttpRequests {
                it.requestMatchers(HttpMethod.GET, "/authentication/v1/login").permitAll()
                it.requestMatchers(HttpMethod.GET, "/authentication/v1/refresh-token").permitAll()
            }
            .authorizeHttpRequests {
                it.requestMatchers(HttpMethod.POST, "/user/v1/register").permitAll()
                it.requestMatchers(HttpMethod.PUT, "/user/v1/update").authenticated()
            }
            .authorizeHttpRequests {
                it.requestMatchers(HttpMethod.GET, "/movie/v1").hasAnyAuthority("ADMIN", "USER")
                it.requestMatchers(HttpMethod.GET, "/movie/v1/movies").hasAnyAuthority("ADMIN", "USER")
                it.requestMatchers(HttpMethod.POST, "/movie/v1/save").hasAnyAuthority("ADMIN")
                it.requestMatchers(HttpMethod.PUT, "/movie/v1/update").hasAnyAuthority("ADMIN")
                it.requestMatchers(HttpMethod.DELETE, "/movie/v1/delete").hasAnyAuthority("ADMIN")
                    .anyRequest().authenticated()
            }
            .authenticationProvider(authenticationProvider)
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter::class.java)
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .exceptionHandling()
            .authenticationEntryPoint(authenticationEntryPoint())
        return http.build()
    }

    @Bean
    fun authenticationEntryPoint(): ExceptionSecurityHandler {
        return ExceptionSecurityHandler(ObjectMapper())
    }
}
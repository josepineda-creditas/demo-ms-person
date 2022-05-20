package mx.financial.person.configuration

import org.springframework.context.annotation.Bean
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.web.server.SecurityWebFilterChain

@EnableWebFluxSecurity
class SecurityConfiguration {

  @Bean
  fun springWebFilterChain(http: ServerHttpSecurity): SecurityWebFilterChain {
    return http
      .csrf()
      .disable()
      .cors()
      .and()
      .authorizeExchange()
      .anyExchange()
      .permitAll()
      .and()
      .build()
  }
}

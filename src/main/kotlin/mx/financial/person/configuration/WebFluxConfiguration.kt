package mx.financial.person.configuration

import org.springframework.context.annotation.Configuration
import org.springframework.http.codec.ServerCodecConfigurer
import org.springframework.web.reactive.config.WebFluxConfigurer

@Configuration
class WebFluxConfiguration : WebFluxConfigurer {

  companion object {
    const val MB = 1048576
    const val MAX_MB_COUNT = 10
  }

  override fun configureHttpMessageCodecs(configurer: ServerCodecConfigurer) {
    super.configureHttpMessageCodecs(configurer)
    configurer.defaultCodecs().maxInMemorySize(MAX_MB_COUNT * MB)
  }
}

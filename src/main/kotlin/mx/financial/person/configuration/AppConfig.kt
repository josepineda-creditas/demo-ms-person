package mx.financial.person.configuration

import mx.financial.person.handlers.ErrorCodeHandler
import org.springframework.context.MessageSource
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.support.MessageSourceAccessor
import org.springframework.context.support.ReloadableResourceBundleMessageSource
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean

@Configuration
class AppConfig {

  @Bean
  fun validator(): LocalValidatorFactoryBean? {
    val bean = LocalValidatorFactoryBean()
    bean.setValidationMessageSource(messageSource()!!)
    return bean
  }

  @Bean
  fun messageSource(): MessageSource? {
    val messageSource = ReloadableResourceBundleMessageSource()
    messageSource.setBasename("classpath:messages")
    messageSource.setDefaultEncoding("UTF-8")
    return messageSource
  }

  @Bean
  fun messageAccessor(messageSource: MessageSource) = MessageSourceAccessor(messageSource)

  @Bean
  fun codeErrorHandler(messageAccessor: MessageSourceAccessor) = ErrorCodeHandler(messageAccessor)
}

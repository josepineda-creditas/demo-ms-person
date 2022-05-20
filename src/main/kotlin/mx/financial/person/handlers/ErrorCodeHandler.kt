package mx.financial.person.handlers

import mx.financial.person.models.ErrorCode
import org.springframework.context.support.MessageSourceAccessor

class ErrorCodeHandler(val accessor: MessageSourceAccessor) {

  operator fun get(code: String): ErrorCode {
    return ErrorCode(code, accessor.getMessage(code))
  }
}

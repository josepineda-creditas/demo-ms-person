package mx.financial.person.controller

import mx.financial.person.support.exception.ApiError
import mx.financial.person.support.exception.CustomErrorCodes
import mx.financial.person.support.exception.ValidationException
import mx.financial.person.support.exception.PersonNotFoundException

import mx.financial.person.support.exception.PersonNotUpdatableException //
import mx.financial.person.support.exception.UpdateErrorsException //

import org.slf4j.Logger
import org.springframework.context.annotation.Primary
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.support.WebExchangeBindException
import org.springframework.web.server.ServerWebInputException
import retrofit2.HttpException

@ControllerAdvice(annotations = [RestController::class])
class ControllerAdviceException(
  private val log: Logger
) {

  companion object {
    @JvmField
    val ERROR_REGEX = "(\\d+) (.+)".toRegex()
  }

  fun extractErrors(errorMessage: String?): Map<String, Any> {
    if (errorMessage == null) return emptyMap()

    val (code, message) = ERROR_REGEX.find(errorMessage)!!.destructured
    return mapOf(
      "code" to Integer.parseInt(code),
      "message" to message
    )
  }

  @ExceptionHandler(value = [(WebExchangeBindException::class)])
  fun handler(ex: WebExchangeBindException): ResponseEntity<Any>? {
    val errors = mutableMapOf<String, Any?>()
    ex.bindingResult.fieldErrors.forEach { errors[it.field] = extractErrors(it.defaultMessage) }

    val result = ApiError(
      message = "Validation error",
      code = CustomErrorCodes.VALIDATION_ERROR,
      fields = errors
    )

    log.warn("$result")

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result)
  }

  @ExceptionHandler(value = [(HttpException::class)])
  fun handler(ex: HttpException): ResponseEntity<Any>? {
    val code: Int = if (ex.code() == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
      HttpStatus.FAILED_DEPENDENCY.value()
    } else ex.code()
    val result = ApiError(
      message = ex.message(),
      code = code,
      fields = ex.response()
    )

    log.warn("$result")

    return ResponseEntity.status(code).body(result)
  }

  @ExceptionHandler(value = [(ValidationException::class)])
  fun handler(ex: ValidationException): ResponseEntity<Any>? {
    val errors = mutableMapOf<String, Any?>()
    ex.errors.forEach { errors[it.propertyPath.toString()] = extractErrors(it.message) }

    val result = ApiError(
      message = "Validation error",
      code = CustomErrorCodes.VALIDATION_ERROR,
      fields = errors
    )

    log.warn("$result")

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result)
  }

  @ExceptionHandler(value = [(UpdateErrorsException::class)])
  fun handler(ex: UpdateErrorsException): ResponseEntity<Any>? {
    log.warn("UpdateErrorsException ${ex.errors}")

    val result = ApiError(
      message = "Update fields errors",
      code = CustomErrorCodes.UPDATE_FIELDS_ERROR,
      fields = ex.errors
    )

    log.warn("$result")

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result)
  }

  @ExceptionHandler(value = [(PersonNotFoundException::class)])
  fun handler(ex: PersonNotFoundException): ResponseEntity<Any>? {
    val result = ApiError(
      message = ex.message,
      code = CustomErrorCodes.NOT_FOUND
    )

    log.warn("$result")

    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result)
  }

  @ExceptionHandler(value = [(PersonNotUpdatableException::class)])
  fun handler(ex: PersonNotUpdatableException): ResponseEntity<Any>? {
    val result = ApiError(
      message = ex.message,
      code = CustomErrorCodes.NOT_UPDATABLE_LEAD
    )

    log.warn("$result")

    return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(result)
  }

  @Primary
  @ExceptionHandler(value = [(ServerWebInputException::class)])
  fun handler(ex: ServerWebInputException): ResponseEntity<Any>? {
    log.warn("Bad Request: ${ex.message}")

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).build()
  }
}

package mx.financial.person.support.exception

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ApiError(
  val code: Int,
  val message: String? = "Unknown error",
  val fields: Any? = null
)

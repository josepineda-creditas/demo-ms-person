package mx.financial.person.support.exception

class CustomErrorCodes private constructor() {

  companion object {
    const val BAD_REQUEST = 4000
    const val VALIDATION_ERROR = 4001
    const val UPDATE_FIELDS_ERROR = 4002
    const val FORBIDDEN = 4031
    const val NOT_FOUND = 4040
    const val NOT_UPDATABLE_LEAD = 4120
  }
}

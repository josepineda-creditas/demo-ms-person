package mx.financial.person.support.exception

class UpdateErrorsException(val errors: MutableMap<String, Any?>) : Exception()

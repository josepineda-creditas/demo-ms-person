package mx.financial.person.support.exception

import mx.financial.person.controller.dto.PersonDTO
import javax.validation.ConstraintViolation

class ValidationException(val errors: MutableIterator<ConstraintViolation<PersonDTO>>) : Exception()

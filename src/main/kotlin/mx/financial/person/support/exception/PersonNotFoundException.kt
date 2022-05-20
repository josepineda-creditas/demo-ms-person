package mx.financial.person.support.exception

class PersonNotFoundException(personId: String?) : Exception(
  "Person $personId was not found"
)

package mx.financial.person.support.exception

class PersonNotUpdatableException(personId: String?) : Exception(
  "Person $personId is not updatable"
)

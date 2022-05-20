package mx.financial.person.service

import mx.financial.person.models.person.Person
import mx.financial.person.repository.PersonsRepository
import kotlinx.coroutines.reactive.awaitFirst
import kotlinx.coroutines.reactive.awaitLast
import kotlinx.coroutines.reactive.awaitFirstOrNull
import mx.financial.person.support.exception.PersonNotFoundException
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class PersonService(
  private val personsRepository: PersonsRepository
) {
  suspend fun createPerson(person: Person): Person = personsRepository.save(person).awaitFirst()

  suspend fun update(person: Person): Person = personsRepository.save(person).awaitLast()

  suspend fun findUpdatablePersonByStrId(personId: String): Person {
    val person = findByStrId(personId)
    // Si tiene estatus de elimnado no se puede actualizar
    // if (lead.status == LeadStatus.DEEP_PQ_COMPLETED) throw LeadNotUpdatableException(leadId)
    return person
  }

  suspend fun findByStrId(personId: String) = try {
    findById(UUID.fromString(personId))
  } catch (ex: IllegalArgumentException) {
    throw PersonNotFoundException(personId)
  }

  suspend fun findById(id: UUID) =
    personsRepository.findById(id).awaitFirstOrNull() ?: throw PersonNotFoundException(id.toString())
}

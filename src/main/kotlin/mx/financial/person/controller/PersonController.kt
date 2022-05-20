package mx.financial.person.controller

import mx.financial.person.api.PersonApi
import mx.financial.person.controller.dto.PersonDTO
import mx.financial.person.controller.dto.PersonIdDTO
import mx.financial.person.models.person.Person
import mx.financial.person.service.PersonService
import mx.financial.person.support.exception.UpdateErrorsException
import mx.financial.person.support.exception.ValidationException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import javax.validation.Validator

@RestController
class PersonController(
  private val personService: PersonService,
  private val validator: Validator,
) : PersonApi {

  override suspend fun createPerson(personReq: PersonDTO) = ResponseEntity
    .status(HttpStatus.CREATED)
    .body(PersonIdDTO.fromEntity(personService.createPerson(personReq.toEntity())))

  override suspend fun findById(personId: String) = ResponseEntity
    .ok(PersonDTO.fromEntity(personService.findByStrId(personId)))

  override suspend fun updatePerson(
    personId: String,
    body: Map<String, Any?>
  ): ResponseEntity<PersonIdDTO> = updatePersonValidator(
    person = personService.findUpdatablePersonByStrId(personId),
    body = body
  )

  private suspend fun updatePersonValidator(
    person: Person,
    body: Map<String, Any?>
  ): ResponseEntity<PersonIdDTO> {
    val updateDTO = PersonDTO.fromEntity(person)
    val updateErrors = updateDTO.update(body)
    if (updateErrors.isNotEmpty()) {
      throw UpdateErrorsException(updateErrors)
    }

    val validation = validator.validate(updateDTO)
    if (validation.isNotEmpty()) {
      throw ValidationException(validation.iterator())
    }

    val updatedPerson = updateDTO.toEntity()
    updatedPerson.id = person.id

    return ResponseEntity.ok(PersonIdDTO.fromEntity(personService.update(updatedPerson)))
  }
}

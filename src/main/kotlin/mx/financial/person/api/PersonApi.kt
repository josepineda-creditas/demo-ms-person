package mx.financial.person.api

import mx.financial.person.controller.dto.PersonDTO
import mx.financial.person.controller.dto.PersonIdDTO
import mx.financial.person.support.openapi.person.PersonApiDocumentation
import mx.financial.person.support.openapi.person.PostCreateDocumentation
import mx.financial.person.support.openapi.person.PutUpdateDocumentation
import mx.financial.person.support.openapi.person.GetFindByIdDocumentation
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.PathVariable
import javax.validation.Valid

@PersonApiDocumentation
@RequestMapping("/v1/persons")
interface PersonApi {
  @PostCreateDocumentation
  @PostMapping
  suspend fun createPerson(
    @Valid @RequestBody personReq: PersonDTO
  ): ResponseEntity<PersonIdDTO>

  @PutUpdateDocumentation
  @PutMapping("/{personId}")
  suspend fun updatePerson(
    @PathVariable personId: String,
    @RequestBody body: Map<String, Any?>
  ): ResponseEntity<PersonIdDTO>

  @GetFindByIdDocumentation
  @GetMapping("/{personId}")
  suspend fun findById(
    @PathVariable personId: String,
  ): ResponseEntity<PersonDTO>
}

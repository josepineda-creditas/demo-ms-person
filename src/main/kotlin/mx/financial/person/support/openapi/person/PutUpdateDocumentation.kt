package mx.financial.person.support.openapi.person

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.parameters.RequestBody
import mx.financial.person.controller.dto.PersonDTO
import mx.financial.person.controller.dto.PersonIdDTO

@Operation(summary = "Update Person")
@ApiResponses(
  value = [
    ApiResponse(
      responseCode = "200",
      description = "Response with personId if succeeded",
      content = [
        Content(
          mediaType = "application/json",
          examples = [],
          schema = Schema(implementation = PersonIdDTO::class)
        )
      ]
    ),
  ]
)
@RequestBody(
  description = "Description of PersonDTO ",
  content = [
    Content(
      schema = Schema(implementation = PersonDTO::class)
    )
  ]
)
annotation class PutUpdateDocumentation

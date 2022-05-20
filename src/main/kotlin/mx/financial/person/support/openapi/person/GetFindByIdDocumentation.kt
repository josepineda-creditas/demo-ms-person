package mx.financial.person.support.openapi.person

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import mx.financial.person.controller.dto.PersonDTO

@Operation(summary = "Find by personId")
@ApiResponses(
  value = [
    ApiResponse(
      responseCode = "200",
      description = "Get a person info",
      content = [
        Content(
          mediaType = "application/json",
          schema = Schema(implementation = PersonDTO::class)
        )
      ]
    ),
  ]
)
annotation class GetFindByIdDocumentation

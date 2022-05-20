package mx.financial.person.support.openapi.person

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.parameters.RequestBody
import mx.financial.person.controller.dto.PersonIdDTO

@Operation(summary = "Create Person")
@ApiResponses(
  value = [
    ApiResponse(
      responseCode = "201",
      description = "Create Person",
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
@RequestBody()
annotation class PostCreateDocumentation

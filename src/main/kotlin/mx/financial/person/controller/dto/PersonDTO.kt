package mx.financial.person.controller.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import io.r2dbc.postgresql.codec.Json
import mx.financial.person.models.person.Person
import mx.financial.person.models.person.Status
import java.util.logging.Level
import java.util.logging.Logger
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.javaType
import kotlin.reflect.jvm.jvmErasure

data class PersonDTO(
  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  var id: String?,

  @field:NotBlank(message = "{validation.names.notBlank}")
  @field:Size(message = "{validation.names}", min = 3, max = 50)
  var names: String? = null,

  var lastNames: String? = null,
  var curp: String? = null,
  var rfc: String? = null,
  var email: String? = null,
  var birthDate: Long? = null,
  var cellPhone: String? = null,

  var address: JsonNode?,
  val createdAt: Long?
) {
  fun toEntity() = Person(
    id = null,
    names = names,
    lastNames = lastNames,
    curp = curp,
    rfc = rfc,
    email = email,
    birthDate = birthDate,
    cellPhone = cellPhone,
    address = toPostgresJson(address),
    status = Status.ACTIVE,
    createdAt = createdAt
  )

  private fun toPostgresJson(reference: JsonNode?) =
    if (reference == null) null else Json.of(reference.toPrettyString())

  fun update(data: Map<String, Any?>): MutableMap<String, Any?> {
    val errors = mutableMapOf<String, Any?>()
    for (prop in PersonDTO::class.memberProperties) {
      val value = data[prop.name]
      val targetClazz = prop.returnType.jvmErasure

      if (prop is KMutableProperty<*> && value != null) {
        if (targetClazz == value::class) {
          prop.setter.call(this, value)
          continue
        }
        try {
          prop.setter.call(this, tryCast(targetClazz, value))
        } catch (ex: IllegalArgumentException) {
          addError(errors, prop, value)
        }
      }
    }
    return errors
  }

  private fun tryCast(targetClazz: KClass<*>, value: Any) = when (targetClazz) {
    Double::class -> "$value".toDouble()
    Int::class -> "$value".toInt()
    Long::class -> "$value".toLong()
    JsonNode::class -> hashMapToJsonNode(value)
    else -> value
  }

  private fun hashMapToJsonNode(value: Any): JsonNode {
    val mapper = ObjectMapper()
    return mapper.readTree(mapper.writeValueAsString(value))
  }

  private val log: Logger = Logger.getLogger(PersonDTO::class.java.getName())

  private fun addError(
    errors: MutableMap<String, Any?>,
    prop: KProperty1<PersonDTO, *>,
    value: Any
  ) {
    val msg = "Expected %s but received %s"
    log.log(Level.WARNING, String.format(msg, prop.returnType, value::class.qualifiedName))

    errors[prop.name] = String.format(
      msg,
      prop.returnType.javaType.typeName.substringAfterLast("."),
      value::class.simpleName
    )
  }

  companion object {
    fun fromEntity(person: Person) = PersonDTO(
      id = person.id.toString(),
      names = person.names,
      lastNames = person.lastNames,
      curp = person.curp,
      rfc = person.rfc,
      email = person.email,
      birthDate = person.birthDate,
      cellPhone = person.cellPhone,
      address = fromPostgresJson(person.address),
      createdAt = person.createdAt
    )

    private fun fromPostgresJson(reference: Json?) =
      if (reference == null) null else ObjectMapper().readTree(reference.asString())
  }
}

class PersonIdDTO(
  val id: String
) {
  companion object {
    fun fromEntity(person: Person) = PersonIdDTO(
      id = person.id.toString()
    )
  }
}

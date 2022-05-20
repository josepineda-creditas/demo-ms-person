package mx.financial.person.models.person

import io.r2dbc.postgresql.codec.Json
import org.springframework.data.annotation.Id
import org.springframework.data.domain.Persistable
import org.springframework.data.relational.core.mapping.Table
import java.util.UUID

@Table("persons")
data class Person(
  @Id
  @JvmField var id: UUID?,
  val names: String? = null,
  val lastNames: String? = null,
  val curp: String? = null,
  val rfc: String? = null,
  val email: String? = null,
  val birthDate: Long?,
  val cellPhone: String? = null,
  val address: Json?,
  val status: Status? = null,
  val createdAt: Long? = null
) : Persistable<UUID> {

  override fun isNew(): Boolean {
    val isNewPerson: Boolean = id == null
    if (isNewPerson) id = UUID.randomUUID()
    return isNewPerson
  }

  override fun getId(): UUID? = id
}

enum class Status {
  ACTIVE,
  INACTIVE
}

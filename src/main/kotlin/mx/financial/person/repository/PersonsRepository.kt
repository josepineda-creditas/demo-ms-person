package mx.financial.person.repository

import mx.financial.person.models.person.Person
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface PersonsRepository : ReactiveCrudRepository<Person, UUID>

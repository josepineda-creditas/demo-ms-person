package mx.financial.person

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class PersonApplication

@SuppressWarnings("SpreadOperator")
fun main(args: Array<String>) {
  runApplication<PersonApplication>(*args)
}

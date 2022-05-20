package mx.financial.person.configuration

import io.r2dbc.postgresql.client.SSLMode
import io.r2dbc.spi.ConnectionFactories
import io.r2dbc.spi.ConnectionFactory
import io.r2dbc.spi.ConnectionFactoryOptions
import io.r2dbc.spi.ConnectionFactoryOptions.DATABASE
import io.r2dbc.spi.ConnectionFactoryOptions.DRIVER
import io.r2dbc.spi.ConnectionFactoryOptions.HOST
import io.r2dbc.spi.ConnectionFactoryOptions.PASSWORD
import io.r2dbc.spi.ConnectionFactoryOptions.PORT
import io.r2dbc.spi.ConnectionFactoryOptions.USER
import io.r2dbc.spi.Option
import org.flywaydb.core.Flyway
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing
import java.net.URLDecoder
import java.nio.charset.Charset

@Configuration
@EnableR2dbcAuditing
class DbConfig : AbstractR2dbcConfiguration() {

  @Value("\${spring.databaseUrl}")
  val dbUrl: String = ""

  companion object {
    const val FLYWAY_PROTOCOL = "jdbc:postgresql://"
    const val R2DBC_DRIVER = "postgresql"

    @JvmField
    val R2DBC_REGEX = ".+://(.+):(.+)@(.+):(.+)/(.+)\\?sslmode=(.+)".toRegex()

    @JvmField
    val JDBC_REGEX = ".+://.+:.+@(.+)".toRegex()
  }

  @Bean
  fun databaseInfo(): DatabaseInfo {
    val (fullHost) = JDBC_REGEX.find(dbUrl)!!.destructured
    val (user, pass, host, port, database, sslMode) = R2DBC_REGEX.find(dbUrl)!!.destructured

    return DatabaseInfo(
      flywayUrl = "${FLYWAY_PROTOCOL}$fullHost",
      user = user,
      pass = URLDecoder.decode(pass, Charset.defaultCharset().name()),
      host = host,
      port = Integer.parseInt(port),
      database = database,
      sslMode = SSLMode.valueOf(sslMode.toUpperCase())
    )
  }

  @Primary
  @Bean(initMethod = "migrate")
  fun flyway(dbInfo: DatabaseInfo): Flyway = Flyway(
    Flyway.configure()
      .baselineOnMigrate(true)
      .dataSource(dbInfo.flywayUrl, dbInfo.user, dbInfo.pass)
  )

  @Primary
  @Bean
  override fun connectionFactory(): ConnectionFactory {
    val dbInfo = databaseInfo()

    val optionBuilder = ConnectionFactoryOptions.builder()
      .option(DRIVER, R2DBC_DRIVER)
      .option(HOST, dbInfo.host)
      .option(PORT, dbInfo.port)
      .option(USER, dbInfo.user)
      .option(PASSWORD, dbInfo.pass)
      .option(DATABASE, dbInfo.database)
      .option(Option.valueOf("sslMode"), dbInfo.sslMode)

    return ConnectionFactories.get(optionBuilder.build())
  }
}

data class DatabaseInfo(
  val flywayUrl: String,
  val host: String,
  val port: Int,
  val user: String,
  val pass: String,
  val database: String,
  val sslMode: SSLMode
)

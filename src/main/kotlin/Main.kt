import db.DatabaseConfig
import db.DatabaseManager
import io.github.oshai.kotlinlogging.KotlinLogging
import log.LoggingMiddleware
import person.PersonController
import person.PersonRepository
import person.PersonService
import route.HttpRouter
import route.RoutingMiddleware
import route.controllerMethodHandler
import util.Env.Companion.getEnv
import util.Env.Companion.loadEnvFile

private val logger = KotlinLogging.logger {}

fun main() {

  val start = System.currentTimeMillis()

  loadEnvFile(".env")

  val dbConfig = DatabaseConfig(
    jdbcUrl = "jdbc:postgresql://${getEnv("POSTGRES_HOST")}:5433/${getEnv("POSTGRES_DB")}",
    username = getEnv("POSTGRES_USER")!!,
    password = getEnv("POSTGRES_PASSWORD")!!
  )
  val databaseManager = DatabaseManager(dbConfig)
  val personRepository = PersonRepository(databaseManager)
  val personService = PersonService(personRepository)
  val personController = PersonController(personService)

  val router = HttpRouter()
    .enableHealthCheck()
    .delete("/api/people/{id}", controllerMethodHandler(personController::deletePerson))
    .get("/api/people/{id}", controllerMethodHandler(personController::personById))
    .get("/api/people", personController::people)
    .post("/api/people", personController::savePerson)

  val port = getEnv("SERVER_PORT")!!.toInt()
  val server = Server(port)
    .add(LoggingMiddleware())
    .add(RoutingMiddleware(router))

  server.start()

  logger.info { "Server started in ${System.currentTimeMillis() - start}ms on port $port" }

  // Keep the main thread alive to prevent the application from exiting
  Thread.currentThread().join()
}

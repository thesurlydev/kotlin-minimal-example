package person

import http.server.HttpRequest
import http.server.HttpResponse
import http.server.HttpResponse.Companion.internalServerError
import http.server.HttpResponse.Companion.notFound
import http.server.HttpResponse.Companion.ok
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.*

class PersonController(private val personService: PersonService) {

  fun personById(uuid: UUID): HttpResponse {
    return when (val person: Person? = personService.findById(uuid)) {
      null -> notFound()
      else -> json(person)
    }
  }

  fun people(request: HttpRequest): HttpResponse {
    val people = personService.findAll()
    return json(People(people))
  }

  fun deletePerson(uuid: UUID): HttpResponse {
    return if (personService.delete(uuid)) ok() else internalServerError("Failed to delete person")
  }

  fun savePerson(request: HttpRequest): HttpResponse {
    // TODO assumes body is JSON
    val person = request.jsonToEntity<Person>()
    val savedPerson = person?.let { personService.save(it) } ?: return internalServerError("Failed to save person")
    return json<Person>(savedPerson)
  }
}

inline fun <reified T> json(obj: T): HttpResponse {
  val jsonString = Json.encodeToString(obj)
  return ok().json(jsonString.toByteArray())
}

inline fun <reified T> HttpRequest.jsonToEntity(): T? {
  if (this.body == null) return null
  return Json.decodeFromString(this.body!!.toString())
}
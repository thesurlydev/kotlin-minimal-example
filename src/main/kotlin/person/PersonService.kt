package person

import java.util.*

class PersonService(private val personRepository: PersonRepository) {

  fun findById(id: UUID): Person? {
    return personRepository.findById(id)
  }

  fun findAll(): List<Person> {
    return personRepository.findAll()
  }

  fun save(person: Person): Person? {
    return personRepository.save(person)
  }

  fun delete(id: UUID): Boolean {
    return personRepository.delete(id)
  }
}
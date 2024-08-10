package person

import db.DatabaseManager
import db.Querier
import repository.Repository
import java.sql.ResultSet
import java.util.*

class PersonRepository(private val databaseManager: DatabaseManager) : Repository<UUID, Person> {
  override fun findAll(): List<Person> {
    val sql = "SELECT * FROM people"
    return Querier.executeQuery(databaseManager, sql, ::person)
  }

  override fun findById(id: UUID): Person? {
    val sql = "SELECT * FROM people WHERE id = :id"
    val params = mapOf("id" to id)
    return Querier.queryWithParams(databaseManager, sql, params, ::maybePerson)
  }

  override fun delete(id: UUID): Boolean {
    val sql = "DELETE FROM people WHERE id = :id"
    val params = mapOf("id" to id)
    return Querier.updateObject(databaseManager, sql, params) > 0
  }

  override fun save(entity: Person): Person? {
    if (entity.id != null) {
      val sql = "UPDATE people SET name = :name, age = :age WHERE id = :id"
      val params = mapOf<String, Any?>(
        "id" to entity.id,
        "name" to entity.name,
        "age" to entity.age
      )
      return Querier.saveObject(databaseManager, sql, params, ::person)
    }
    val sql = "INSERT INTO people (name, age) VALUES (:name, :age)"
    val params = mapOf<String, Any?>(
      "name" to entity.name,
      "age" to entity.age
    )
    return Querier.saveObject(databaseManager, sql, params, ::person)
  }

  private fun maybePerson(resultSet: ResultSet): Person? = when {
    resultSet.next() -> person(resultSet)
    else -> null
  }

  private fun person(resultSet: ResultSet) = Person(
    resultSet.getString("id"),
    resultSet.getString("name"),
    resultSet.getInt("age")
  )
}
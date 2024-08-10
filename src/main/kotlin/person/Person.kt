package person

import data.NoReflect
import kotlinx.serialization.Serializable

@Serializable
data class Person(val id: String? = null, val name: String, val age: Int) : NoReflect {
  override fun fieldGetters(): Map<String, () -> Any?> = mapOf(
    "id" to { this.id },
    "name" to { this.name },
    "age" to { this.age }
  )
}

package person

import kotlinx.serialization.Serializable

@Serializable
data class People(val data: List<Person>)

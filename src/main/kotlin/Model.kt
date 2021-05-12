import java.util.*

data class Counter(
        val id: Int,
        var name: String,
        var count: Int
)

val counters: MutableList<Counter> = Collections.synchronizedList(mutableListOf())

data class CreateCounterBody(
        val name: String
)

data class CreateCountersBody(
        val names: List<String>
)

data class EditCounterBody(
        val id: Int
)

data class EditCountersBody(
        val ids: List<Int>
)

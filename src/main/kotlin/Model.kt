import java.util.*

data class Counter(
        val id: Id,
        var name: Name,
        var count: Count
)

val counters: MutableList<Counter> = Collections.synchronizedList(mutableListOf())

data class CreateCounterBody(
        val name: Name
)

data class CreateCountersBody(
        val names: List<Name>
)

data class EditCounterBody(
        val id: Id
)

data class EditCountersBody(
        val ids: List<Id>
)

inline class Name(val value: String)
inline class Id(val value: Int)
class Count(
        var value: Int
) {
    var valor = Int
}
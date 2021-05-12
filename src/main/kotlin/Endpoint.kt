import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.*
import kotlinx.atomicfu.atomic

private const val API_VERSION = "/v1"
private const val COUNTERS_PATH = "/counters"
private const val COUNTER_PATH = "/counter"
private const val INC_PATH = "/inc"
private const val DEC_PATH = "/dec"
private const val DELETED_MSG = "DELETED"
const val INVALID_ID_MSG = "INVALID ID"
private val id = atomic(initial = 0)

fun Routing.countersEndpoints() = route(path = API_VERSION) {

    //region Get counters
    get(path = COUNTERS_PATH) {
        call.respond(counters.toList())
    }
    //endregion

    //region Create counters
    post(path = COUNTER_PATH) {
        with(call) {
            val body: CreateCounterBody = receive()
            val counter = Counter(
                    id = id.incrementAndGet(),
                    name = body.name,
                    count = 0
            )
            counters += counter
            respond(HttpStatusCode.Created, counter)
        }
    }

    post(path = COUNTERS_PATH) {
        with(call) {
            val body: CreateCountersBody = receive()
            body.names.forEach { name ->
                val counter = Counter(
                        id = id.incrementAndGet(),
                        name = name,
                        count = 0
                )
                counters += counter
            }
            respond(HttpStatusCode.Created, counters.takeLast(body.names.size))
        }
    }
    //endregion

    //region Increment count by counter id
    patch(path = COUNTER_PATH + INC_PATH) {
        with(call) {
            val body: EditCounterBody = receive()
            val counter: Counter = counters.first { it.id == body.id }
            counter.count += 1
            respond(HttpStatusCode.OK, counter)
        }
    }

    patch(path = COUNTERS_PATH + INC_PATH) {
        with(call) {
            val body: EditCountersBody = receive()
            body.ids.forEach { id ->
                val counter: Counter = counters.first { it.id == id }
                counter.count += 1
            }
            respond(HttpStatusCode.OK, counters.takeLast(body.ids.size))
        }
    }
    //endregion

    //region Decrement count by counter id
    patch(path = COUNTER_PATH + DEC_PATH) {
        with(call) {
            val body: EditCounterBody = receive()
            val counter: Counter = counters.first { it.id == body.id && it.count > 0 }
            counter.count -= 1
            respond(HttpStatusCode.OK, counter)
        }
    }

    patch(path = COUNTERS_PATH + DEC_PATH) {
        with(call) {
            val body: EditCountersBody = receive()
            body.ids.forEach { id ->
                val counter: Counter = counters.first { it.id == id && it.count > 0 }
                counter.count -= 1
            }
            respond(HttpStatusCode.OK, counters.takeLast(body.ids.size))
        }
    }
    //endregion

    //region Delete counters by id
    delete(path = COUNTER_PATH) {
        with(call) {
            val body: EditCounterBody = receive()
            counters.remove(counters.first { it.id == body.id })
            respond(HttpStatusCode.NoContent, DELETED_MSG)
        }
    }

    delete(path = COUNTERS_PATH) {
        with(call) {
            val body: EditCountersBody = receive()
            body.ids.forEach { id ->
                counters.remove(counters.first { it.id == id })
            }
            respond(HttpStatusCode.NoContent, DELETED_MSG)
        }
    }
    //endregion
}

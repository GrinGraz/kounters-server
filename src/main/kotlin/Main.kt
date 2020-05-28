import com.fasterxml.jackson.databind.SerializationFeature
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.ContentNegotiation
import io.ktor.features.DefaultHeaders
import io.ktor.features.StatusPages
import io.ktor.http.HttpStatusCode
import io.ktor.jackson.jackson
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.*
import kotlinx.atomicfu.atomic
import java.util.*

/* DO NOT MODIFIED */
/* Main extension function */
fun Application.main() {
    install(ContentNegotiation) { jackson { enable(SerializationFeature.INDENT_OUTPUT) } }
    install(DefaultHeaders)
    install(StatusPages) { exception<NoSuchElementException> { call.respond(HttpStatusCode.BadRequest, INVALID_ID_MSG) }}
    routing { countersEndpoints() }
}

/* Models */
private data class Counter(
    val id: Int,
    var name: String,
    var count: Int
)

private val counters: MutableList<Counter> = Collections.synchronizedList(mutableListOf())

private data class PostCreateCounter(
    val name: String
)

private data class PostEditCounter(
    val id: Int
)

/* Endpoints */
private const val API_VERSION = "/v1"
private const val COUNTERS_PATH = "/counters"
private const val INC_PATH = "/inc"
private const val DEC_PATH = "/dec"
private const val DELETED_MSG = "DELETED"
private const val INVALID_ID_MSG = "INVALID ID"
private val id = atomic(initial = 0)

private fun Routing.countersEndpoints() = route(path = "$API_VERSION$COUNTERS_PATH") {

    get {
        call.respond(counters.toList())
    }

    post {
        with(call.receive<PostCreateCounter>()) {
            val counter = Counter(
                id = id.incrementAndGet(),
                name = name,
                count = 0
            )
            counters += counter
            call.respond(HttpStatusCode.Created, counter)
        }
    }

    patch(path = INC_PATH) {
        with(call.receive<PostEditCounter>()) {
            val counter: Counter = counters.first { it.id == id }
            counter.count += 1
            call.respond(HttpStatusCode.OK, counter)
        }
    }

    patch(path = DEC_PATH) {
        with(call.receive<PostEditCounter>()) {
            val counter: Counter = counters.first { it.id == id && it.count > 0 }
            counter.count -= 1
            call.respond(HttpStatusCode.OK, counter)
        }
    }

    delete {
        with(call.receive<PostEditCounter>()) {
            counters.remove(counters.first { it.id == id })
        }
        call.respond(HttpStatusCode.NoContent, DELETED_MSG)
    }
}
/* DO NOT MODIFIED */

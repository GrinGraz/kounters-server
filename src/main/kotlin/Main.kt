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

fun Application.main() {
    install(ContentNegotiation) { jackson { enable(SerializationFeature.INDENT_OUTPUT) } }
    install(DefaultHeaders)
    install(StatusPages) { exception<NoSuchElementException> { call.respond(HttpStatusCode.BadRequest, INVALID_ID_MSG) } }
    routing { countersEndpoints() }
}

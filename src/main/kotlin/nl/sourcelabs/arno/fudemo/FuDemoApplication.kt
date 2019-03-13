package nl.sourcelabs.arno.fudemo

import org.springframework.fu.kofu.web.server
import org.springframework.fu.kofu.webApplication
import org.springframework.web.reactive.function.server.*
import org.springframework.web.reactive.function.server.ServerResponse.ok
import reactor.core.publisher.Mono

val app = webApplication {
    beans {
        bean<SampleHandler>()
    }
    server {
        router {
            val handler = ref<SampleHandler>()
            GET("/hello", handler::hello)
        }
    }
}

class SampleHandler {
    fun hello(request: ServerRequest): Mono<ServerResponse> = ok().syncBody("Hello world!")
}

fun main() {
    app.run()
}


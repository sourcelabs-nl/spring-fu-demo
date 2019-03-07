package nl.sourcelabs.arno.fudemo

import org.springframework.http.MediaType
import org.springframework.web.function.server.CoServerRequest
import org.springframework.web.function.server.body
import org.springframework.web.function.server.coHandler

class BooksHandler(
        private val repository: BookRepository,
        private val configuration: SampleProperties) {

    suspend fun findOne(request: CoServerRequest) = coHandler {
        val isbn = request.pathVariable("isbn")!!
        ok().contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(repository.findOne(isbn))
    }

    suspend fun listApi(request: CoServerRequest) = coHandler {
        ok().contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(repository.findAll())
    }

//    suspend fun listView(request: ServerRequest) =
//            ok().renderAndAwait("users", mapOf("users" to repository.findAll()))


    suspend fun conf(request: CoServerRequest) = coHandler {
        ok().syncBody(configuration.message)
    }

}

package nl.sourcelabs.arno.fudemo

import org.springframework.http.MediaType
import org.springframework.web.function.server.bodyAndAwait
import org.springframework.web.function.server.renderAndAwait
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse.ok

class BooksHandler(
        private val repository: BookRepository,
        private val configuration: SampleProperties) {

    suspend fun findOne(request: ServerRequest) =
        ok().contentType(MediaType.APPLICATION_JSON_UTF8)
                .bodyAndAwait(repository.findOne(request.pathVariable("isbn")))


    suspend fun listApi(request: ServerRequest) =
        ok().contentType(MediaType.APPLICATION_JSON_UTF8)
                .bodyAndAwait(repository.findAll())


    suspend fun listView(request: ServerRequest) =
            ok().renderAndAwait("books", mapOf("books" to repository.findAll()))


    suspend fun conf(request: ServerRequest) =
            ok().bodyAndAwait(configuration.message)


}

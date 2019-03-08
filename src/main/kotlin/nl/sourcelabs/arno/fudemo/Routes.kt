package nl.sourcelabs.arno.fudemo

import org.springframework.web.function.server.coRouter

fun routes(booksHandler: BooksHandler) = coRouter {
    GET("/", booksHandler::listView)
    GET("/api/books/{isbn}", booksHandler::findOne)
    GET("/api/books", booksHandler::listApi)
    GET("/conf", booksHandler::conf)
}

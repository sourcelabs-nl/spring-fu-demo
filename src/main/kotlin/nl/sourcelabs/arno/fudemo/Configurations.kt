package nl.sourcelabs.arno.fudemo

import kotlinx.coroutines.runBlocking
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.fu.kofu.bean
import org.springframework.fu.kofu.configuration
import org.springframework.fu.kofu.r2dbc.r2dbcH2
import org.springframework.fu.kofu.web.mustache
import org.springframework.fu.kofu.web.server

val dataConfig = configuration {
    beans {
        bean<BookRepository>()
    }
    listener<ApplicationReadyEvent> {
        runBlocking {
            ref<BookRepository>().init()
        }
    }
    r2dbcH2 {
        coroutines = true
    }
}

val webConfig = configuration {
    beans {
        bean<BooksHandler>()
        bean(::routes)
    }
    server {
        port = if (profiles.contains("test")) 8181 else 8080
        mustache()
        codecs {
            string()
            jackson()
        }
    }
}

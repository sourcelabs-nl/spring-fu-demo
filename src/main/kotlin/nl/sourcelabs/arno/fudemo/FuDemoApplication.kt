package nl.sourcelabs.arno.fudemo

import kotlinx.coroutines.runBlocking
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.boot.logging.LogLevel
import org.springframework.data.r2dbc.function.CoDatabaseClient
import org.springframework.fu.kofu.configuration
import org.springframework.fu.kofu.r2dbc.r2dbcH2
import org.springframework.fu.kofu.web.server
import org.springframework.fu.kofu.webApplication
import org.springframework.http.MediaType
import org.springframework.web.function.server.CoServerRequest
import org.springframework.web.function.server.body
import org.springframework.web.function.server.coHandler

val app = webApplication {
    logging {
        level = LogLevel.DEBUG
    }
    configurationProperties<SampleProperties>(prefix = "sample")
    enable(dataConfig)
    enable(webConfig)
}

class SampleProperties {
    lateinit var message: String
}

data class User(
        val login: String,
        val firstName: String,
        val lastName: String
)

val dataConfig = configuration {
    beans {
        bean<UserRepository>()
    }
    listener<ApplicationReadyEvent> {
        runBlocking {
            ref<UserRepository>().init()
        }
    }
    r2dbcH2 {
        coroutines = true
    }
}

val webConfig = configuration {
    beans {
        bean<UserHandler>()
    }
    server {
        port = if (profiles.contains("test")) 8181 else 8080
        coRouter {
            val userHandler = ref<UserHandler>()
            GET("/api/user", userHandler::listApi)
            GET("/conf", userHandler::conf)
        }
        codecs {
            string()
            jackson()
        }
    }
}

class UserHandler(
        private val repository: UserRepository,
        private val configuration: SampleProperties) {

    suspend fun listApi(request: CoServerRequest) = coHandler {
        ok().contentType(MediaType.APPLICATION_JSON)
                .body(repository.findAll())
    }

    suspend fun conf(request: CoServerRequest) = coHandler {
        ok().syncBody(configuration.message)
    }

}

class UserRepository(private val client: CoDatabaseClient) {

    suspend fun count() =
            client.execute().sql("Select COUNT(*) FROM users").asType(Int::class).fetch().one()!!

    suspend fun findAll() =
            client.select().from("users").asType(User::class).fetch().all()

    suspend fun findOne(id: String) =
            client.execute().sql("SELECT * FROM users WHERE login = \$1").bind(1, id).asType(User::class).fetch().one()

    suspend fun deleteAll() {
        client.execute().sql("DELETE FROM users").fetch().one()
    }

    suspend fun save(user: User) =
            client.insert().into(User::class).table("users").using(user).execute()

    suspend fun init() {
        client.execute().sql("CREATE TABLE users (login varchar PRIMARY KEY, first_name varchar, last_name varchar)").fetch().rowsUpdated()
        deleteAll()
        save(User("koehlerad", "Arno", "Koehler"))
        save(User("asssenpaw", "Paul", "Assen"))
        save(User("dewijzeman", "Robbert-Jan", "Peters"))
    }

}

fun main(args: Array<String>) {
    app.run(args)
}


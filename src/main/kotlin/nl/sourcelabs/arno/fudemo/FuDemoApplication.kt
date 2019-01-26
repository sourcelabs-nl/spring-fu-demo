package nl.sourcelabs.arno.fudemo

import kotlinx.coroutines.runBlocking
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.boot.logging.LogLevel
import org.springframework.data.r2dbc.function.CoDatabaseClient
import org.springframework.fu.kofu.configuration
import org.springframework.fu.kofu.r2dbc.r2dbcH2
import org.springframework.fu.kofu.web.server
import org.springframework.fu.kofu.webApplication

val app = webApplication {
    server()
    logging {
        level = LogLevel.DEBUG
    }
    configurationProperties<SampleProperties>(prefix = "sample")
    enable(dataConfig)
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

class UserRepository(private val client: CoDatabaseClient) {

    suspend fun count() =
            client.execute().sql("Select COUNT(*) FROM users").asType(Int::class).fetch().one()!!

    suspend fun findAll() =
            client.select().from("users").asType(User::class).fetch().all()

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


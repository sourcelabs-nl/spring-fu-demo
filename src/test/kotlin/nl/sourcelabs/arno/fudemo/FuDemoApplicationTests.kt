package nl.sourcelabs.arno.fudemo

import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class FuDemoApplicationTests {

    private val client = WebTestClient.bindToServer().baseUrl("http://localhost:8181").build()

    private lateinit var context: ConfigurableApplicationContext

    @BeforeAll
    fun beforeAll() {
        context = app.run(profiles = "test")
    }

    @Test
    fun `Request HTTP api endpoint`() {
        client.get().uri("/api/books").exchange()
                .expectStatus().is2xxSuccessful
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
    }

    @Test
    fun `Request of endpoint`() {
        client.get().uri("/conf").exchange()
                .expectStatus().is2xxSuccessful
                .expectHeader().contentType("text/plain;charset=UTF-8")
    }

    @AfterAll
    fun afterAll() {
        context.close()
    }

}


package nl.sourcelabs.arno.fudemo

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.getBean

class FuDemoApplicationTests {

    @Test
    fun `Run the web application`() {
        val context = app.run()
        assertEquals("hello", context.getBean<SampleProperties>().message)
    }

}


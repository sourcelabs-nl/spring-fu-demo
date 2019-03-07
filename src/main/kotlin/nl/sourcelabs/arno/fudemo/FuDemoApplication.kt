package nl.sourcelabs.arno.fudemo

import org.springframework.boot.logging.LogLevel
import org.springframework.fu.kofu.webApplication

val app = webApplication {
    logging {
        level = LogLevel.DEBUG
    }
    configurationProperties<SampleProperties>("sample")
    enable(dataConfig)
    enable(webConfig)
}

fun main(args: Array<String>) {
    app.run(args)
}

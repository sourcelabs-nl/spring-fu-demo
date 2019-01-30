# spring-fu-demo
Spring FU demo

## spring initializer project Kotlin & reactive web

Choose new project from initializer and choose kotlin

## add dependencies

start with modifying dependencies

        <dependency>
            <groupId>org.springframework.fu</groupId>
            <artifactId>spring-fu-kofu</artifactId>
            <version>0.0.4</version>
        </dependency>
        <dependency>
            <groupId>org.springframework.fu</groupId>
            <artifactId>spring-fu-webflux-coroutines</artifactId>
            <version>0.0.4</version>
        </dependency>
        <dependency>
            <groupId>org.springframework.fu</groupId>
            <artifactId>spring-fu-data-r2dbc-coroutines</artifactId>
            <version>0.0.4</version>
        </dependency>
        <dependency>
            <groupId>io.r2dbc</groupId>
            <artifactId>r2dbc-h2</artifactId>
            <version>1.0.0.M6</version>
        </dependency>
        <dependency>
            <groupId>org.junit.jupiter</groupId>
            <artifactId>junit-jupiter-api</artifactId>
            <version>5.3.2</version>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
            <!-- and exclude: -->
            <exclusions>
                <exclusion>
                    <artifactId>junit</artifactId>
                    <groupId>junit</groupId>
                </exclusion>
            </exclusions>
        </dependency>

## remove all the imports

and start modifying the app to have

    val app = webApplication {
           server()
    }
    
and the test to

    fun `Run the web application`() {
    
with the jupiter style of testing


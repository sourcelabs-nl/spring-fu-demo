package nl.sourcelabs.arno.fudemo

import org.springframework.data.r2dbc.function.CoDatabaseClient

class BookRepository(private val client: CoDatabaseClient) {

    suspend fun count() =
            client.execute().sql("Select COUNT(*) FROM books").asType(Int::class).fetch().one()!!

    suspend fun findAll() =
            client.select().from("books").asType(Book::class).fetch().all()

    suspend fun findOne(id: String) =
            client.execute().sql("SELECT * FROM books WHERE isbn = \$1").bind(1, id).asType(Book::class).fetch().one()!!

    suspend fun deleteAll() {
        client.execute().sql("DELETE FROM books").execute()
    }

    suspend fun save(book: Book) =
            client.insert().into(Book::class).table("books").using(book).execute()

    suspend fun init() {
        client.execute().sql("CREATE TABLE IF NOT EXISTS books (isbn varchar PRIMARY KEY, book_name varchar, author varchar);").execute()
        deleteAll()
        save(Book("9780142410370", "Matilda", "Roald Dahl"))
        save(Book("9780142410387", "Paul", "Roald Dahl"))
        save(Book("9780142410349", "Fantastic Mr. Fox", "Roald Dahl"))
        save(Book("9789021481036", "Jip en Janneke", "Annie M.G. Schmitt"))
    }

}

package kb.tmp2.orm;

import kb.tmp2.orm.config.Db;
import kb.tmp2.orm.domain.Author;
import kb.tmp2.orm.domain.Book;
import org.hibernate.graph.GraphSemantic;

import java.util.List;
import java.util.stream.Collectors;

public class App {
    static List<Book> books;

    public static void main(String[] args) {
        Db.inTransaction(session -> {
            books = session.createSelectionQuery("from Book", Book.class)
                    .setEntityGraph(session.createEntityGraph(Book.class, "book_with_authors_awards"),
                            GraphSemantic.FETCH)
                    .setMaxResults(30)
                    .getResultList();
        });

        books.stream()
                .map(b -> """
                        %s by %s, awards: %s
                        """.formatted(
                        b.getTitle(),
                        b.getAuthors().stream()
                                .map(Author::getName)
                                .collect(Collectors.joining(", ")),
                        b.getAwards().stream()
                                .map(a -> a.getTitle() + " (" + a.getCommission().getName() + ")")
                                .collect(Collectors.joining(", "))
                ))
                .forEach(System.out::println);
    }
}

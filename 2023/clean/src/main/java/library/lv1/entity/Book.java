package library.lv1.entity;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder(toBuilder = true)
public class Book {
    private final long id;
    private final String title;
    private final String author;

    public Book(long id, String title, String author) {
        if (id < 0) {
            throw new IllegalArgumentException("id must be positive integer");
        }
        this.id = id;
        this.title = title;
        this.author = author;
    }
}

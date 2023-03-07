package library.lv5.impl.repo;

import library.lv1.entity.Book;
import library.lv2.spi.repo.BookRepository;
import library.lv2.spi.repo.EntityNotFoundAppException;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MapBookRepository implements BookRepository {
    private final Map<Long, Book> map = new LinkedHashMap<>() {{
        put(1L, new Book(1, "title 1", "author 1"));
        put(2L, new Book(2, "title 2", "author 2"));
        put(3L, new Book(3, "title 3", "author 3"));
    }};

    @Override
    public List<Book> findAll() {
        return map.values().stream().toList();
    }

    @Override
    public Book save(Book book) {
        return book.getId() == 0 ? insert(book) : update(book);
    }

    private Book insert(Book book) {
        var id = map.keySet().stream().mapToLong(x -> x).max().orElse(0) + 1;
        book = book.toBuilder().id(id).build();
        map.put(id, book);
        return book;
    }

    private Book update(Book book) {
        if (!map.containsKey(book.getId())) {
            throw new EntityNotFoundAppException("no book with id " + book.getId());
        }
        map.put(book.getId(), book);
        return book;
    }
}

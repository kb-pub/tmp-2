package library.lv2.spi.repo;

import library.lv1.entity.Book;

import java.util.List;

public interface BookRepository {
    List<Book> findAll();

    /**
     *
     * @param book
     * @return !!!!!!!!
     */
    Book save(Book book);
}

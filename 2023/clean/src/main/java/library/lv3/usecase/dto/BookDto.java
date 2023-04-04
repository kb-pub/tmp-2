package library.lv3.usecase.dto;

import lombok.Data;

@Data
public class BookDto {
    private final long id;
    private final String title;
    private final int year;

    public static BookDto newBook(String title, int year) {
        return new BookDto(0, title, year);
    }
}

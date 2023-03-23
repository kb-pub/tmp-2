package library.lv4.controller.console;

import library.AppException;
import library.lv3.usecase.AddNewBookUseCase;
import library.lv3.usecase.GetAllBooksUseCase;
import library.lv3.usecase.dto.BookDto;
import library.lv4.controller.Controller;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ConsoleController implements Controller {
    private final IO io;
    private final GetAllBooksUseCase getAllBooksUseCase;
    private final AddNewBookUseCase addNewBookUseCase;

    public void start() {
        var command = "";
        do {
            io.print(">>> ");
            command = io.read().strip().toLowerCase();
            switch (command) {
                case "book ls" -> showAllBooks();
                case "book add" -> addNewBook();
                case "exit" -> io.println("bye!");
                default -> io.println("unrecognized command: " + command);
            }
        }
        while (! "exit".equals(command));
    }

    private void showAllBooks() {
        var response = getAllBooksUseCase.get();
        if (response.getBooks().isEmpty()) {
            io.println("no books found");
        }
        else {
            io.println("books in library:");
            response.getBooks().forEach(b -> io.println(book2String(b)));
        }
    }

    private void addNewBook() {
        try {
            io.print("Title: ");
            var title = io.read();
            io.print("Year: ");
            var year = io.readInt();
            addNewBookUseCase.add(new BookDto(title, year));
            io.println("all done!");
        }
        catch (AppException e) {
            io.println("error: " + e.getMessage());
        }
    }

    private String book2String(BookDto b) {
        return String.format("\"%s\", year %s", b.getTitle(), b.getYear());
    }
}

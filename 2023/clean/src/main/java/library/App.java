package library;

import library.lv3.usecase.AddNewBookUseCase;
import library.lv3.usecase.GetAllAuthorsUseCase;
import library.lv3.usecase.GetAllBooksUseCase;
import library.lv4.controller.Controller;
import library.lv4.controller.console.AuthorController;
import library.lv4.controller.console.BookController;
import library.lv4.controller.console.ConsoleController;
import library.lv5.impl.infrastructure.ConsoleIO;
import library.lv5.impl.repo.pg.PgAuthorRepository;
import library.lv5.impl.repo.pg.PgBookRepository;
import library.lv5.impl.repo.pg.PgDataSource;
import library.lv5.impl.service.StubEmailService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class App {
    public static void main(String[] args) {
        build().start();
    }

    private static Controller build() {
        var dataSource = new PgDataSource();

//        var bookRepo = new MapBookRepository();
        var bookRepo = new PgBookRepository(dataSource);
        var authorRepo = new PgAuthorRepository(dataSource);

        var io = new ConsoleIO();

        var emailService = new StubEmailService(io);
//        var emailService = new YandexMailService();

        var getAllBooksUseCase = new GetAllBooksUseCase(bookRepo);
        var addNewBookUseCase = new AddNewBookUseCase(
                bookRepo,
                emailService);
        var getAllAuthorsUseCase = new GetAllAuthorsUseCase(authorRepo);

        var bookController = new BookController(io, getAllBooksUseCase, addNewBookUseCase);
        var authorController = new AuthorController(io, getAllAuthorsUseCase);

        var controller = new ConsoleController(
                io, bookController, authorController);
//        GetAllBooksAction.Factory.init(getAllBooksUseCase);
//        var controller = new TelegramBotController();

        return controller;
    }
}

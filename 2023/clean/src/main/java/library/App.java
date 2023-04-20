package library;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import library.lv3.usecase.AddNewBookUseCase;
import library.lv3.usecase.GetAllAuthorsUseCase;
import library.lv3.usecase.GetAllBooksUseCase;
import library.lv4.controller.Controller;
import library.lv4.controller.console.AuthorController;
import library.lv4.controller.console.BookController;
import library.lv4.controller.console.ConsoleController;
import library.lv4.controller.mapper.Mapper;
import library.lv5.impl.infrastructure.ConsoleIO;
import library.lv5.impl.repo.pg.PgAuthorRepository;
import library.lv5.impl.repo.pg.PgBookRepository;
import library.lv5.impl.repo.pg.PgDataSource;
import library.lv5.impl.service.StubEmailService;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.logging.log4j.LogManager;

@Slf4j
public class App {
    public static final Mapper mapper;
    public static final GetAllBooksUseCase getAllBooksUseCase;
    public static final AddNewBookUseCase addNewBookUseCase;
    public static final GetAllAuthorsUseCase getAllAuthorsUseCase;
    public static final Controller controller;

    static {
        LogManager.getLogger(App.class).info("init started...");

        mapper = new Mapper();

        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new AppException(e);
        }

        var dataSource = new PgDataSource();

//        var bookRepo = new MapBookRepository();
        var bookRepo = new PgBookRepository(dataSource);
        var authorRepo = new PgAuthorRepository(dataSource);

        var io = new ConsoleIO();

        var emailService = new StubEmailService(io);
//        var emailService = new YandexMailService();

        getAllBooksUseCase = new GetAllBooksUseCase(bookRepo);
        addNewBookUseCase = new AddNewBookUseCase(
                bookRepo,
                emailService);
        getAllAuthorsUseCase = new GetAllAuthorsUseCase(authorRepo);

        val bookController = new BookController(io, getAllBooksUseCase, addNewBookUseCase);
        var authorController = new AuthorController(io, getAllAuthorsUseCase);

        controller = new ConsoleController(
                io, bookController, authorController);
//        GetAllBooksAction.Factory.init(getAllBooksUseCase);
//        var controller = new TelegramBotController();

        LogManager.getLogger(App.class).info("init done!");
    }

    public static void main(String[] args) {
        controller.start();
    }

}

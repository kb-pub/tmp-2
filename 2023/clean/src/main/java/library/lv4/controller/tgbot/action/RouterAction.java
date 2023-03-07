package library.lv4.controller.tgbot.action;

import library.lv3.usecase.GetAllBooksUseCase;
import lombok.RequiredArgsConstructor;

import java.util.List;

public class RouterAction implements Action {
    private static RouterAction instance;
    public static RouterAction getInstance() {
        return instance;
    }

    public RouterAction(GetAllBooksUseCase getAllBooksUseCase) {
        this.getAllBooksUseCase = getAllBooksUseCase;
        instance = this;
    }

    private final GetAllBooksUseCase getAllBooksUseCase;
    @Override
    public Answer act(String chatId, String text) {
        var nextAction = switch (text) {
            case "book ls" -> new GetAllBooksAction(getAllBooksUseCase);
            default -> new EchoAction();
        };
        return new Answer(List.of(), nextAction, true);
    }
}

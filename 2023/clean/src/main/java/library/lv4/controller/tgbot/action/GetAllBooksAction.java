package library.lv4.controller.tgbot.action;

import library.lv3.usecase.GetAllBooksUseCase;
import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.ArrayList;

@RequiredArgsConstructor
public class GetAllBooksAction implements Action {
    private final GetAllBooksUseCase useCase;
    @Override
    public Answer act(String charId, String text) {
        var books = useCase.get().getBooks();
        var messages = new ArrayList<SendMessage>();
        if (books.isEmpty()) {
            messages.add(new SendMessage(charId, "no books in library"));
        }
        else {
            messages.add(new SendMessage(charId, "books in library:"));
            books.stream()
                    .map(b -> new SendMessage(
                            charId,
                            String.format("'%s', %s", b.getTitle(), b.getAuthor())))
                    .forEach(messages::add);
        }
        return new Answer(messages, Action.DEFAULT, false);
    }
}

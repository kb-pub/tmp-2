package library.lv3.usecase;

import library.lv0.crosscutting.Settings;
import library.lv2.spi.repo.BookRepository;
import library.lv2.spi.service.Email;
import library.lv2.spi.service.EmailService;
import library.lv3.usecase.dto.BookDto;
import library.lv3.usecase.dto.BookMapper;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AddNewBookUseCase {
    private final BookRepository bookRepository;
    private final EmailService emailService;

    public void add(BookDto book) {
        var newBook = bookRepository.save(BookMapper.fromDtoToNewBook(book));
        emailService.send(new Email(
                Settings.EMAIL_ADDRESS, "new Book added: " + newBook));
    }
}

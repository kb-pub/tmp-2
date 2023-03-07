package library.lv3.usecase;

import library.lv2.spi.repo.BookRepository;
import library.lv2.spi.repo.RepositoryAppException;
import library.lv2.spi.service.EmailService;
import library.lv3.usecase.dto.BookDto;
import library.lv3.usecase.dto.BookMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AddNewBookUseCaseTest {
    AddNewBookUseCase sut;
    BookRepository bookRepository;
    EmailService emailService;

    @BeforeEach
    void setUp() {
        bookRepository = mock(BookRepository.class);
        emailService = mock(EmailService.class);
        sut = new AddNewBookUseCase(bookRepository, emailService);
    }

    @Test
    void givenCorrectBook_whenAdd_thenSaveAndEmail() {
        var dto = new BookDto("t1", "a1");
        var book = BookMapper.fromDtoToNewBook(dto);
        var insertedBook = book.toBuilder().id(1).build();
        when(bookRepository.save(book)).thenReturn(insertedBook);

        sut.add(dto);

        verify(bookRepository).save(book);
        verify(emailService).send(any());
    }

    @Test
    void givenIncorrectBook_whenAdd_thenThrowsAndNoEmail() {
        var dto = new BookDto("t1", "a1");
        var book = BookMapper.fromDtoToNewBook(dto);
        when(bookRepository.save(book)).thenThrow(new RepositoryAppException("test"));

        assertThatExceptionOfType(RepositoryAppException.class)
                .isThrownBy(() -> sut.add(dto));

        verify(emailService, never()).send(any());
    }
}
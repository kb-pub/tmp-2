package library.lv1.entity;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Random;

import static org.assertj.core.api.Assertions.*;

class BookTest {

    @Test
    void givenNegativeId_whenNewOrder_thenThrows() {
        // given
        var ids = List.of(-1L, -15L, -1000000000000L, Long.MIN_VALUE);

        // when
        // then
        ids.forEach(id -> assertThatException()
                .isThrownBy(() -> new Book(id, "test", "test")));
    }

    @Test
    void givenNonNegativeId_whenNewOrder_thenSuccess() {
        // given
        var ids = List.of(0L, 1L, 15L, 1000000000000L, Long.MAX_VALUE);

        // when
        // then
        ids.forEach(id -> assertThatNoException()
                .isThrownBy(() -> new Book(id, "test", "test")));
    }

    @Test
    void getTitle() {
        var title = "test title " + new Random().nextInt();

        var sut = new Book(0, title, "test author");

        assertThat(sut.getTitle()).isEqualTo(title);
    }
}
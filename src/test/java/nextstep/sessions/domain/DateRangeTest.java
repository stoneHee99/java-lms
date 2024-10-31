package nextstep.sessions.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DateRangeTest {

    @DisplayName("종료일자가 시작일자보다 빠르면 예외가 잘 발생하는지")
    @Test
    void createTest_withEndDateBeforeStartDate() {
        assertThatThrownBy(() -> new DateRange(LocalDateTime.of(2021, 1, 1, 0, 0), LocalDateTime.of(2020, 1, 1, 0, 0)))
                .isInstanceOf(IllegalArgumentException.class);
    }
}

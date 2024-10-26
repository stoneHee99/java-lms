package nextstep.qna.domain;

import nextstep.qna.CannotDeleteException;
import nextstep.qna.ForbiddenException;
import nextstep.users.domain.NsUserTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AnswerTest {
    public static final Answer A1 = new Answer(NsUserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(NsUserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

    @DisplayName("사용자와 작성자가 다른 경우 삭제 시도 시 예외가 잘 발생하는지")
    @Test
    void delete_byNonWriter() {
        Answer answer = new Answer(NsUserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
        assertThrows(CannotDeleteException.class, () -> answer.delete(NsUserTest.SANJIGI));
    }

    @DisplayName("사용자와 작성자가 같은 경우 삭제 시도 시 정상적으로 삭제되는지")
    @Test
    void delete_byWriter() {
        Answer answer = new Answer(NsUserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
        answer.delete(NsUserTest.JAVAJIGI);
    }

    @DisplayName("이미 삭제되지 않은 답변은 삭제 이력으로 변환 시도시 예외가 잘 발생하는지")
    @Test
    void convertToDeleteHistory() {
        Answer answer = new Answer(NsUserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
        assertThrows(ForbiddenException.class, answer::toDeleteHistory);
    }

    @DisplayName("답변을 삭제하면 삭제 이력으로 변환되는지")
    @Test
    void convertToDeleteHistory_afterDelete() {
        Answer answer = new Answer(NsUserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
        answer.delete(NsUserTest.JAVAJIGI);
        assertThatNoException().isThrownBy(answer::toDeleteHistory);
    }
}

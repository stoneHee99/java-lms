package nextstep.qna.domain;

import nextstep.qna.CannotDeleteException;
import nextstep.qna.ForbiddenException;
import nextstep.users.domain.NsUserTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class QuestionTest {
    public static final Question Q1 = new Question(NsUserTest.JAVAJIGI, "title1", "contents1");
    public static final Question Q2 = new Question(NsUserTest.SANJIGI, "title2", "contents2");

    @DisplayName("질문 삭제 시 질문한 사람과 로그인한 사람이 같지 않은 경우 예외가 발생하는지")
    @Test
    void deleteTest_WhenUserIsNotOwner() {
        Question question = new Question(NsUserTest.JAVAJIGI, "title1", "contents1");
        assertThatThrownBy(() -> question.delete(NsUserTest.SANJIGI))
            .isInstanceOf(CannotDeleteException.class);
    }

    @DisplayName("질문 삭제 시 답변이 없는 경우 삭제가 가능한지")
    @Test
    void deleteTest_WhenAnswersEmpty() {
        Question question = new Question(NsUserTest.JAVAJIGI, "title1", "contents1");
        assertThatNoException()
                .isThrownBy(() -> question.delete(NsUserTest.JAVAJIGI));
    }

    @DisplayName("질문 삭제 시 답변글의 모든 답변자가 질문자인 경우 삭제가 가능한지")
    @Test
    void deleteTest_WhenAnswersAllOwner() {
        Question question = new Question(NsUserTest.JAVAJIGI, "title1", "contents1");
        question.addAnswer(new Answer(NsUserTest.JAVAJIGI, Q1, "Answers Contents1"));
        assertThatNoException()
            .isThrownBy(() -> question.delete(NsUserTest.JAVAJIGI));
    }

    @DisplayName("질문 삭제 시 다른 사람의 답변이 있는 경우 예외가 발생하는지")
    @Test
    void deleteTest_WhenAnswersNotEmpty() {
        Question question = new Question(NsUserTest.JAVAJIGI, "title1", "contents1");
        question.addAnswer(new Answer(NsUserTest.SANJIGI, Q1, "Answers Contents1"));
        assertThatThrownBy(() -> question.delete(NsUserTest.JAVAJIGI))
            .isInstanceOf(CannotDeleteException.class);
    }

    @DisplayName("질문 삭제 이력 생성 시 삭제되지 않은 질문인 경우 예외가 발생하는지")
    @Test
    void createDeleteHistoryTest_WhenNotDeleted() {
        Question question = new Question(NsUserTest.JAVAJIGI, "title1", "contents1");
        assertThatThrownBy(question::toDeleteHistories)
            .isInstanceOf(ForbiddenException.class);
    }

    @DisplayName("질문 삭제 이력 생성 시 삭제된 질문인 경우 삭제 이력이 생성되는지")
    @Test
    void createDeleteHistoryTest_WhenDeleted() {
        Question question = new Question(NsUserTest.JAVAJIGI, "title1", "contents1");
        question.delete(NsUserTest.JAVAJIGI);
        assertThatNoException()
            .isThrownBy(question::toDeleteHistories);
    }
}

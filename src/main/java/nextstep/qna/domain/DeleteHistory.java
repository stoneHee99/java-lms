package nextstep.qna.domain;

import nextstep.qna.ForbiddenException;
import nextstep.users.domain.NsUser;

import java.time.LocalDateTime;
import java.util.Objects;

public class DeleteHistory {
    private Long id;

    private ContentType contentType;

    private Long contentId;

    private NsUser deletedBy;

    private LocalDateTime createdDate = LocalDateTime.now();

    public DeleteHistory() {
    }

    public DeleteHistory(ContentType contentType, Long contentId, NsUser deletedBy, LocalDateTime createdDate) {
        this.contentType = contentType;
        this.contentId = contentId;
        this.deletedBy = deletedBy;
        this.createdDate = createdDate;
    }

    public DeleteHistory(Answer answer) {
        this(ContentType.ANSWER, answer.getId(), answer.getWriter(), LocalDateTime.now());
        if (!answer.isDeleted()) throw new ForbiddenException("삭제할 수 없는 답변입니다.");
    }

    public DeleteHistory(Question question) {
        this(ContentType.QUESTION, question.getId(), question.getWriter(), LocalDateTime.now());
        if (!question.isDeleted()) throw new ForbiddenException("삭제할 수 없는 질문입니다.");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeleteHistory that = (DeleteHistory) o;
        return Objects.equals(id, that.id) &&
                contentType == that.contentType &&
                Objects.equals(contentId, that.contentId) &&
                Objects.equals(deletedBy, that.deletedBy);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, contentType, contentId, deletedBy);
    }

    @Override
    public String toString() {
        return "DeleteHistory [id=" + id + ", contentType=" + contentType + ", contentId=" + contentId + ", deletedBy="
                + deletedBy + ", createdDate=" + createdDate + "]";
    }
}

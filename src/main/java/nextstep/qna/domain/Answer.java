package nextstep.qna.domain;

import nextstep.qna.CannotDeleteException;
import nextstep.qna.ForbiddenException;
import nextstep.qna.NotFoundException;
import nextstep.qna.UnAuthorizedException;
import nextstep.users.domain.NsUser;

import java.time.LocalDateTime;

public class Answer {
    private Long id;

    private NsUser writer;

    private Question question;

    private String contents;

    private boolean deleted = false;

    private LocalDateTime createdDate = LocalDateTime.now();

    private LocalDateTime updatedDate;


    public Answer(NsUser writer, Question question, String contents) {
        this(null, writer, question, contents);
    }

    public Answer(Long id, NsUser writer, Question question, String contents) {
        this.id = id;
        if(writer == null) {
            throw new UnAuthorizedException();
        }

        if(question == null) {
            throw new NotFoundException();
        }

        this.writer = writer;
        this.question = question;
        this.contents = contents;
    }

    public Long getId() {
        return id;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public NsUser getWriter() {
        return writer;
    }

    public void delete(NsUser user) {
        if (!isOwner(user)) {
            throw new CannotDeleteException("질문을 삭제할 권한이 없습니다.");
        }
        deleted = true;
    }

    public DeleteHistory toDeleteHistory() {
        if (!deleted) {
            throw new ForbiddenException("삭제되지 않은 답변은 삭제 이력으로 이동할 수 없습니다.");
        }
        return new DeleteHistory(ContentType.ANSWER, id, writer, LocalDateTime.now());
    }

    private boolean isOwner(NsUser writer) {
        return this.writer.equals(writer);
    }

    @Override
    public String toString() {
        return "Answer [id=" + getId() + ", writer=" + writer + ", contents=" + contents + "]";
    }
}

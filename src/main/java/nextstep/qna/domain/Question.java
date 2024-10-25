package nextstep.qna.domain;

import nextstep.qna.CannotDeleteException;
import nextstep.qna.ForbiddenException;
import nextstep.users.domain.NsUser;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Question {
    private Long id;

    private String title;

    private String contents;

    private NsUser writer;

    private Answers answers;

    private boolean deleted = false;

    private LocalDateTime createdDate = LocalDateTime.now();

    private LocalDateTime updatedDate;

    public Question(NsUser writer, String title, String contents) {
        this(0L, writer, title, contents);
    }

    public Question(Long id, NsUser writer, String title, String contents) {
        this.id = id;
        this.writer = writer;
        this.title = title;
        this.contents = contents;
        this.answers = new Answers();
    }

    public Long getId() {
        return id;
    }

    public NsUser getWriter() {
        return writer;
    }

    public void addAnswer(Answer answer) {
        answers.add(answer);
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void delete(NsUser user) {
        validateDeletable(user);
        setDeleteInfo(user);
    }

    private void validateDeletable(NsUser user) {
        if (!isOwner(user)) {
            throw new CannotDeleteException("질문을 삭제할 수 없습니다.");
        }
    }

    private void setDeleteInfo(NsUser user) {
        this.deleted = true;
        this.updatedDate = LocalDateTime.now();
        answers.delete(user);
    }

    public List<DeleteHistory> toDeleteHistories() {
        if(!deleted) {
            throw new ForbiddenException("질문이 삭제되지 않았습니다.");
        }
        List<DeleteHistory> deleteHistories = new ArrayList<>();
        deleteHistories.add(toDeleteHistory());
        deleteHistories.addAll(answers.toDeleteHistories());
        return deleteHistories;
    }

    private DeleteHistory toDeleteHistory() {
        return new DeleteHistory(ContentType.QUESTION, id, writer, LocalDateTime.now());
    }

    private boolean isOwner(NsUser loginUser) {
        return writer.equals(loginUser);
    }

    @Override
    public String toString() {
        return "Question [id=" + getId() + ", title=" + title + ", contents=" + contents + ", writer=" + writer + "]";
    }
}

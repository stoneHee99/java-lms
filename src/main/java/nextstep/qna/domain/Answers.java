package nextstep.qna.domain;

import nextstep.users.domain.NsUser;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class  Answers {

    private final List<Answer> answers;

    public Answers(List<Answer> answers) {
        this.answers = answers;
    }

    public Answers() {
        this(new ArrayList<>());
    }

    public void add(Answer answer) {
        answers.add(answer);
    }

    public void delete(NsUser user) {
        answers.forEach(answer -> answer.delete(user));
    }

    public List<DeleteHistory> toDeleteHistories() {
        return answers.stream()
                .map(Answer::toDeleteHistory)
                .collect(Collectors.toList());
    }
}

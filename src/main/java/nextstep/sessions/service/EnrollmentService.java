package nextstep.sessions.service;

import nextstep.sessions.domain.FreeSession;
import nextstep.sessions.domain.PaidSession;
import nextstep.sessions.domain.Session;
import nextstep.sessions.domain.SessionRepository;
import nextstep.payments.domain.Payment;
import nextstep.payments.service.PaymentService;
import nextstep.qna.NotFoundException;
import nextstep.users.domain.NsUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import static nextstep.sessions.domain.SessionType.FREE;
import static nextstep.sessions.domain.SessionType.PAID;

@Service("enrollmentService")
public class EnrollmentService {
    @Resource(name = "paymentService")
    private PaymentService paymentService;

    @Resource(name = "sessionRepository")
    private SessionRepository sessionRepository;

    @Transactional
    public void enrollSession(NsUser loginUser, long sessionId) {
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(NotFoundException::new);

        switch (session.getSessionType()) {
            case PAID:
                Payment payment = paymentService.getPaymentHistory(loginUser, sessionId);
                ((PaidSession) session).enroll(payment, loginUser);
                break;
            case FREE:
                ((FreeSession) session).enroll(loginUser);
                break;
        }

    }
}

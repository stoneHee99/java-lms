package nextstep.courses.service;

import nextstep.courses.domain.Session;
import nextstep.courses.domain.SessionRepository;
import nextstep.payments.domain.Payment;
import nextstep.payments.service.PaymentService;
import nextstep.qna.NotFoundException;
import nextstep.users.domain.NsUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

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

        Payment payment = paymentService.getPaymentHistory(loginUser, sessionId);

        session.enroll(payment, loginUser);
    }
}

package com.example.EmailAuthentication;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final EmailRepository emailRepository;
    private final EmailService emailService;

    public void sendCodeToEmail(String email) {
        VerificationCode createCode = createVerificationCode(email);

        String title = "Img Forest 이메일 인증 번호";

        String content = "<html>"
                +"<body>"
                +"<h1>ImgForest 인증 코드: " + createCode.getCode() + "</h1>"
                +"<p>해당 코를 홈페이지에 입력하세요.</p>"
                +"<footer style='color: grey; font-size: small;'>"
                +"<p>*본 메일은 자동응답 메일이므로 본 메일에 회신하지 마시기 바랍니다.</p>"
                +"</footer>"
                +"</body>"
                +"</html>";
        try {
            emailService.sendEmail(email, title, content);
        } catch (RuntimeException | MessagingException e) {
            e.printStackTrace();
            throw new RuntimeException("Unable to send email in sendCodeToEmail", e);
        }
    }

    public VerificationCode createVerificationCode(String email) {
        String randomCode = generateRandomCode(6);
        VerificationCode code = VerificationCode.builder()
                .email(email)
                .code(randomCode)
                .expiresTime(LocalDateTime.now().plusDays(1))
                .build();

        return emailRepository.save(code);
    }

    public String generateRandomCode(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder();
        ThreadLocalRandom random = ThreadLocalRandom.current();

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            sb.append(characters.charAt(index));
        }

        return sb.toString();
    }

    public boolean verifyCode(String email, String code) {
        return emailRepository.findByEmailAndCode(email, code)
                .map(vc -> vc.getExpiresTime().isAfter(LocalDateTime.now()))
                .orElse(false);
    }

    @Transactional
    @Scheduled(cron = "0 0 12 * * ?") //매일 정오에 해당 만료 코드 삭제
    public void deleteExpiredVerificationCodes() {
        emailRepository.deleteByExpiresTimeBefore(LocalDateTime.now());
    }
}

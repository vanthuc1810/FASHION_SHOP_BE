package com.example.FashionShop.Services;

import com.example.FashionShop.Dto.request.EmailSenderRequest;
import com.example.FashionShop.Dto.response.ApiResponse;
import com.example.FashionShop.Entity.User;
import com.example.FashionShop.Enum.ErrorCode;
import com.example.FashionShop.Exception.AppException;
import com.example.FashionShop.Repository.UserRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EmailService {
    JavaMailSender mailSender;
    UserRepository userRepository;

    @NonFinal
    @Value("${jwt.secret}")
    protected String SIGNER_KEY;

    public void sendHtmlEmail(EmailSenderRequest request) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        User user = userRepository.findByEmail(request.getTo()).orElseThrow(() -> new AppException(ErrorCode.USER_NOTFOUND));

        String token = genToken(user.getUserName());

        helper.setTo(request.getTo());
        helper.setSubject("Bạn vừa yêu cầu cập đổi mật khẩu???");
        String htmlContent = "<a href=\"http://localhost:3000/confirm-update-password/"+ token  +"\"\n" +
                "       style=\"display:inline-block;padding:10px 20px;background-color:#28a745;color:#fff;text-decoration:none;border-radius:5px;\">\n" +
                "      Xác nhận\n" +
                "    </a>";
        helper.setText(htmlContent, true);

        mailSender.send(message);
    }

    public String genToken(String username) {
        Optional<User> user = userRepository.findByUserName(username);
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.get().getIdUser().toString())
                .issueTime(new Date())
                .expirationTime(new Date(Instant.now().plus(1, ChronoUnit.DAYS).toEpochMilli()))
                .claim("scope", user.get().getRole())
                .build();
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(header, payload);

        //        Ky token(Thuat toan ky, )
        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
    }

}

package com.example.FashionShop.IServices;

import com.example.FashionShop.Dto.request.EmailSenderRequest;
import jakarta.mail.MessagingException;

public interface IEmailService {
    void sendHtmlEmail(EmailSenderRequest request) throws MessagingException;
}

package com.example.FashionShop.Services;

import com.example.FashionShop.Dto.request.GreetingRequest;
import com.example.FashionShop.Dto.response.GreetingResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.time.LocalDateTime;

@Service
public class ChatService {

    public GreetingResponse greeting(GreetingRequest greetingRequest) throws InterruptedException {
        Thread.sleep(1000); // simulated delay
        GreetingResponse greetingResponse = new GreetingResponse()
                .builder()
                .sendTime(LocalDateTime.now())
                .senderID(greetingRequest.getSenderID())
                .build();
        return greetingResponse;
    }
}

package com.example.FashionShop.Controller;

import com.example.FashionShop.Dto.request.GreetingRequest;
import com.example.FashionShop.Dto.request.Message;
import com.example.FashionShop.Dto.request.MessageImg;
import com.example.FashionShop.Dto.response.GreetingResponse;
import com.example.FashionShop.Dto.response.MessageImgResponse;
import com.example.FashionShop.Dto.response.MessageResponse;
import com.example.FashionShop.Entity.User;
import com.example.FashionShop.Enum.ErrorCode;
import com.example.FashionShop.Enum.Role;
import com.example.FashionShop.Exception.AppException;
import com.example.FashionShop.Repository.UserRepository;
import com.example.FashionShop.Services.ChatService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ChatController {
    ChatService chatService;
    SimpMessagingTemplate simpMessagingTemplate;
    UserRepository userRepository;
    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public GreetingResponse greeting(@Payload GreetingRequest greetingRequest) throws Exception {
        return chatService.greeting(greetingRequest);
    }

    @MessageMapping("/greating")
    public void greating(@Payload GreetingRequest greetingRequest) throws Exception {
        Thread.sleep(1000); // simulated delay
        User user = userRepository.findById(Integer.parseInt(greetingRequest.getSenderID())).orElseThrow(() -> new AppException(ErrorCode.USER_NOTFOUND));
        if(!user.getRole().equals(Role.CHAT.name()))
        {
            List<User> listAdmin = userRepository.findAllByRole(Role.CHAT.name());
            for (User u : listAdmin) {
                if (u.isAvailable()) {
                    MessageResponse messageResponse = MessageResponse
                            .builder()
                            .reciveID(u.getIdUser().toString())
                            .senderID(greetingRequest.getSenderID())
                            .message("Make a conversation!")
                            .build();
                    simpMessagingTemplate.convertAndSendToUser(u.getIdUser().toString(), "/greating", messageResponse);

                    break; // Thoát khỏi vòng lặp ngay khi tìm thấy user đầu tiên thỏa mãn điều kiện
                }
            }

        }
    }
    @MessageMapping("/chat")
    public void chat(@Payload Message message) throws Exception {
        Thread.sleep(1000); // simulated delay
        User user = userRepository.findById(Integer.parseInt(message.getSenderID())).orElseThrow(() -> new AppException(ErrorCode.USER_NOTFOUND));
        MessageResponse messageResponse = MessageResponse
                .builder()
                .reciveID(message.getReciveID())
                .senderID(message.getSenderID())
                .message(message.getMessage())
                .time(LocalDateTime.now())
                .build();
        simpMessagingTemplate.convertAndSendToUser(message.getReciveID(), "/private", messageResponse);
    }
    @MessageMapping("/chatImage")
    public void chatImage(@Payload MessageImg message) throws Exception {
        System.out.println("GET IMG");
        Thread.sleep(1000); // simulated delay
        User user = userRepository.findById(Integer.parseInt(message.getSenderID())).orElseThrow(() -> new AppException(ErrorCode.USER_NOTFOUND));
        MessageImgResponse messageResponse = MessageImgResponse
                .builder()
                .reciveID(message.getReciveID())
                .senderID(message.getSenderID())
                .time(LocalDateTime.now())
                .imgs(message.getImgs())
                .build();
        simpMessagingTemplate.convertAndSendToUser(message.getReciveID(), "/image", messageResponse);
    }

    @MessageMapping("/close")
    public void closeUser(@Payload Message message) throws Exception {
        Thread.sleep(1000); // simulated delay
        User user = userRepository.findById(Integer.parseInt(message.getSenderID())).orElseThrow(() -> new AppException(ErrorCode.USER_NOTFOUND));
        MessageResponse messageResponse = MessageResponse
                .builder()
                .reciveID(message.getReciveID())
                .senderID(message.getSenderID())
                .message("Close user!!!")
                .time(LocalDateTime.now())
                .build();
        simpMessagingTemplate.convertAndSendToUser(message.getReciveID(), "/close", messageResponse);
    }
    @MessageMapping("/accept")
    public void acceptChat(@Payload Message message) throws Exception {
        Thread.sleep(1000); // simulated delay
        MessageResponse messageResponse = MessageResponse
                .builder()
                .reciveID(message.getReciveID())
                .senderID(message.getSenderID())
                .message("Accept chat!!")
                .time(LocalDateTime.now())
                .build();
        simpMessagingTemplate.convertAndSendToUser(message.getReciveID(), "/accept", messageResponse);
    }
    @PostMapping("/accept")
    public void acceptChat() {
        var context = SecurityContextHolder.getContext();
        Integer idUser = Integer.parseInt(context.getAuthentication().getName());
        User user = userRepository.findById(idUser).orElseThrow(() -> new AppException(ErrorCode.USER_NOTFOUND));
        user.setAvailable(false);
        userRepository.save(user);
    }

}
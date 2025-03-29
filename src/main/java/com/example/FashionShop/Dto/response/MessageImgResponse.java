package com.example.FashionShop.Dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class MessageImgResponse {
    String senderID;
    String reciveID;
    List<String> imgs;
    LocalDateTime time;
}

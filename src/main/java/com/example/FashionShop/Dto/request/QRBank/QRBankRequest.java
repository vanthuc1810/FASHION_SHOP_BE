package com.example.FashionShop.Dto.request.QRBank;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QRBankRequest {
    private String accountNo;
    private String accountName;
    private String acqId;
    private String addInfo;
    private String amount;
    private String template;
}

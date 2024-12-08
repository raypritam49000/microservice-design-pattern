package com.notification.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderInformation {
    private String orderId;
    private String userId;
    private Date createdDate;
    private String price;
    private String emailId;
    private String userPhone;
}

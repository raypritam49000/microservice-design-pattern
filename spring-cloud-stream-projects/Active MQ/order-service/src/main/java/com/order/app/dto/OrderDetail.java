package com.order.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetail implements Serializable {
    private String orderId;
    private String userId;
    private Date createdDate;
    private String price;
    private String emailId;
    private String userPhone;
    private boolean orderPaymentStatus = false;
    private boolean orderStatus = false;
    private String courseId;
}
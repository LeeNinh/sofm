package com.example.demoapi.dto.coupon;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CouponDTO {
    private int id;

    private String couponCode;

    private double discountAmount;

    private String expiredDate;
}

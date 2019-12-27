package com.vouchers.controller;

import com.vouchers.constant.Endpoint;
import com.vouchers.model.ApiResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;

import static com.vouchers.constant.Request.DISCOUNT_PERCENTAGE;
import static com.vouchers.constant.Request.EMAIL;
import static com.vouchers.constant.Request.EXPIRY_DATE;
import static com.vouchers.constant.Request.SPECIAL_OFFER;
import static com.vouchers.constant.Request.VOUCHER_CODE;

@Api
public interface VoucherServiceApi {

    @ApiOperation("Create voucher codes for all recipients on the new special offer.")
    @PostMapping(
            value = Endpoint.VOUCHERS_ENDPOINT,
            produces = {"application/json"})
    ApiResponse createVoucherCodes(@RequestParam(SPECIAL_OFFER) final String specialOffer,
                                   @RequestParam(DISCOUNT_PERCENTAGE) final Double percentageDiscount,
                                   @RequestParam(EXPIRY_DATE + " (e.g. 12/12/2020)") final Date expiryDate);

    @ApiOperation("Validate voucher code tied to a recipient's email is valid and return percentage discount.")
    @PutMapping(
            value = Endpoint.VOUCHERS_ENDPOINT,
            produces = {"application/json"}
    )
    ApiResponse useVoucherCode(@RequestParam(EMAIL) final String email,
                               @RequestParam(VOUCHER_CODE) final String voucherCode);

    @ApiOperation("Get voucher codes tied to a recipient's email.")
    @GetMapping(
            value = Endpoint.VOUCHERS_ENDPOINT,
            produces = {"application/json"}
    )
    ApiResponse getVoucherCodes(@RequestParam(EMAIL) final String email);

}

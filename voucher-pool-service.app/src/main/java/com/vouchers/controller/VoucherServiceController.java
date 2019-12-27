package com.vouchers.controller;

import com.vouchers.constant.Response;
import com.vouchers.model.ApiResponse;
import com.vouchers.service.VoucherPoolService;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

@RestController
public class VoucherServiceController implements VoucherServiceApi {

	private final VoucherPoolService voucherPoolService;

	VoucherServiceController(final VoucherPoolService voucherPoolService) {
		this.voucherPoolService = voucherPoolService;
	}

	@Override
	public ApiResponse createVoucherCodes(final String specialOffer,
										  final Double discountPercentage,
										  final Date expiryDate) {
		final LocalDate expiryDateLocal = expiryDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		return new ApiResponse(
				voucherPoolService.createVoucherCodes(specialOffer, discountPercentage, expiryDateLocal.toString()),
				Response.STATUS_SUCCESS,
				"Vouchers created.");
	}

	@Override
	public ApiResponse useVoucherCode(final String email,
									  final String voucherCode) {
		return new ApiResponse(
				voucherPoolService.useVoucherCode(email, voucherCode),
				Response.STATUS_SUCCESS,
				"Voucher code used.");
	}

	@Override
	public ApiResponse getVoucherCodes(final String email) {
		return new ApiResponse(
				voucherPoolService.getVoucherCodes(email),
				Response.STATUS_SUCCESS,
				"Special Offers listed by Voucher Code.");
	}

}

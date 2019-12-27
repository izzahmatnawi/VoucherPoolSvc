package com.vouchers.service;

import java.util.Map;

public interface VoucherPoolService {

    Map<String, Object> createVoucherCodes(final String specialOfferName,
                                           final Double discount,
                                           final String expiryDate);
    Map<String, Object> useVoucherCode(final String email, final String voucherCode);
    Map<String, Object> getVoucherCodes(final String email);
}

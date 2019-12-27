package com.vouchers.repository;

import com.vouchers.model.Voucher;

import java.util.List;
import java.util.Set;

public interface VoucherRepository {

    Voucher getVoucherByCodeAndNoUsageDate(final String code);
    List<Voucher> getVouchersByCodesAndNoUsageDate(final Set<String> codes);
    void updateUsageDateByCode(final String code, final String usageDate);
    void insertVoucher(final Voucher voucher);
}

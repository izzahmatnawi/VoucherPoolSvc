package com.vouchers.model;

public class Voucher {

    private final String code;
    private final String specialOfferName;
    private final String usageDate;

    public Voucher(final String code, final String specialOfferName, final String usageDate) {
        this.code = code;
        this.specialOfferName = specialOfferName;
        this.usageDate = usageDate;
    }

    public String getCode() {
        return code;
    }

    public String getSpecialOfferName() {
        return specialOfferName;
    }

    public String getUsageDate() {
        return usageDate;
    }
}

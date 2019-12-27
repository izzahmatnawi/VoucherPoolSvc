package com.vouchers.model;

import java.util.HashSet;
import java.util.Set;

public class Recipient {

    private final String email;
    private final String name;
    private final Set<String> voucherCodes;

    public Recipient(final String email, final String name, final Set<String> voucherCodes) {
        this.email = email;
        this.name = name;
        this.voucherCodes = voucherCodes;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public Set<String> getVoucherCodes() {
        return voucherCodes == null? new HashSet<>() : voucherCodes;
    }
}

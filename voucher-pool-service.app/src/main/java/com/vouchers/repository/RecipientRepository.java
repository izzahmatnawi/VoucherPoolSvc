package com.vouchers.repository;

import com.vouchers.model.Recipient;

import java.util.List;
import java.util.Set;

public interface RecipientRepository {

    List<Recipient> getAllRecipients();
    Recipient getRecipientByEmail(final String email);
    void UpdateVoucherCodesByEmail(final String email, final Set<String> voucherCodes);
}

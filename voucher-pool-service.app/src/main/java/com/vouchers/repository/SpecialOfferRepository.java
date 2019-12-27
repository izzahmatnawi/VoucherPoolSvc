package com.vouchers.repository;

import com.vouchers.model.SpecialOffer;

public interface SpecialOfferRepository {

    SpecialOffer getSpecialOfferByName(final String name);
    void insertSpecialOffer(final SpecialOffer specialOffer);
}

package com.vouchers.model;

import java.util.Objects;

public class SpecialOffer {

    private final String name;
    private final String expiryDate;
    private final Double discount;

    public SpecialOffer(final String name, final String expiryDate, final Double discount) {
        this.name = name;
        this.expiryDate = expiryDate;
        this.discount = discount;
    }

    public String getName() {
        return name;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public Double getDiscount() {
        return discount;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final SpecialOffer that = (SpecialOffer) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(expiryDate, that.expiryDate) &&
                Objects.equals(discount, that.discount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, expiryDate, discount);
    }
}

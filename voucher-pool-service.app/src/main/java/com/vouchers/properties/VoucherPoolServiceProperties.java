package com.vouchers.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
@ConfigurationProperties(prefix = "vps")
public class VoucherPoolServiceProperties {

    private String recipientCollectionName = "recipient";
    private String voucherCollectionName = "voucher";
    private String specialOfferCollectionName = "specialOffer";
    private String mongoUri;

    public String getRecipientCollectionName() {
        return recipientCollectionName;
    }

    public void setRecipientCollectionName(String recipientCollectionName) {
        this.recipientCollectionName = recipientCollectionName;
    }

    public String getVoucherCollectionName() {
        return voucherCollectionName;
    }

    public void setVoucherCollectionName(String voucherCollectionName) {
        this.voucherCollectionName = voucherCollectionName;
    }

    public String getSpecialOfferCollectionName() {
        return specialOfferCollectionName;
    }

    public void setSpecialOfferCollectionName(String specialOfferCollectionName) {
        this.specialOfferCollectionName = specialOfferCollectionName;
    }

    public String getMongoUri() {
        return mongoUri;
    }

    public void setMongoUri(String mongoUri) {
        this.mongoUri = mongoUri;
    }
}

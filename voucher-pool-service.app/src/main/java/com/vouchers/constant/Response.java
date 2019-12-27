package com.vouchers.constant;

public final class Response {

    public static final String STATUS_SUCCESS = "success";
    public static final String STATUS_FAILED = "failed";

    public static final String DETAIL_OFFER = "Special Offer";
    public static final String DETAIL_OFFER_RECIPIENTS = "No. of Recipients To Receive Offer";
    public static final String DETAIL_VOUCHER = "Voucher";
    public static final String DETAIL_RECIPIENTS = "Recipient";

    public static final String ERROR_NO_OFFER_CREATED = "No recipients found to receive special offer. No special offer is created.";
    public static final String ERROR_RECIPIENT_BY_EMAIL_NOT_FOUND = "No recipients found for email(s): ";
    public static final String ERROR_RECIPIENT_VOUCHER_BY_EMAIL_NOT_FOUND = "No valid voucher code(s) found for recipient with email: ";
    public static final String ERROR_VOUCHER_NOT_FOUND = "Voucher code not found: ";
    public static final String ERROR_SPECIAL_OFFER_NOT_FOUND = "Special Offer for Voucher code not found: ";
    public static final String ERROR_VOUCHER_EMAIL_INVALID = "Invalid voucher code for email: ";
    public static final String ERROR_VOUCHER_EXPIRED = "Voucher code has expired.";
    public static final String ERROR_INTERNAL = "Internal error encountered. Please contact service administrator.";

    private Response() {
    }
}

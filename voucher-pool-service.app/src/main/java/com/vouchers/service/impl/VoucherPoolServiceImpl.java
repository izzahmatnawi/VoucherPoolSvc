package com.vouchers.service.impl;

import com.vouchers.constant.Response;
import com.vouchers.exception.NotFoundException;
import com.vouchers.exception.UnauthorizedException;
import com.vouchers.model.Recipient;
import com.vouchers.model.SpecialOffer;
import com.vouchers.model.Voucher;
import com.vouchers.repository.RecipientRepository;
import com.vouchers.repository.SpecialOfferRepository;
import com.vouchers.repository.VoucherRepository;
import com.vouchers.service.VoucherPoolService;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@Service
public class VoucherPoolServiceImpl implements VoucherPoolService {

    private final VoucherRepository voucherRepository;
    private final RecipientRepository recipientRepository;
    private final SpecialOfferRepository specialOfferRepository;

    public VoucherPoolServiceImpl(final VoucherRepository voucherRepository,
                                  final RecipientRepository recipientRepository,
                                  final SpecialOfferRepository specialOfferRepository) {
        this.voucherRepository = voucherRepository;
        this.recipientRepository = recipientRepository;
        this.specialOfferRepository = specialOfferRepository;
    }

    @Override
    public Map<String, Object> createVoucherCodes(final String specialOfferName,
                                                  final Double discount,
                                                  final String expiryDate) {
        final SpecialOffer specialOffer = new SpecialOffer(specialOfferName, expiryDate, discount);
        final List<Recipient> recipients = getAllReceipientsToReceiveSpecialOffer();

        specialOfferRepository.insertSpecialOffer(specialOffer);
        recipients.stream().forEach(r -> {
            final Voucher voucher = new Voucher(UUID.randomUUID().toString(), specialOfferName, null);
            voucherRepository.insertVoucher(voucher);

            r.getVoucherCodes().add(voucher.getCode());
            recipientRepository.UpdateVoucherCodesByEmail(r.getEmail(), r.getVoucherCodes());
        });

        final Map<String, Object> details = new HashMap<>();
        details.put(Response.DETAIL_OFFER, specialOffer);
        details.put(Response.DETAIL_OFFER_RECIPIENTS, recipients.size());
        return details;
    }

    @Override
    public Map<String, Object> getVoucherCodes(final String email) {
        final Recipient recipient = getReceipientByEmail(email);
        final LocalDate dateNow = LocalDate.now();
        final List<Voucher> vouchers = getVouchers(recipient.getVoucherCodes(), email);
        final List<Voucher> validVouchers = vouchers.stream().filter(v -> {
            final SpecialOffer specialOffer = specialOfferRepository.getSpecialOfferByName(v.getSpecialOfferName());
            final LocalDate expiryDate = LocalDate.parse(specialOffer.getExpiryDate());
            return dateNow.isBefore(expiryDate);
        }).collect(Collectors.toList());

        if(validVouchers.isEmpty()) {
            throw new NotFoundException(Response.ERROR_RECIPIENT_VOUCHER_BY_EMAIL_NOT_FOUND + email);
        }
        return validVouchers.stream().collect(Collectors.toMap(Voucher::getCode, Voucher::getSpecialOfferName));
    }

    @Override
    public Map<String, Object> useVoucherCode(final String email, final String voucherCode) {
        final Voucher voucher = getVoucherByCode(voucherCode);
        final SpecialOffer specialOffer = getSpecialOfferByName(voucher.getSpecialOfferName());
        final Recipient recipient = getReceipientByEmail(email);

        final Set<String> voucherCodes = recipient.getVoucherCodes();
        if(!voucherCodes.contains(voucherCode)) {
            throw new UnauthorizedException(Response.ERROR_VOUCHER_EMAIL_INVALID + email);
        }

        final LocalDate todayDate = LocalDate.now();
        final LocalDate expiryDate = LocalDate.parse(specialOffer.getExpiryDate());
        if(todayDate.isAfter(expiryDate)) {
            throw new UnauthorizedException(Response.ERROR_VOUCHER_EXPIRED);
        }
        voucherRepository.updateUsageDateByCode(voucherCode, todayDate.toString());

        final Map<String, Object> details = new HashMap<>();
        details.put(Response.DETAIL_OFFER, specialOffer);
        details.put(Response.DETAIL_VOUCHER, voucher);
        details.put(Response.DETAIL_RECIPIENTS, recipient);
        return details;
    }

    private List<Voucher> getVouchers(final Set<String> voucherCodes, final String email) {
        final List<Voucher> vouchers = voucherRepository.getVouchersByCodesAndNoUsageDate(voucherCodes);
        if(vouchers == null || vouchers.isEmpty()) {
            throw new NotFoundException(Response.ERROR_RECIPIENT_VOUCHER_BY_EMAIL_NOT_FOUND + email);
        }
        return vouchers;
    }

    private Recipient getReceipientByEmail(final String email) {
        final Recipient recipient = recipientRepository.getRecipientByEmail(email);
        if(recipient == null) {
            throw new NotFoundException(Response.ERROR_RECIPIENT_BY_EMAIL_NOT_FOUND + email);
        }
        return recipient;
    }

    private List<Recipient> getAllReceipientsToReceiveSpecialOffer() {
        final List<Recipient> recipients = recipientRepository.getAllRecipients();
        if (recipients == null || recipients.isEmpty()) {
            throw new NotFoundException(Response.ERROR_NO_OFFER_CREATED);
        }
        return recipients;
    }

    private Voucher getVoucherByCode(final String voucherCode) {
        final Voucher voucher = voucherRepository.getVoucherByCodeAndNoUsageDate(voucherCode);
        if (voucher == null) {
            throw new NotFoundException(Response.ERROR_VOUCHER_NOT_FOUND + voucherCode);
        }
        return voucher;
    }

    private SpecialOffer getSpecialOfferByName(final String name) {
        final SpecialOffer specialOffer = specialOfferRepository.getSpecialOfferByName(name);
        if (specialOffer == null) {
            throw new NotFoundException(Response.ERROR_SPECIAL_OFFER_NOT_FOUND + name);
        }
        return specialOffer;
    }
}

package com.vouchers.service;

import com.vouchers.exception.NotFoundException;
import com.vouchers.model.Recipient;
import com.vouchers.model.SpecialOffer;
import com.vouchers.model.Voucher;
import com.vouchers.repository.RecipientRepository;
import com.vouchers.repository.SpecialOfferRepository;
import com.vouchers.repository.VoucherRepository;
import com.vouchers.service.impl.VoucherPoolServiceImpl;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import junitparams.naming.TestCaseName;
import org.assertj.core.api.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(JUnitParamsRunner.class)
public class VoucherPoolServiceImplTest {
    //TODO: add the rest of the test case
    private static final Recipient SAMPLE_RECIPIENT1 = new Recipient("a@a.com", "a",
            new HashSet<>(Arrays.asList(UUID.randomUUID().toString(), UUID.randomUUID().toString())));
    private static final Recipient SAMPLE_RECIPIENT2 = new Recipient("b@b.com", "b",
            new HashSet<>(Arrays.asList(UUID.randomUUID().toString(), UUID.randomUUID().toString())));
    private static final List<Recipient> SAMPLE_RECIPIENTS = Arrays.asList(SAMPLE_RECIPIENT1, SAMPLE_RECIPIENT2);
    private static final SpecialOffer SP_OFFER_SAMPLE = new SpecialOffer("12% off", "15/12/2021", 12.0);

    private VoucherRepository voucherRepository;
    private RecipientRepository recipientRepository;
    private SpecialOfferRepository specialOfferRepository;
    private VoucherPoolService service;

    @Before
    public void setup() {
        voucherRepository = mock(VoucherRepository.class);
        recipientRepository = mock(RecipientRepository.class);
        specialOfferRepository = mock(SpecialOfferRepository.class);
        service = new VoucherPoolServiceImpl(voucherRepository, recipientRepository, specialOfferRepository);
    }

    @Test
    public void createVoucherCodes_recipientsReturned_vouchersInserted() {
        final List<Recipient> existingRecipients = SAMPLE_RECIPIENTS;
        final SpecialOffer inputSpecialOffer = SP_OFFER_SAMPLE;

        Mockito.when(recipientRepository.getAllRecipients()).thenReturn(existingRecipients);
        service.createVoucherCodes(inputSpecialOffer.getName(), inputSpecialOffer.getDiscount(), inputSpecialOffer.getExpiryDate());

        ArgumentCaptor<Voucher> voucherCaptor = ArgumentCaptor.forClass(Voucher.class);
        ArgumentCaptor<Set> voucherCodesCaptor = ArgumentCaptor.forClass(Set.class);
        ArgumentCaptor<String> emailCaptor = ArgumentCaptor.forClass(String.class);

        verify(specialOfferRepository, times(1)).insertSpecialOffer(inputSpecialOffer);
        verify(voucherRepository, times(existingRecipients.size())).insertVoucher(voucherCaptor.capture());
        verify(recipientRepository, times(existingRecipients.size())).UpdateVoucherCodesByEmail(emailCaptor.capture(),
                voucherCodesCaptor.capture());

        List<Voucher> capturedVouchers = voucherCaptor.getAllValues();
        assertThat(capturedVouchers).allSatisfy(voucher -> voucher.getSpecialOfferName().equals(inputSpecialOffer.getName()));

        List<String> emailsCaptured = emailCaptor.getAllValues();
        List<String> emailsExpected = existingRecipients.stream().map(Recipient::getEmail).collect(Collectors.toList());
        assertThat(emailsCaptured).isEqualTo(emailsExpected);

        Set<String> createdVoucherCodes = capturedVouchers.stream().map(Voucher::getCode).collect(Collectors.toSet());
        Set<String> existingRecipientsVoucherCodes = existingRecipients.stream().flatMap(r -> r.getVoucherCodes().stream())
                .collect(Collectors.toSet());
        createdVoucherCodes.addAll(existingRecipientsVoucherCodes);

        Set<String> capturedVoucherCodes = voucherCodesCaptor.getAllValues().stream()
                .flatMap(s -> ((Set<String>) s).stream())
                .collect(Collectors.toSet());
        assertThat(createdVoucherCodes).isEqualTo(capturedVoucherCodes);
    }

    @Test
    public void createVoucherCodes_noRecipientsReturned_noVouchersInserted() {
        final List<Recipient> existingRecipients = Collections.emptyList();
        final SpecialOffer inputSpecialOffer = SP_OFFER_SAMPLE;

        Mockito.when(recipientRepository.getAllRecipients()).thenReturn(existingRecipients);
        assertThatExceptionOfType(NotFoundException.class).isThrownBy(() ->
                service.createVoucherCodes(inputSpecialOffer.getName(),
                        inputSpecialOffer.getDiscount(),
                        inputSpecialOffer.getExpiryDate()));

        verify(specialOfferRepository, times(0)).insertSpecialOffer(any());
        verify(voucherRepository, times(0)).insertVoucher(any());
        verify(recipientRepository, times(0)).UpdateVoucherCodesByEmail(any(), any());
    }
}

package com.vouchers.repository;

import com.vouchers.model.Recipient;
import com.vouchers.properties.VoucherPoolServiceProperties;
import com.vouchers.repository.impl.MongoRecipientRepository;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import junitparams.naming.TestCaseName;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

@RunWith(JUnitParamsRunner.class)
public class MongoRecipientRepositoryTest {

    private static final String RECIPIENT_COLLECTION_NAME = "rec";
    private static final String VALID_EMAIL_SAMPLE = "recipient@test.com";
    private static final Set<String> VALID_VOUCHER_CODES_SAMPLE = new HashSet<>(Arrays.asList(
            UUID.randomUUID().toString(),
            UUID.randomUUID().toString()));
    private static final Query VALID_EMAIL_QUERY = new Query(Criteria.where("email").is(VALID_EMAIL_SAMPLE));

    private MongoTemplate mongoTemplate;
    private MongoRecipientRepository repo;

    @Before
    public void setup() {
        final VoucherPoolServiceProperties vpsProperties = mock(VoucherPoolServiceProperties.class);
        when(vpsProperties.getRecipientCollectionName()).thenReturn(RECIPIENT_COLLECTION_NAME);
        mongoTemplate = mock(MongoTemplate.class);
        repo = new MongoRecipientRepository(mongoTemplate, vpsProperties);
    }


    @Test
    public void getAllRecipients_findAllCalled1Time() {
        repo.getAllRecipients();
        verify(mongoTemplate, times(1)).findAll(Recipient.class, RECIPIENT_COLLECTION_NAME);
    }

    @Test
    public void getRecipientByEmail_givenEmail_thenFindOneIsCalled() {
        repo.getRecipientByEmail(VALID_EMAIL_SAMPLE);
        verify(mongoTemplate, times(1)).findOne(
                eq(VALID_EMAIL_QUERY),
                eq(Recipient.class),
                eq(RECIPIENT_COLLECTION_NAME));
    }

    @Test
    @Parameters(method = "invalidEmailsTestData")
    @TestCaseName("{0}")
    public void getRecipientByEmail_givenNoEmail_thenFindOneIsNotCalled(final String testCase,
                                                                        final String email) {
        repo.getRecipientByEmail(email);
        verify(mongoTemplate, times(0)).findOne(
                isA(Query.class),
                eq(Recipient.class),
                eq(RECIPIENT_COLLECTION_NAME));
    }

    @Test
    @Parameters(method = "validVoucherCodesTestData")
    @TestCaseName("{0}")
    public void updateVoucherCodesByEmail_givenValidEmail_theUpdateOneIsCalled(final String testCase,
                                                                               final Set<String> voucherCodes) {
        repo.UpdateVoucherCodesByEmail(VALID_EMAIL_SAMPLE, voucherCodes);
        verify(mongoTemplate, times(1)).updateFirst(
                eq(VALID_EMAIL_QUERY),
                isA(Update.class),
                eq(Recipient.class),
                eq(RECIPIENT_COLLECTION_NAME));
    }


    @Test
    @Parameters(method = "invalidEmailsTestData")
    @TestCaseName("{0}")
    public void updateVoucherCodesByEmail_givenNoEmail_theUpdateOneIsNotCalled(final String testCase,
                                                                               final String email) {
        repo.UpdateVoucherCodesByEmail(email, VALID_VOUCHER_CODES_SAMPLE);
        verify(mongoTemplate, times(0)).updateFirst(
                isA(Query.class),
                isA(Update.class),
                eq(Recipient.class),
                eq(RECIPIENT_COLLECTION_NAME));
    }

    private Object[] invalidEmailsTestData() {
        return new Object[][] {
                {"empty email", ""},
                {"null email", null}
        };
    }

    private Object[] validVoucherCodesTestData() {
        return new Object[][] {
                {"non-empty voucher code set", VALID_VOUCHER_CODES_SAMPLE},
                {"empty voucher code set", Collections.emptySet()},
                {"null voucher code set", null}
        };
    }
}

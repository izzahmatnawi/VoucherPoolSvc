package com.vouchers.repository;

import com.vouchers.model.Voucher;
import com.vouchers.properties.VoucherPoolServiceProperties;
import com.vouchers.repository.impl.MongoVoucherRepository;
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

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(JUnitParamsRunner.class)
public class MongoVoucherRepositoryTest {

    private static final String VOUCHER_COLLECTION_NAME = "voucher";

    private static final String VALID_CODE_SAMPLE = UUID.randomUUID().toString();
    private static final String VALID_USAGE_DATE = "12/12/2020";
    private static final Voucher VALID_VOUCHER = new Voucher(VALID_CODE_SAMPLE, "20% off", null);
    private static final Query VALID_CODE_QUERY = new Query(Criteria.where("code").is(VALID_CODE_SAMPLE));
    private static final Query VALID_CODE_UNUSED_QUERY = new Query(Criteria.where("code").is(VALID_CODE_SAMPLE)
            .andOperator(Criteria.where("usageDate").exists(false)));

    private static final Set<String> VALID_CODES_SAMPLE = new HashSet<>(Arrays.asList(
            UUID.randomUUID().toString(),
            UUID.randomUUID().toString()));
    private static final Query VALID_CODES_UNUSED_QUERY = new Query(Criteria.where("code").in(VALID_CODES_SAMPLE)
            .andOperator(Criteria.where("usageDate").exists(false)));

    private MongoTemplate mongoTemplate;
    private VoucherRepository repo;

    @Before
    public void setup() {
        final VoucherPoolServiceProperties vpsProperties = mock(VoucherPoolServiceProperties.class);
        when(vpsProperties.getVoucherCollectionName()).thenReturn(VOUCHER_COLLECTION_NAME);
        mongoTemplate = mock(MongoTemplate.class);
        repo = new MongoVoucherRepository(mongoTemplate, vpsProperties);
    }

    @Test
    public void getVoucherByCodeAndNoUsageDate_validCode_findOneIsCalled() {
        repo.getVoucherByCodeAndNoUsageDate(VALID_CODE_SAMPLE);
        verify(mongoTemplate, times(1)).findOne(
                VALID_CODE_UNUSED_QUERY,
                Voucher.class,
                VOUCHER_COLLECTION_NAME);
    }

    @Test
    @Parameters(method = "invalidCodesTestData")
    @TestCaseName("{0}")
    public void getVoucherByCodeAndNoUsageDate_invalidCode_findOneIsNotCalled(final String testCase,
                                                                              final String code) {
        repo.getVoucherByCodeAndNoUsageDate(code);
        verify(mongoTemplate, times(0)).findOne(
                any(),
                eq(Voucher.class),
                eq(VOUCHER_COLLECTION_NAME));
    }

    @Test
    public void getVouchersByCodesAndNoUsageDate_validCode_findIsCalled() {
        repo.getVouchersByCodesAndNoUsageDate(VALID_CODES_SAMPLE);
        verify(mongoTemplate, times(1)).find(
                VALID_CODES_UNUSED_QUERY,
                Voucher.class,
                VOUCHER_COLLECTION_NAME);
    }

    @Test
    @Parameters(method = "invalidCodeSetTestData")
    @TestCaseName("{0}")
    public void getVouchersByCodesAndNoUsageDate_invalidCode_findIsNotCalled(final String testCase,
                                                                             final Set<String> codeSet) {
        repo.getVouchersByCodesAndNoUsageDate(codeSet);
        verify(mongoTemplate, times(0)).find(
                any(),
                eq(Voucher.class),
                eq(VOUCHER_COLLECTION_NAME));
    }

    @Test
    public void updateUsageDateByCode_validCode_updateFirstIsCalled() {
        repo.updateUsageDateByCode(VALID_CODE_SAMPLE, VALID_USAGE_DATE);
        verify(mongoTemplate, times(1)).updateFirst(
                eq(VALID_CODE_QUERY),
                isA(Update.class),
                eq(Voucher.class),
                eq(VOUCHER_COLLECTION_NAME));
    }

    @Test
    @Parameters(method = "invalidCodesTestData")
    @TestCaseName("{0}")
    public void updateUsageDateByCode_invalidCode_updateFirstIsNotCalled(final String testCase,
                                                                         final String code) {
        repo.updateUsageDateByCode(code, VALID_USAGE_DATE);
        verify(mongoTemplate, times(0)).updateFirst(
                any(),
                isA(Update.class),
                eq(Voucher.class),
                eq(VOUCHER_COLLECTION_NAME));
    }

    @Test
    public void insertVoucher_validVoucher_insertIsCalled() {
        repo.insertVoucher(VALID_VOUCHER);
        verify(mongoTemplate, times(1)).insert(
                VALID_VOUCHER,
                VOUCHER_COLLECTION_NAME);
    }

    @Test
    public void insertVoucher_nullVoucher_insertIsNotCalled() {
        repo.insertVoucher(null);
        verify(mongoTemplate, times(0)).insert(
                any(),
                eq(VOUCHER_COLLECTION_NAME));
    }

    private Object[] invalidCodesTestData() {
        return new Object[][] {
                {"empty code", ""},
                {"null code", null}
        };
    }

    private Object[] invalidCodeSetTestData() {
        return new Object[][] {
                {"empty code set", Collections.emptySet()},
                {"null code set", null}
        };
    }
}

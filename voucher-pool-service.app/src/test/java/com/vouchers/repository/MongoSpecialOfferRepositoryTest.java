package com.vouchers.repository;

import com.vouchers.model.SpecialOffer;
import com.vouchers.properties.VoucherPoolServiceProperties;
import com.vouchers.repository.impl.MongoSpecialOfferRepository;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import junitparams.naming.TestCaseName;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(JUnitParamsRunner.class)
public class MongoSpecialOfferRepositoryTest {

    private static final String SPECIAL_OFFER_COLLECTION_NAME = "spO";
    private static final String VALID_NAME_SAMPLE = "50% off";
    private static final SpecialOffer VALID_SPECIAL_OFFER = new SpecialOffer(VALID_NAME_SAMPLE, "12/12/2020", 50.0);
    private static final Query VALID_NAME_QUERY = new Query(Criteria.where("name").is(VALID_NAME_SAMPLE));

    private MongoTemplate mongoTemplate;
    private SpecialOfferRepository repo;

    @Before
    public void setup() {
        final VoucherPoolServiceProperties vpsProperties = mock(VoucherPoolServiceProperties.class);
        when(vpsProperties.getSpecialOfferCollectionName()).thenReturn(SPECIAL_OFFER_COLLECTION_NAME);
        mongoTemplate = mock(MongoTemplate.class);
        repo = new MongoSpecialOfferRepository(mongoTemplate, vpsProperties);
    }

    @Test
    public void getSpecialOfferByName_givenValidName_findOneIsCalled() {
        repo.getSpecialOfferByName(VALID_NAME_SAMPLE);
        verify(mongoTemplate, times(1)).findOne(
                VALID_NAME_QUERY,
                SpecialOffer.class,
                SPECIAL_OFFER_COLLECTION_NAME);
    }

    @Test
    @Parameters(method = "invalidNamesTestData")
    @TestCaseName("{0}")
    public void getSpecialOfferByName_givenInvalidName_findOneIsNotCalled(final String testCase,
                                                                          final String name) {
        repo.getSpecialOfferByName(name);
        verify(mongoTemplate, times(0)).findOne(
                isA(Query.class),
                eq(SpecialOffer.class),
                eq(SPECIAL_OFFER_COLLECTION_NAME));
    }

    @Test
    public void insertSpecialOffer_validSpecialOffer_insertOneIsCalled() {
        repo.insertSpecialOffer(new SpecialOffer(VALID_NAME_SAMPLE, "12/12/2020", 50.0));
        verify(mongoTemplate, times(1)).insert(
                VALID_SPECIAL_OFFER,
                SPECIAL_OFFER_COLLECTION_NAME);
    }

    @Test
    public void insertSpecialOffer_nullSpecialOffer_insertOneIsNotCalled() {
        repo.insertSpecialOffer(null);
        verify(mongoTemplate, times(0)).insert(
                any(),
                eq(SPECIAL_OFFER_COLLECTION_NAME));
    }

    private Object[] invalidNamesTestData() {
        return new Object[][] {
                {"empty name", ""},
                {"null name", null}
        };
    }
}

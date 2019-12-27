package com.vouchers.repository.impl;

import com.vouchers.model.SpecialOffer;
import com.vouchers.properties.VoucherPoolServiceProperties;
import com.vouchers.repository.SpecialOfferRepository;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public class MongoSpecialOfferRepository implements SpecialOfferRepository {

    private final MongoTemplate mongoTemplate;
    private final String collectionName;

    public MongoSpecialOfferRepository(final MongoTemplate mongoTemplate,
                                       final VoucherPoolServiceProperties vpsProperties) {
        this.mongoTemplate = mongoTemplate;
        this.collectionName = vpsProperties.getSpecialOfferCollectionName();
    }

    @Override
    public SpecialOffer getSpecialOfferByName(final String name) {
        if(name == null || name.isEmpty()) {
            return null;
        }
        final Criteria criteria = Criteria.where("name").is(name);
        final Query query = new Query(criteria);
        return mongoTemplate.findOne(query, SpecialOffer.class, collectionName);
    }

    @Override
    public void insertSpecialOffer(final SpecialOffer specialOffer) {
        if (specialOffer == null) {
            return;
        }
        mongoTemplate.insert(specialOffer, collectionName);
    }
}

package com.vouchers.repository.impl;

import com.vouchers.model.Recipient;
import com.vouchers.properties.VoucherPoolServiceProperties;
import com.vouchers.repository.RecipientRepository;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public class MongoRecipientRepository implements RecipientRepository {

    private final MongoTemplate mongoTemplate;
    private final String collectionName;

    public MongoRecipientRepository(final MongoTemplate mongoTemplate,
                                    final VoucherPoolServiceProperties vpsProperties) {
        this.mongoTemplate = mongoTemplate;
        this.collectionName = vpsProperties.getRecipientCollectionName();
    }

    @Override
    public List<Recipient> getAllRecipients() {
        return mongoTemplate.findAll(Recipient.class, collectionName);
    }

    @Override
    public Recipient getRecipientByEmail(final String email) {
        if(email == null || email.isEmpty()) {
            return null;
        }
        final Criteria criteria = Criteria.where("email").is(email);
        final Query query = new Query(criteria);
        return mongoTemplate.findOne(query, Recipient.class, collectionName);
    }

    @Override
    public void UpdateVoucherCodesByEmail(final String email, final Set<String> voucherCodes) {
        if(email == null || email.isEmpty()) {
            return;
        }
        final Criteria criteria = Criteria.where("email").is(email);
        final Query query = new Query(criteria);
        final Update update = new Update();
        update.set("voucherCodes", voucherCodes);
        mongoTemplate.updateFirst(query, update, Recipient.class, collectionName);
    }
}

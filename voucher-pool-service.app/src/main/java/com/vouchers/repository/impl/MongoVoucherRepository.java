package com.vouchers.repository.impl;

import com.vouchers.model.Voucher;
import com.vouchers.properties.VoucherPoolServiceProperties;
import com.vouchers.repository.VoucherRepository;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository
public class MongoVoucherRepository implements VoucherRepository {

    private final MongoTemplate mongoTemplate;
    private final String collectionName;

    public MongoVoucherRepository(final MongoTemplate mongoTemplate,
                                  final VoucherPoolServiceProperties vpsProperties) {
        this.mongoTemplate = mongoTemplate;
        this.collectionName = vpsProperties.getVoucherCollectionName();
    }

    @Override
    public Voucher getVoucherByCodeAndNoUsageDate(final String code) {
        if(code == null || code.isEmpty()) {
            return null;
        }

        final Criteria criteria1 = Criteria.where("code").is(code);
        final Criteria criteria2 = Criteria.where("usageDate").exists(false);
        final Criteria allCriteria = criteria1.andOperator(criteria2);
        final Query query = new Query(allCriteria);
        return mongoTemplate.findOne(query, Voucher.class, collectionName);
    }

    @Override
    public List<Voucher> getVouchersByCodesAndNoUsageDate(final Set<String> codes) {
        if(codes == null || codes.isEmpty()) {
            return Collections.emptyList();
        }
        final Criteria criteria1 = Criteria.where("code").in(codes);
        final Criteria criteria2 = Criteria.where("usageDate").exists(false);
        final Criteria allCriteria = criteria1.andOperator(criteria2);
        final Query query = new Query(allCriteria);
        return mongoTemplate.find(query, Voucher.class, collectionName);
    }

    @Override
    public void updateUsageDateByCode(final String code, final String usageDate) {
        if(code == null || code.isEmpty()){
            return;
        }

        final Criteria criteria = Criteria.where("code").is(code);
        final Query query = new Query(criteria);
        final Update update = new Update();
        update.set("usageDate", usageDate);
        mongoTemplate.updateFirst(query, update, Voucher.class, collectionName);
    }

    @Override
    public void insertVoucher(final Voucher voucher) {
        if(voucher == null) {
            return;
        }
        mongoTemplate.insert(voucher, collectionName);
    }
}

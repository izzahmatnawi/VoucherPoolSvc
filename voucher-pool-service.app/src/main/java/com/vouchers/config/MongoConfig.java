package com.vouchers.config;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.vouchers.VoucherPoolApplication;
import com.vouchers.properties.VoucherPoolServiceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
@Import(VoucherPoolApplication.class)
public class MongoConfig {

    @Bean
    public Mongo mongo(final VoucherPoolServiceProperties vpsProps) {
        return new MongoClient(new MongoClientURI(vpsProps.getMongoUri()));
    }

    @Bean
    public MongoTemplate mongoTemplate(final Mongo mongo, final VoucherPoolServiceProperties vpsProps) {
        final MongoClientURI uri = new MongoClientURI(vpsProps.getMongoUri());
        return new MongoTemplate(mongo, uri.getDatabase());
    }
}

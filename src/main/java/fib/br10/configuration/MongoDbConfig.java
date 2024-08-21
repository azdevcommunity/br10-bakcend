package fib.br10.configuration;


//import com.mongodb.client.MongoClient;
//import com.mongodb.client.MongoClients;
//import lombok.NonNull;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.mongodb.MongoDatabaseFactory;
//import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
//import org.springframework.data.mongodb.core.MongoTemplate;
//import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
//import org.springframework.data.mongodb.gridfs.GridFsTemplate;
//import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;


//@Configuration
//@EnableMongoRepositories(basePackages = "fib.br10.repository")
public class MongoDbConfig
//        extends AbstractMongoClientConfiguration
{

//    @Value("${spring.data.mongodb.uri}")
//    private String uri;
//
//    @Value("${spring.data.mongodb.database}")
//    private String databaseName;
//
//    @Override
//    protected @NonNull String getDatabaseName() {
//        return databaseName;
//    }
//
//    @Bean
//    @Override
//    public @NonNull MongoClient mongoClient() {
//        return MongoClients.create(uri);
//    }
//
//    @Bean
//    public GridFsTemplate gridFsTemplate(MongoDatabaseFactory mongoDbFactory, MappingMongoConverter mappingMongoConverter) {
//        return new GridFsTemplate(mongoDbFactory, mappingMongoConverter);
//    }
//
//    @Bean
//    public MongoTemplate mongoTemplate() {
//        return new MongoTemplate(mongoClient(), databaseName);
//    }
}
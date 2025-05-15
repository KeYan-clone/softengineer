package org.linghu.mybackend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.TextIndexDefinition;

@Configuration
public class MongoConfig {

    @Bean
    public void mongoIndexes(MongoTemplate mongoTemplate) {
        // 创建Discussion 文本索引
        if (!mongoTemplate.indexOps("discussions").getIndexInfo().stream()
                .anyMatch(index -> index.getName().equals("title_content_text"))) {
            
            TextIndexDefinition textIndex = new TextIndexDefinition.TextIndexDefinitionBuilder()
                    .onField("title", 10.0f)
                    .onField("content", 5.0f)
                    .withDefaultLanguage("chinese")
                    .build();
            mongoTemplate.indexOps("discussions").ensureIndex(textIndex);
        }
        
        // 创建Comment 文本索引
        if (!mongoTemplate.indexOps("comments").getIndexInfo().stream()
                .anyMatch(index -> index.getName().equals("content_text"))) {
            
            TextIndexDefinition textIndex = new TextIndexDefinition.TextIndexDefinitionBuilder()
                    .onField("content")
                    .withDefaultLanguage("chinese")
                    .build();
            mongoTemplate.indexOps("comments").ensureIndex(textIndex);
        }
    }
}

package com.ecommerce.productservice.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Configuration
@ConditionalOnProperty(prefix = "app.search", name = "enabled", havingValue = "true")
@EnableElasticsearchRepositories(basePackages = "com.ecommerce.productservice.modules.search")
public class ElasticsearchConfig {
    // Spring Boot auto-configuration handles the rest
}


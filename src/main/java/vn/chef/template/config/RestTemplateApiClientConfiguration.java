package vn.chef.template.config;

import vn.chef.template.client.RestTemplateClientInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
public class RestTemplateApiClientConfiguration {

    @Value("${template-service..client.timeout:30}")
    private int timeout;

    private final RestTemplateBuilder restTemplateBuilder;

    public RestTemplateApiClientConfiguration(final RestTemplateBuilder restTemplateBuilder) {
        this.restTemplateBuilder = restTemplateBuilder;
    }

    @Bean
    public RestTemplate getRestTemplate() {
        RestTemplateClientInterceptor interceptor = new RestTemplateClientInterceptor();
        return restTemplateBuilder
            .setConnectTimeout(Duration.ofSeconds(timeout))
            .additionalInterceptors(interceptor)
            .build();
    }
}

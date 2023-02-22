package vn.chef.template.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import vn.chef.common.web.security.UserContextHolder;

import java.io.IOException;

@Slf4j
@Component
public class RestTemplateClientInterceptor implements ClientHttpRequestInterceptor {

    @Override
    @SuppressWarnings("NullableProblems")
    public ClientHttpResponse intercept(
        HttpRequest httpRequest,
        byte[] bytes,
        ClientHttpRequestExecution execution) throws IOException {
        httpRequest.getHeaders().set(HttpHeaders.AUTHORIZATION, UserContextHolder.getContext().getBearerToken());

        return execution.execute(httpRequest, bytes);
    }

}

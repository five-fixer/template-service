package vn.chef.template.web.rest.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.InvalidDefinitionException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonInputMessage;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.stream.Collectors;

/**
 * @author fixcer
 */
@Slf4j
@Component
@SuppressWarnings("NullableProblems")
public class MultipartJackson2HttpMessageConverter extends AbstractJackson2HttpMessageConverter {

    /**
     * Converter for support http request with header Content-Type: multipart/form-data
     */
    public MultipartJackson2HttpMessageConverter(final ObjectMapper objectMapper) {
        super(objectMapper, MediaType.APPLICATION_OCTET_STREAM);
    }

    @Override
    public Object read(final Type type, final Class<?> contextClass, final HttpInputMessage inputMessage)
            throws IOException, HttpMessageNotReadableException {
        final JavaType javaType = getJavaType(type, contextClass);

        return readJavaType(javaType, inputMessage);
    }

    @Override
    public boolean canWrite(final Class<?> clazz, final MediaType mediaType) {
        return false;
    }

    @Override
    public boolean canWrite(final Type type, final Class<?> clazz, final MediaType mediaType) {
        return false;
    }

    @Override
    protected boolean canWrite(final MediaType mediaType) {
        return false;
    }

    private Object readJavaType(final JavaType javaType, final HttpInputMessage inputMessage) throws IOException {
        try {
            final InputStream body = inputMessage.getBody();
            String requestBody = new BufferedReader(new InputStreamReader(body, StandardCharsets.UTF_8))
                    .lines()
                    .collect(Collectors.joining("\n"));
            requestBody = decodeBase64IfAny(requestBody);

            if (inputMessage instanceof MappingJacksonInputMessage) {
                final Class<?> deserializationView = ((MappingJacksonInputMessage) inputMessage).getDeserializationView();
                if (deserializationView != null) {
                    return defaultObjectMapper.readerWithView(deserializationView).forType(javaType).readValue(requestBody);
                }
            }

            return defaultObjectMapper.readValue(requestBody, javaType);
        } catch (final InvalidDefinitionException ex) {
            throw new HttpMessageConversionException("Type definition error: " + ex.getType(), ex);
        } catch (final JsonProcessingException ex) {
            throw new HttpMessageNotReadableException("JSON parse error: " + ex.getOriginalMessage(), ex, inputMessage);
        }
    }

    private String decodeBase64IfAny(final String requestBody) {
        try {
            return new String(Base64.getDecoder().decode(requestBody), StandardCharsets.UTF_8.name());
        } catch (final Exception exception) {
            return requestBody;
        }
    }

}

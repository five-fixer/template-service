package vn.chef.template.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.AbstractRequestLoggingFilter;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Configuration
public class RequestLoggingTimeFilter extends AbstractRequestLoggingFilter {

    @Override
    protected boolean isIncludeClientInfo() {
        return true;
    }

    @Override
    protected boolean isIncludeQueryString() {
        return true;
    }

    @Override
    protected boolean isIncludePayload() {
        return true;
    }

    @Override
    protected int getMaxPayloadLength() {
        return 64000;
    }

    @Override
    public void setAfterMessagePrefix(String afterMessagePrefix) {
        super.setAfterMessagePrefix("Executed request");
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return new AntPathMatcher().match("/management/health", request.getServletPath());
    }

    @Override
    protected void beforeRequest(HttpServletRequest request, String message) {
        request.setAttribute("StartTime", System.currentTimeMillis());
    }

    @Override
    protected void afterRequest(HttpServletRequest request, String message) {
        log.debug(createMessage(request, "Executed request [", "]"));
        log.debug("Total time execute {}ms ", (System.currentTimeMillis() - Long.parseLong(String.valueOf(request.getAttribute("StartTime")))));
    }
}

package vn.chef.template.security.jwt;

import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;
import vn.chef.common.constant.Constants;
import vn.chef.common.web.security.AutoDestroyThreadLocal;
import vn.chef.common.web.security.UserContext;
import vn.chef.common.web.security.UserContextHolder;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class DummyAuthenticationConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    @Override
    public void configure(final HttpSecurity httpSecurity) {
       httpSecurity
            .antMatcher("/api/**")
            .addFilterBefore(new OncePerRequestFilter() {
                @Override
                protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
                        try {
                            UserContext userContext = UserContextHolder.getContext();
                            userContext.setUserId(Constants.UserContext.ANONYMOUS_USER);
                            userContext.setOsName(request.getParameter(Constants.UserContext.OS_NAME));
                            userContext.setOsVersion(request.getParameter(Constants.UserContext.OS_VERSION));
                            userContext.setChefAIApp(true);
                            filterChain.doFilter(request, response);
                        } finally {
                            UserContextHolder.getContext().clear();
                            AutoDestroyThreadLocal.removeAll();
                        }

                    }
            }, UsernamePasswordAuthenticationFilter.class);
    }

}

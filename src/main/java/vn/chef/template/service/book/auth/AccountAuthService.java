package vn.chef.template.service.book.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import vn.chef.template.domain.UserPrincipal;
import vn.chef.template.security.jwt.JwtTokenProvider;
import vn.chef.template.utils.Constants;
import vn.chef.template.web.api.model.LoginRequest;
import vn.chef.template.web.api.model.LoginThirdPartyRequest;

@Component
public class AccountAuthService {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    protected JwtTokenProvider tokenProvider;

    public String login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserPrincipal  userPrincipal = (UserPrincipal) authentication.getPrincipal();

        String jwt = tokenProvider.generateTokenWithPrinciple(userPrincipal);
        return Constants.TOKEN_PREFIX + jwt;

    }
}

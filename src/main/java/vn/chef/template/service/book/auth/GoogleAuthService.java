package vn.chef.template.service.book.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import vn.chef.common.exception.ChefValidationException;
import vn.chef.template.domain.auth.GoogleUserModel;
import vn.chef.template.domain.User;
import vn.chef.template.domain.UserPrincipal;
import vn.chef.template.domain.enumtype.LoginMethodEnum;
import vn.chef.template.repository.IUserRepository;
import vn.chef.template.security.jwt.JwtTokenProvider;
import vn.chef.template.utils.Constants;
import vn.chef.template.web.api.model.LoginThirdPartyRequest;

import java.util.Optional;


@Component
public class GoogleAuthService implements ThirdPartyAuthService {
    @Autowired
    public IUserRepository userRepository;

    @Autowired
    protected JwtTokenProvider tokenProvider;

    public GoogleUserModel getUserInfo(String accessToken){
        String templateUrl = String.format(Constants.GOOGLE_AUTH_URL, accessToken);
        GoogleUserModel googleUserModel = WebClient.create().get().uri(templateUrl).retrieve()
                .onStatus(HttpStatus::isError, clientResponse -> {
                    throw new ChefValidationException("Login google failed", HttpStatus.CONFLICT.value());
                })
                .bodyToMono(GoogleUserModel.class)
                .block();
        return googleUserModel;
    }


    @Override
    public String login(LoginThirdPartyRequest facebookAuthModel) {
        GoogleUserModel googleUserModel = getUserInfo(facebookAuthModel.getAccessToken());

        final Optional<User> userOptional = Optional.ofNullable(userRepository.findByEmail(googleUserModel.getEmail()));
        String jwt;
        if (userOptional.isEmpty()) {
            final User user = User.builder()
                    .email(googleUserModel.getEmail())
                    .avatar(googleUserModel.getImageUrl())
                    .password("")
                    .userRole("USER")
                    .provider(LoginMethodEnum.GOOGLE)
                    .build();
            userRepository.save(user);
            final UserPrincipal userPrincipal = new UserPrincipal(user);
            jwt = tokenProvider.generateToken(userPrincipal);
            return Constants.TOKEN_PREFIX + jwt;
        } else { // user exists just login
            final User user = userOptional.get();
            user.setAvatar(googleUserModel.getImageUrl());
            userRepository.save(user);
            UserPrincipal userPrincipal = new UserPrincipal(user);
            jwt = tokenProvider.generateTokenWithPrinciple(userPrincipal);

        }
        return Constants.TOKEN_PREFIX + jwt;

    }

}

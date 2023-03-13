package vn.chef.template.service.book.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import vn.chef.common.exception.ChefValidationException;
import vn.chef.template.domain.auth.FacebookUserModel;
import vn.chef.template.domain.User;
import vn.chef.template.domain.UserPrincipal;
import vn.chef.template.domain.enumtype.LoginMethodEnum;
import vn.chef.template.repository.IUserRepository;
import vn.chef.template.security.jwt.JwtTokenProvider;
import vn.chef.template.utils.Constants;
import vn.chef.template.web.api.model.LoginThirdPartyRequest;

import java.util.Optional;


@Component
public class FaceAuthService implements ThirdPartyAuthService {
    @Autowired
    public IUserRepository userRepository;

    @Autowired
    protected JwtTokenProvider tokenProvider;
    public FacebookUserModel getUserInfo(String accessToken){
        String templateUrl = String.format(Constants.FACEBOOK_AUTH_URL, accessToken);
        FacebookUserModel facebookUserModel = WebClient.create().get().uri(templateUrl).retrieve()
                .onStatus(HttpStatus::isError, clientResponse -> {
                    System.out.println(clientResponse.toString());
                    throw new ChefValidationException("Login facebook failed:", HttpStatus.CONFLICT.value());
                })
                .bodyToMono(FacebookUserModel.class)
                .block();
        return facebookUserModel;
    }


    public String login(LoginThirdPartyRequest facebookAuthModel) {
        FacebookUserModel facebookUserModel = getUserInfo(facebookAuthModel.getAccessToken());

        System.out.println(facebookUserModel.getAvatar());

        final Optional<User> userOptional = Optional.ofNullable(userRepository.findByEmail(facebookUserModel.getId()));
        String jwt;
        if (userOptional.isEmpty()) {
            final User user = User.builder()
                    .email(facebookUserModel.getId())
                    .password("")
                    .userRole("USER")
                    .avatar(facebookUserModel.getAvatar())
                    .provider(LoginMethodEnum.FACEBOOK)
                    .build();
            userRepository.save(user);
            final UserPrincipal userPrincipal = new UserPrincipal(user);
            jwt = tokenProvider.generateToken(userPrincipal);
            return Constants.TOKEN_PREFIX + jwt;
        } else { // user exists just login
            final User user = userOptional.get();
//            if ((user.getProvider() != LoginMethodEnum.FACEBOOK)) { //check if logged in with different logged in method
//                throw new ChefValidationException("Login facebook failed", HttpStatus.CONFLICT.value());
//            }
            user.setAvatar(facebookUserModel.getAvatar());
            UserPrincipal userPrincipal = new UserPrincipal(user);
            jwt = tokenProvider.generateTokenWithPrinciple(userPrincipal);

        }
        return Constants.TOKEN_PREFIX + jwt;

    }

}

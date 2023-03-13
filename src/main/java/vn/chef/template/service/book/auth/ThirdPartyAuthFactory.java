package vn.chef.template.service.book.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import vn.chef.common.exception.ChefValidationException;
import vn.chef.template.domain.enumtype.LoginMethodEnum;

@Service
public class ThirdPartyAuthFactory {

    @Autowired
    private FaceAuthService faceAuthService;

    @Autowired
    private  GoogleAuthService googleAuthService;

    public ThirdPartyAuthService getAuth(LoginMethodEnum loginMethod){
        switch (loginMethod){
            case FACEBOOK:
                return faceAuthService;
            case GOOGLE:
                return googleAuthService;
            default:
                throw new ChefValidationException(String.format("Not support method login with %s", loginMethod.toString()), HttpStatus.CONFLICT.value());
        }

    }

}

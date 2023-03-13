package vn.chef.template.web.rest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import vn.chef.template.domain.enumtype.LoginMethodEnum;
import vn.chef.template.service.book.auth.AccountAuthService;
import vn.chef.template.service.book.auth.ThirdPartyAuthFactory;
import vn.chef.template.service.book.auth.ThirdPartyAuthService;
import vn.chef.template.web.api.AuthApiDelegate;


import org.springframework.http.ResponseEntity;
import vn.chef.template.web.api.model.LoginRequest;
import vn.chef.template.web.api.model.LoginThirdPartyRequest;

@Component
@RequiredArgsConstructor
@ComponentScan("vn.chef.template.service.book.auth")
public class UserAPI implements AuthApiDelegate {
    @Autowired
    private ThirdPartyAuthFactory thirdPartyAuthFactory;

    @Autowired
    private AccountAuthService accountAuthService;


    @Override
    public ResponseEntity<String> loginWithThirdParty(LoginThirdPartyRequest request) {
        String loginMethod = request.getProvider().toString();
        ThirdPartyAuthService thirdPartyAuthService = thirdPartyAuthFactory.getAuth(LoginMethodEnum.valueOf(loginMethod));
        return ResponseEntity.ok(thirdPartyAuthService.login(request));
    }

    @Override
    public ResponseEntity<String> loginWithAccount(LoginRequest request){
        return  ResponseEntity.ok(accountAuthService.login(request));
    }

}

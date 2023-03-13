package vn.chef.template.service.book.auth;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.chef.template.web.api.model.LoginThirdPartyRequest;

@Service
@Transactional
public interface ThirdPartyAuthService {
    public  String login(LoginThirdPartyRequest loginThirdPartyRequestModel);
}

package vn.chef.template.domain.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GoogleUserModel extends ThirdPartyUserModel {
    @JsonProperty("sub")
    private String id;

    private String name;

    private String email;

    @JsonProperty("picture")
    private String imageUrl;
}

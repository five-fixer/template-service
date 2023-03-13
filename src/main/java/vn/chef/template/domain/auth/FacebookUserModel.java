package vn.chef.template.domain.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class FacebookUserModel extends ThirdPartyUserModel {
    private String id;
    @JsonProperty("first_name")
    private String firstName;
    @JsonProperty("last_name")
    private String lastName;

    @JsonProperty("picture")
    private HashMap imageUrl;

    public String getAvatar()  {
            if(imageUrl.containsKey("data")) {
                Map<String, Object> dataObj = (Map<String, Object>) imageUrl.get("data");
                if(dataObj.containsKey("url")) {
                    return (String) dataObj.get("url");
                }
            }

        return null;
    }
}

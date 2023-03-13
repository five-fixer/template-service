package vn.chef.template.utils;

import lombok.experimental.UtilityClass;


@UtilityClass
public final class Constants {
    public static final String HEADER_STRING = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String SECRET = "8f821a74-367b-4741-95b6-fdfad9b44705"; //can be generated from uuid
    public static final long EXPIRATION_TIME = 31536000000L;
    public static final String FACEBOOK_AUTH_URL = "https://graph.facebook.com/me?fields=email,first_name,last_name,picture.width(250).height(250)&access_token=%s";

    public  static  final  String GOOGLE_AUTH_URL = "https://www.googleapis.com/oauth2/v3/userinfo?access_token=%s";
}

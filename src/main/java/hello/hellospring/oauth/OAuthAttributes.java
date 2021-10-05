package hello.hellospring.oauth;

import com.google.gson.Gson;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.util.Map;

@Getter
public class OAuthAttributes implements Serializable {
    private Map<String, Object> attributes;
    private String oauthKey;
    private String oauthName;


    @Builder
    public OAuthAttributes(Map<String, Object> attributes, String oauthKey, String oauthName) {
        this.attributes = attributes;
        this.oauthKey = oauthKey;
        this.oauthName = oauthName;
        System.out.println("-------------------oauth info ---------------");
        System.out.println(" oauth = "+oauthName);
        System.out.println("attributes = "+attributes);
        System.out.println("oauthKey = "+oauthKey);
        System.out.println("---------------------------------------------");
    }

    public static OAuthAttributes of(String registrationId, Map<String ,Object> attributes) {
        System.out.println("OAuthAttributes.OAuthAttributes");
        switch (registrationId){
            case "google":
                return ofGoogle(registrationId, attributes);

            case "kakao":
                return ofKakao(registrationId, attributes);

            case "naver":
                return ofNaver(registrationId, attributes);

            case "facebook":
                return ofFacebook(registrationId, attributes);

            default:
                return null;
        }

    }

    private static OAuthAttributes ofFacebook(String registrationId, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .oauthName(registrationId)
                .oauthKey(attributes.get("id").toString())
                .attributes(attributes)
                .build();
    }



    private static OAuthAttributes ofKakao(String registrationId, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .oauthName(registrationId)
                .oauthKey(attributes.get("id").toString())
                .attributes(attributes)
                .build();
    }


    private static OAuthAttributes ofGoogle(String registrationId, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .oauthName(registrationId)
                .oauthKey(attributes.get("sub").toString())
                .attributes(attributes)
                .build();
    }

    private static OAuthAttributes ofNaver(String registrationId, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .oauthName(registrationId)
                .oauthKey(jData(attributes,"response", "id"))
                .attributes(attributes)
                .build();
    }


    // json 데이터 출력
    public static String jData(Map<String, Object> attributes, String name, String value) {
        return new Gson().toJsonTree(attributes.get(name)).getAsJsonObject().get(value).getAsString();
    }
}
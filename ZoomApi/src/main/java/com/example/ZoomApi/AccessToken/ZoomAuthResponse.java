package com.example.ZoomApi.AccessToken;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ZoomAuthResponse {

    @JsonProperty(value = "access_token")
    private String accessToken;

    @JsonProperty(value = "token_type")
    private String tokenType;

    @JsonProperty(value = "expires_in")
    private int expiresIn;

    @JsonProperty(value = "scope")
    private String scope;

    public String getAccessToken() {
        return accessToken;
    }
}

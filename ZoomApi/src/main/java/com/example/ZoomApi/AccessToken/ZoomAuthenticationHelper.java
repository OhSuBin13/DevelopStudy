package com.example.ZoomApi.AccessToken;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Base64;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class ZoomAuthenticationHelper {

    @Value("${spring.zoom.oauth2.client-id}")
    private String zoomClientId;

    @Value("${spring.zoom.oauth2.client-secret}")
    private String zoomClientSecret;

    @Value("${spring.zoom.oauth2.issuer}")
    private String zoomIssueUrl;

    @Value("${spring.zoom.oauth2.account-id}")
    private String zoomAccountId;

    private ZoomAuthResponse zoomAuthResponse;

    private long tokenExpiryTime;

    public synchronized String getAccessToken() throws Exception {
        if(this.zoomAuthResponse == null || checkIfTokenWillExpire()) {
            fetchToken();
        }
        return this.zoomAuthResponse.getAccessToken();
    }

    /*토큰 재발급이 필요한지 여부 확인*/
    private boolean checkIfTokenWillExpire() {
        Calendar now = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"));
        long differenceInMills = this.tokenExpiryTime - now.getTimeInMillis();

        //토큰 이미 만료 or 20분내 만료 예정
        if(differenceInMills < 0 ||
                TimeUnit.MILLISECONDS.toMinutes(differenceInMills) < 20) {
            return true;
        }

        return false;
    }

    private void fetchToken() throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        String credentials = zoomClientId + ":" + zoomClientSecret;
        String encodedCredentials = new
                String(Base64.getEncoder().encodeToString(credentials.getBytes()));

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_FORM_URLENCODED));
        httpHeaders.add("Authorization", "Basic " + encodedCredentials);
        httpHeaders.add("Host", "zoom.us");

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("grant_type", "account_credentials");
        map.add("account_id", zoomAccountId);

        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>
                (map, httpHeaders);
        try {
            this.zoomAuthResponse = restTemplate.exchange(zoomIssueUrl,
                    HttpMethod.POST, httpEntity, ZoomAuthResponse.class).getBody();
        } catch (HttpClientErrorException e) {
            ResponseEntity<String> errorResponse = new ResponseEntity<>
                    (e.getResponseBodyAsString(), e.getStatusCode());
            throw new Exception(String.format(
                    "Unable to get authentication token due to %s. Response code: %d",
                    errorResponse.getBody(), errorResponse.getStatusCode().value())
            );
        }

        Calendar now = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"));
        this.tokenExpiryTime = now.getTimeInMillis() +
                (this.zoomAuthResponse.getExpiresIn() - 10) * 1000;
    }
}

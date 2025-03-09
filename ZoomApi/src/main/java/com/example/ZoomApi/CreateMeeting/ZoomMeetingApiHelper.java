package com.example.ZoomApi.CreateMeeting;

import com.example.ZoomApi.AccessToken.ZoomAuthenticationHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class ZoomMeetingApiHelper {

    private final ZoomAuthenticationHelper zoomAuthenticationHelper;

    @Value("${spring.zoom.oauth2.api-url}")
    private String zoomApiUri;

    @Value("${spring.zoom.email.host}")
    private String hostEmail;

    private ZoomMeetingCreateDTO createRequestDTO(ProgramType type,
                                                  String title, Integer th, LocalDateTime startDate) {
        String description = type.getValue() + "#" + th + " " + title;
        return ZoomMeetingCreateDTO.of(
                description,
                180,
                startDate,
                description
        );
    }

    public ZoomMeetingCreateResponse createMeeting(ProgramType type, String title,
                                                      Integer th, LocalDateTime startDate) throws Exception {
        ZoomMeetingCreateDTO requestDTO = createRequestDTO(type, title, th,
                startDate);
        String requestUrl = zoomApiUri + "/v2/users/" + hostEmail + "/meetings";

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Bearer" +
                zoomAuthenticationHelper.getAccessToken());
        httpHeaders.add("content-type", "application/json");

        HttpEntity<ZoomMeetingCreateDTO> httpEntity = new HttpEntity<>
                (requestDTO, httpHeaders);
        try {
            ResponseEntity<ZoomMeetingCreateResponse> responseEntity =
                    restTemplate.exchange(requestUrl, HttpMethod.POST, httpEntity,
                            ZoomMeetingCreateResponse.class);
            if(responseEntity.getStatusCode().value() == 201) {
                return responseEntity.getBody();
            }
        } catch (HttpClientErrorException e) {
            ResponseEntity<String> errorResponse = new ResponseEntity<>
                    (e.getResponseBodyAsString(), e.getStatusCode());
            throw new Exception(
                    String.format(
                            "Unable to get response due to %s. Response" +
                                    " code: %d",
                            errorResponse.getBody(), errorResponse.getStatusCode().value())
            );

        }
        return null;
    }
}

package com.example.ZoomApi.AccessToken;

import com.example.ZoomApi.CreateMeeting.ZoomCreateDTO;
import com.example.ZoomApi.CreateMeeting.ZoomMeetingApiHelper;
import com.example.ZoomApi.CreateMeeting.ZoomMeetingCreateResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/program")
public class ProgramController {
    private final ZoomAuthenticationHelper zoomAuthenticationHelper;
    private final ZoomMeetingApiHelper zoomMeetingApiHelper;

    @GetMapping("/zoom")
    public ResponseEntity<String> zoom() throws Exception {
        return ResponseEntity.ok(zoomAuthenticationHelper.getAccessToken());
    }

    @PostMapping("/zoom")
    public ResponseEntity<ZoomMeetingCreateResponse> zoom(@RequestBody
                                                          ZoomCreateDTO requestDTO) throws Exception {
        return ResponseEntity.ok(zoomMeetingApiHelper.createMeeting(requestDTO.getType(),
                requestDTO.getTitle(), requestDTO.getTh(), requestDTO.getStartDate()));
    }
}

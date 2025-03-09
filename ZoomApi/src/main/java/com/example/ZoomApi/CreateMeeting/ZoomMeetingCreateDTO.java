package com.example.ZoomApi.CreateMeeting;

import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
public class ZoomMeetingCreateDTO implements Serializable {
    private String agenda;

    private Boolean default_password = true;

    private Integer duration;

    private String start_time;

    private String timezone = "Asia/Seoul";

    private String topic;

    private Integer type = 2;

    @Builder
    public ZoomMeetingCreateDTO(String agenda, Integer duration,
                                LocalDateTime startTime, String topic) {
        this.agenda = agenda;
        this.duration = duration;
        this.start_time = startTime.toString() + ":00";
        this.topic = topic;
    }

    public static ZoomMeetingCreateDTO of(String agenda, Integer duration,
                                          LocalDateTime startTime, String topic) {
        return ZoomMeetingCreateDTO.builder()
                .agenda(agenda)
                .duration(duration)
                .startTime(startTime)
                .topic(topic)
                .build();
    }
}

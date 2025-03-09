package com.example.ZoomApi.CreateMeeting;

import lombok.Data;

import java.io.Serializable;

@Data
public class ZoomMeetingCreateResponse implements Serializable {

    private String host_email;

    private String id;

    private String created_at;

    private String join_url;

    private String start_time;

    private String password;

    // 3-2-2 확인용
    private String agenda;

    private Integer duration;

    private String timezone;

    private String topic;

    private Integer type;
}

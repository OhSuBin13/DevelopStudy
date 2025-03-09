package com.example.ZoomApi.CreateMeeting;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Data;

public enum ProgramType {
    LETS_CHAT("LETS_CHAT");

    private final String value;

    ProgramType(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue(){
        return value;
    }

    @JsonValue
    public static ProgramType fromValue(String value) {
        for (ProgramType type : ProgramType.values()) {
            if (type.value.equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown enum type " + value);
    }
}

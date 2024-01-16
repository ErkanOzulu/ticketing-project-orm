package com.cydeo.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jboss.resteasy.annotations.SseElementType;

@Getter
@Setter
@NoArgsConstructor
public class ResponseDTO {
    private String access_token;
}

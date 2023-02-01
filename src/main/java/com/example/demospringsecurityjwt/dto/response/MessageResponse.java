package com.example.demospringsecurityjwt.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MessageResponse {
    private String rspCode;

    private String rspMsg;

    private JwtResponse rspData;

    public MessageResponse(String rspCode, String rspMsg) {
        this.rspCode = rspCode;
        this.rspMsg = rspMsg;
    }

}

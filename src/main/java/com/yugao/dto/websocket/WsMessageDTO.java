package com.yugao.dto.websocket;

import lombok.Data;

@Data
public class WsMessageDTO {

    private String type;
    private String content;
    private String timeStamp;
}

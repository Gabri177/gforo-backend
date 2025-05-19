package com.yugao.domain.websocket;

import lombok.Builder;
import lombok.Data;

@Data
public class WsMessage {

    private String type;
    private String content;
    private String timeStamp;
}

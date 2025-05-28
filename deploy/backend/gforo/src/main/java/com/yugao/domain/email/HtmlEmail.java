package com.yugao.domain.email;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class HtmlEmail {

    private String to;
    private String subject;
    private String content;
}

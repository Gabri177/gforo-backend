package com.yugao.vo.report;

import com.yugao.enums.EntityTypeEnum;
import com.yugao.enums.StatusEnum;
import com.yugao.vo.comment.SimpleCommentVO;
import com.yugao.vo.post.SimpleDiscussPostVO;
import com.yugao.vo.user.SimpleUserVO;
import jdk.jshell.Snippet;
import lombok.Data;

import java.util.Date;

@Data
public class ReportInfoVO {

    private Long id; // 举报ID
    private SimpleUserVO reporterInfo; // 举报人信息
    private EntityTypeEnum entityType;
    private SimpleDiscussPostVO post;
    private SimpleCommentVO comment;
    private String reason; // 举报详细说明
    private StatusEnum status; // 举报状态
    private Date createTime;

}

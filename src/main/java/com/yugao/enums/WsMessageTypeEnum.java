package com.yugao.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum WsMessageTypeEnum {
    LIKE_COMMENT("LIKE_COMMENT", "点赞评论"),
    LIKE_POST( "LIKE_POST", "点赞帖子"),
    COMMENT_COMMENT( "COMMENT_COMMENT", "回复评论"),
    COMMENT_POST("COMMENT_POST", "回复帖子"),
    DELETE_ENTITY( "DELETE_ENTITY", "删除实体"),
    HANDLE_REPORT( "HANDLE_REPORT", "处理举报"),
    REFRESH_USER_INFO( "REFRESH_USER_INFO", "刷新用户信息"),
    SYSTEM( "SYSTEM", "系统消息"),
    HEARTBEAT( "pong", "心跳"),
    CHAT( "CHAT", "聊天"),
    DELETE_SESSION( "DELETE_SESSION", "删除会话"),
    CHAT_LIMIT( "CHAT_LIMIT", "聊天限制"),
    NEW_TITLE( "NEW_TITLE", "新称号"),;

    private final String code;
    private final String description;
}

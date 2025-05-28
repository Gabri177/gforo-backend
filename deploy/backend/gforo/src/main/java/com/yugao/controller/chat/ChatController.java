package com.yugao.controller.chat;

import com.yugao.result.ResultFormat;
import com.yugao.result.ResultResponse;
import com.yugao.service.business.chat.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @PreAuthorize("hasAnyAuthority('chat:sessions:own')")
    @GetMapping("/sessions")
    public ResponseEntity<ResultFormat> getMyChatSessions(){
        return ResultResponse.success(chatService.getUserSessions());
    }

    @PreAuthorize("hasAnyAuthority('chat:session-message:own')")
    @GetMapping("/session/{sessionId}")
    public ResponseEntity<ResultFormat> getChatSession(
            @PathVariable Long sessionId
    ) {
        return ResultResponse.success(chatService.getMessagesForSession(sessionId));
    }

    @PreAuthorize("hasAnyAuthority('chat:session-init:own')")
    @PostMapping("/init/{targetUserId}")
    public ResponseEntity<ResultFormat> initSession(
            @PathVariable Long targetUserId
    ) {
        chatService.initSession(targetUserId);
        return ResultResponse.success(null);
    }

    @PreAuthorize("hasAnyAuthority('chat:session-delete:own')")
    @DeleteMapping("/session/{sessionId}")
    public ResponseEntity<ResultFormat> deleteSession(
            @PathVariable Long sessionId
    ) {

        chatService.deleteSession(sessionId);
        return ResultResponse.success(null);
    }
}

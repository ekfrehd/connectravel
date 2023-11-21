package com.connectravel.controller.chat;

import com.connectravel.domain.dto.Response;
import com.connectravel.domain.dto.chat.ChatRoomDTO;
import com.connectravel.domain.entity.Member;
import com.connectravel.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/room")
@RequiredArgsConstructor
public class ChatRoomRestController {
    private final ChatRoomService chatRoomService;
    @PostMapping()
    public Response addRoom(@RequestBody ChatRoomDTO chatRoomDTO, @AuthenticationPrincipal Member user){
        chatRoomService.createChatRoomDTO(chatRoomDTO,user.getUsername());
        return Response.success("개설성공");
    }
}

package com.connectravel.controller.chat;

import com.connectravel.dto.Response;
import com.connectravel.dto.chat.ChatRoomDTO;
import com.connectravel.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
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
    public Response addRoom(@RequestBody ChatRoomDTO chatRoomDTO, Authentication authentication){
        chatRoomService.createChatRoomDTO(chatRoomDTO,authentication.getName());
        return Response.success("개설성공");
    }
}

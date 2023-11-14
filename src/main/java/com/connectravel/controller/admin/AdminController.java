package com.connectravel.controller.admin;


import com.connectravel.domain.entity.Member;
import lombok.RequiredArgsConstructor;
import org.aspectj.bridge.Message;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequiredArgsConstructor
public class AdminController {

    @GetMapping(value="/admin")
    public String home(Model model) throws Exception {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();

        String username = null;

        if (principal != null && principal instanceof Member) {
            username = ((Member) principal).getUsername();
        }

        model.addAttribute("userId", username);


        return "admin/home";
    }

    @PreAuthorize("isAuthenticated() and (( #member.username == principal.username ) or hasRole('ROLE_ADMIN'))")
    @RequestMapping( value = "", method = RequestMethod.PUT)
    public ResponseEntity<Message> updateMember(Member member ) {

       // messageService.updateMessage( member );

        return new ResponseEntity<Message>( new Message("",null, true), HttpStatus.OK );
    }

}
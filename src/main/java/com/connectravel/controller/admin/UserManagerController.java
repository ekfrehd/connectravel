package com.connectravel.controller.admin;


import com.connectravel.domain.dto.MemberDTO;
import com.connectravel.domain.entity.Member;
import com.connectravel.domain.entity.Role;
import com.connectravel.service.MemberService;
import com.connectravel.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class UserManagerController {
	
	@Autowired
	private MemberService memberService;

	@Autowired
	private RoleService roleService;

	@GetMapping(value="/admin/accounts")
	public String getMember(Model model) throws Exception {

		List<Member> accounts = memberService.getMembers();
		model.addAttribute("accounts", accounts);

		return "admin/user/list";
	}

	@PostMapping(value="/admin/accounts")
	public String updateMember(MemberDTO memberDTO) throws Exception {

		memberService.updateMember(memberDTO);

		return "redirect:/admin/accounts";
	}

	@GetMapping(value = "/admin/accounts/{id}")
	public String getMember(@PathVariable(value = "id") Long id, Model model) {

		MemberDTO memberDTO = memberService.getMember(id);
		List<Role> roleList = roleService.getRoles();

		model.addAttribute("account", memberDTO);
		model.addAttribute("roleList", roleList);

		return "admin/user/detail";
	}

	@GetMapping(value = "/admin/accounts/delete/{id}")
	public String removeMember(@PathVariable(value = "id") Long id, Model model) {

		memberService.deleteMember(id);

		return "redirect:/admin/users";
	}
}
package com.connectravel.controller.admin;

import com.connectravel.domain.dto.ResourcesDTO;
import com.connectravel.domain.entity.Resources;
import com.connectravel.domain.entity.Role;
import com.connectravel.repository.RoleRepository;
import com.connectravel.security.metaDataSource.UrlSecurityMetadataSource;
import com.connectravel.service.MethodSecurityService;
import com.connectravel.service.ResourcesService;
import com.connectravel.service.RoleService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
public class ResourcesController {
	
	@Autowired
	private ResourcesService resourcesService;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private RoleService roleService;

	@Autowired
	private UrlSecurityMetadataSource urlSecurityMetadataSource;

	@Autowired
	private MethodSecurityService methodSecurityService;

	@GetMapping(value="/admin/resources")
	public String getResources(Model model) throws Exception {

		List<Resources> resources = resourcesService.getResources();
		model.addAttribute("resources", resources);

		return "admin/resource/list";
	}

	@PostMapping(value="/admin/resources")
	public String createResources(ResourcesDTO resourcesDTO) throws Exception {

		ModelMapper modelMapper = new ModelMapper();
		Role role = roleRepository.findByRoleName(resourcesDTO.getRoleName());
		Set<Role> roles = new HashSet<>();
		roles.add(role);
		Resources resources = modelMapper.map(resourcesDTO, Resources.class);
		resources.setRoleSet(roles);

		resourcesService.createResources(resources);
		urlSecurityMetadataSource.reload();
		methodSecurityService.addMethodSecured(resourcesDTO.getResourceName(),resourcesDTO.getRoleName());

		return "redirect:/admin/resources";
	}

	@GetMapping(value="/admin/resources/register")
	public String viewRoles(Model model) throws Exception {

		List<Role> roleList = roleService.getRoles();
		model.addAttribute("roleList", roleList);

		ResourcesDTO resources = new ResourcesDTO();
		Set<Role> roleSet = new HashSet<>();
		roleSet.add(new Role());
		resources.setRoleSet(roleSet);

		model.addAttribute("resources", resources);

		return "admin/resource/detail";
	}

	@GetMapping(value="/admin/resources/{id}")
	public String getResources(@PathVariable String id, Model model) throws Exception {

		List<Role> roleList = roleService.getRoles();
        model.addAttribute("roleList", roleList);
		Resources resources = resourcesService.getResources(Long.valueOf(id));

		ModelMapper modelMapper = new ModelMapper();
		ResourcesDTO resourcesDto = modelMapper.map(resources, ResourcesDTO.class);
		model.addAttribute("resources", resourcesDto);

		return "admin/resource/detail";
	}

	@GetMapping(value="/admin/resources/delete/{id}")
	public String removeResources(@PathVariable String id, Model model) throws Exception {

		Resources resources = resourcesService.getResources(Long.valueOf(id));
		resourcesService.deleteResources(Long.valueOf(id));
		urlSecurityMetadataSource.reload();
		methodSecurityService.removeMethodSecured(resources.getResourceName());

		return "redirect:/admin/resources";
	}
}

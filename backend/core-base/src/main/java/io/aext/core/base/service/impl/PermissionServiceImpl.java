package io.aext.core.base.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.aext.core.base.enums.ResourceType;
import io.aext.core.base.model.entity.Member;
import io.aext.core.base.model.entity.Permission;
import io.aext.core.base.model.entity.Role;
import io.aext.core.base.repository.MemberRepository;
import io.aext.core.base.repository.PermissionRepository;
import io.aext.core.base.service.PermissionService;

/**
 * @author rojar
 *
 * @date 2021-06-19
 */
@Service
public class PermissionServiceImpl implements PermissionService {
	@Autowired
	MemberRepository memberRepository;

	@Autowired
	PermissionRepository permissionRepository;

	@Override
	public List<Permission> getIdsByUserId(Long userId) {
		List<Permission> permissions = new ArrayList<Permission>();
		Optional<Member> member = memberRepository.findById(userId);
		if (member.isEmpty()) {
			return permissions;
		}

		for (Role role : member.get().getRoleList()) {
			for (Permission permission : role.getPermissionList()) {
				permissions.add(permission);
			}
		}

		return permissions;
	}

	@Override
	public List<Permission> getAllByTypeEquals(ResourceType type) {
		return permissionRepository.getAllByTypeEquals(type);
	}

	@Override
	public Optional<Permission> findById(Long id) {
		return permissionRepository.findById(id);
	}

	@Override
	public int deleteByType(ResourceType type) {
		List<Permission> data = permissionRepository.getAllByTypeEquals(type);
		permissionRepository.deleteAll(data);
		return data.size();
	}

	@Override
	public List<Permission> save(List<Permission> resource) {
		return permissionRepository.saveAll(resource);
	}

	@Override
	public Permission save(Permission resource) {
		return permissionRepository.save(resource);
	}

}

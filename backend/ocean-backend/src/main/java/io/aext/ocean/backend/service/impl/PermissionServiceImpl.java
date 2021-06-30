package io.aext.ocean.backend.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import io.aext.ocean.backend.enums.ResourceType;
import io.aext.ocean.backend.model.entity.Member;
import io.aext.ocean.backend.model.entity.Permission;
import io.aext.ocean.backend.model.entity.Role;
import io.aext.ocean.backend.repository.MemberRepository;
import io.aext.ocean.backend.repository.PermissionRepository;
import io.aext.ocean.backend.service.PermissionService;

/**
 * @author Rojar Smith
 *
 * @date 2021-06-30
 */
@Service
public class PermissionServiceImpl implements PermissionService {
	@Autowired
	MemberRepository memberRepository;

	@Autowired
	PermissionRepository permissionRepository;

	@Override
	public Optional<Permission> readPermission(Long id) {
		return permissionRepository.findById(id);
	}

	/*
	 * Include all roles.
	 */
	@Override
	public List<Permission> readPermissions(Long userId) {
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
	public List<Permission> readPermissions(ResourceType type) {
		return permissionRepository.getAllByTypeEquals(type);
	}

	@Override
	public List<Permission> readPermissions(ResourceType type, PageRequest page) {
		@SuppressWarnings("serial")
		Specification<Permission> s1 = new Specification<Permission>() {

			@Override
			public Predicate toPredicate(@SuppressWarnings("rawtypes") Root root,
					@SuppressWarnings("rawtypes") CriteriaQuery query, CriteriaBuilder criteriaBuilder) {
				Predicate p1 = criteriaBuilder.equal(root.get("type"), type);
				return p1;
			}
		};

		Page<Permission> permissions = permissionRepository.findAll(Specification.where(s1), page);

		return permissions.getContent();
	}

	@Override
	public List<Permission> update(List<Permission> resource) {
		return permissionRepository.saveAll(resource);
	}

	@Override
	public Permission update(Permission resource) {
		return permissionRepository.save(resource);
	}

	@Override
	public int delete(ResourceType type) {
		List<Permission> data = permissionRepository.getAllByTypeEquals(type);
		permissionRepository.deleteAll(data);
		return data.size();
	}

}

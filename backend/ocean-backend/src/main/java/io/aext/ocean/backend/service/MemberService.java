package io.aext.ocean.backend.service;

import java.util.List;
import java.util.Optional;

import io.aext.ocean.backend.model.entity.Member;

/**
 * @author Rojar Smith
 *
 * @date 2021-06-30
 */
public interface MemberService extends BaseService<MemberService> {
	Optional<Member> findByUsername(String username);

	Optional<Member> findByEmail(String email);

	boolean isEmailExist(String email);

	boolean isUsernameExist(String username);

	List<Member> update(List<Member> members);

	Member update(Member member);
}

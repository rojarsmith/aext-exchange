package io.aext.core.base.service;

import java.util.List;
import java.util.Optional;

import io.aext.core.base.model.entity.Member;

/**
 * @author rojar
 *
 * @date 2021-06-25
 */
public interface MemberService extends BaseService<MemberService> {
	Optional<Member> findByUsername(String username);

	Optional<Member> findByEmail(String email);

	boolean isEmailExist(String email);

	boolean isUsernameExist(String username);

	List<Member> update(List<Member> members);

	Member update(Member member);
}

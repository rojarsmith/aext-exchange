package io.aext.core.base.dao;

import java.util.List;
import java.util.Optional;

import io.aext.core.base.entity.Member;
import io.aext.core.base.repository.BaseRepository;

/**
 * @author rojar
 *
 * @date 2021-06-06
 */
public interface MemberDao extends BaseRepository<Member> {
	List<Member> getAllByEmailEquals(String email);

	List<Member> getAllByUsernameEquals(String username);

	Optional<Member> findByUsername(String username);

	Optional<Member> findMemberByEmail(String email);
}

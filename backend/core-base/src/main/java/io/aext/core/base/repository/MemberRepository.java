package io.aext.core.base.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import io.aext.core.base.model.entity.Member;

/**
 * @author rojar
 *
 * @date 2021-06-19
 */
@Repository
public interface MemberRepository extends BaseRepository<Member> {
	List<Member> getAllByEmailEquals(String email);

	List<Member> getAllByUsernameEquals(String username);

	Optional<Member> findByUsername(String username);

	Optional<Member> findMemberByEmail(String email);
}

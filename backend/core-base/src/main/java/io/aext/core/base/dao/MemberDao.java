package io.aext.core.base.dao;

import java.util.List;

import io.aext.core.base.entity.Member;

/**
 * @author rojar
 *
 * @date 2021-06-05
 */
public interface MemberDao extends BaseDao<Member> {
	List<Member> getAllByEmailEquals(String email);
}

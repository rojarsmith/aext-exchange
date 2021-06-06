package io.aext.core.base.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.aext.core.base.util.Foo;

/**
 * @author rojar
 *
 * @date 2021-06-05
 */
@Service
public class MemberService extends BaseService<MemberService> {
//	@Autowired
//	private MemberDao memberDao;

	@Autowired
	private Foo foo;

	public boolean isEmailExist(String email) {
//		List<Member> list = memberDao.getAllByEmailEquals(email);
//		return list.size() > 0 ? true : false;

		return false;
	}
}

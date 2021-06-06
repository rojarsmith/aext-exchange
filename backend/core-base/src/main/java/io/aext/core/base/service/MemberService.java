package io.aext.core.base.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.aext.core.base.dao.MemberDao;
import io.aext.core.base.entity.Member;

/**
 * @author rojar
 *
 * @date 2021-06-05
 */
@Service
public class MemberService extends BaseService<MemberService> {
	@Autowired
	private MemberDao memberDao;

	public boolean isEmailExist(String email) {
		List<Member> list = memberDao.getAllByEmailEquals(email);
		return list.size() > 0 ? true : false;
	}

	public Member save(Member member) {
		return memberDao.save(member);
	}
}

package io.aext.ocean.backend.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.aext.ocean.backend.model.entity.Member;
import io.aext.ocean.backend.repository.MemberRepository;
import io.aext.ocean.backend.service.MemberService;

/**
 * @author Rojar Smith
 *
 * @date 2021-06-30
 */
@Service
public class MemberServiceImpl implements MemberService {
	@Autowired
	MemberRepository memberRepository;

	@Override
	public Optional<Member> findByUsername(String username) {
		return memberRepository.findByUsername(username);
	}

	@Override
	public Optional<Member> findByEmail(String email) {
		return memberRepository.findMemberByEmail(email);
	}

	@Override
	public boolean isEmailExist(String email) {
		return memberRepository.getAllByEmailEquals(email).size() > 0 ? true : false;
	}

	@Override
	public boolean isUsernameExist(String username) {
		return memberRepository.getAllByUsernameEquals(username).size() > 0 ? true : false;
	}

	@Override
	public Member update(Member member) {
		return memberRepository.save(member);
	}

	@Override
	public List<Member> update(List<Member> members) {
		return memberRepository.saveAll(members);
	}
}

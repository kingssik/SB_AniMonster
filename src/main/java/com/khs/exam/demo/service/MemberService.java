package com.khs.exam.demo.service;

import org.springframework.stereotype.Service;

import com.khs.exam.demo.repository.MemberRepository;
import com.khs.exam.demo.util.Ut;
import com.khs.exam.demo.vo.Member;
import com.khs.exam.demo.vo.ResultData;

@Service
public class MemberService {
	private MemberRepository memberRepository;
	private AttrService attrService;

	public MemberService(AttrService attrService, MemberRepository memberRepository) {
		this.attrService = attrService;
		this.memberRepository = memberRepository;
	}

	public ResultData<Integer> join(String loginId, String loginPw, String name, String nickname, String cellphoneNum,
			String email) {
		// 로그인아이디 중복체크
		Member existsMember = getMemberByLoginId(loginId);

		if (existsMember != null) {
			return ResultData.from("F-7", Ut.f("이미 사용중인 아이디(%s)입니다", loginId));
		}

		// 이름 + 이메일 중복체크
		existsMember = getMemberByNameAndEmail(name, email);

		if (existsMember != null) {
			return ResultData.from("F-8", Ut.f("이미 사용중인 이름(%s)과 이메일(%s)입니다", name, email));
		}

		loginPw = Ut.sha256(loginPw);

		memberRepository.join(loginId, loginPw, name, nickname, cellphoneNum, email);
		int id = memberRepository.getLastInsertId();

		return ResultData.from("S-1", "회원가입이 완료되었습니다", "id", id);
	}

	private Member getMemberByNameAndEmail(String name, String email) {
		return memberRepository.getMemberByNameAndEmail(name, email);

	}

	public Member getMemberByLoginId(String loginId) {
		return memberRepository.getMemberByLoginId(loginId);

	}

	public Member getMemberById(int id) {
		return memberRepository.getMemberById(id);
	}

	public ResultData modify(int id, String loginPw, String name, String nickname, String cellphoneNum, String email) {
		loginPw = Ut.sha256(loginPw);
		memberRepository.modify(id, loginPw, name, nickname, cellphoneNum, email);
		return ResultData.from("S-1", "회원정보가 수정되었습니다");
	}

	public String genMemberModifyAuthKey(int actorId) {
		String memberModifyAuthKey = Ut.getTempPassword(10);

		attrService.setValue("member", actorId, "extra", "memberModifyAuthKey", memberModifyAuthKey,
				Ut.getDateStrLater(60 * 5));

		return memberModifyAuthKey;
	}

	public ResultData checkMemberModifyAuthKey(int actorId, String memberModifyAuthKey) {
		String saved = attrService.getValue("member", actorId, "extra", "memberModifyAuthKey");

		if (!saved.equals(memberModifyAuthKey)) {
			return ResultData.from("F-1", "일치하지 않거나 만료되었습니다");
		}

		return ResultData.from("S-1", "정상 코드입니다");
	}

	public int idCheck(String id) {
		int cnt = memberRepository.idCheck(id);
		System.out.println("cnt: " + cnt);
		return cnt;
	}
}
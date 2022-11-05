package com.khs.exam.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.khs.exam.demo.service.MemberService;
import com.khs.exam.demo.util.Ut;
import com.khs.exam.demo.vo.Member;
import com.khs.exam.demo.vo.ResultData;
import com.khs.exam.demo.vo.Rq;

@Controller
public class UsrMemberController {

	@Autowired
	private MemberService memberService;
	@Autowired
	private Rq rq;

	@RequestMapping("usr/member/join")
	public String showJoin(String loginId, String loginPw, String loginPwConfirm, String name, String nickname,
			String cellphoneNum, String email) {
		return "usr/member/join";
	}

	@RequestMapping("usr/member/doJoin")
	@ResponseBody
	public String doJoin(String loginId, String loginPw, String loginPwConfirm, String name, String nickname,
			String cellphoneNum, String email) {

		if (Ut.empty(loginId)) {
			return Ut.jsHistoryBack("아이디를 입력하세요");
		}
		if (Ut.empty(loginPw)) {
			return Ut.jsHistoryBack("비밀번호를 입력하세요");
		}
		if (Ut.empty(name)) {
			return Ut.jsHistoryBack("이름을 입력하세요");
		}
		if (Ut.empty(nickname)) {
			return Ut.jsHistoryBack("닉네임을 입력하세요");
		}
		if (Ut.empty(cellphoneNum)) {
			return Ut.jsHistoryBack("전화번호를 입력하세요");
		}
		if (Ut.empty(email)) {
			return Ut.jsHistoryBack("이메일을 입력하세요");
		}

		String joinRd = memberService.join(loginId, loginPw, loginPwConfirm, name, nickname, cellphoneNum, email);

		if (joinRd.isEmpty()) {
			return joinRd;
		}

		return joinRd;
	}

	@RequestMapping("usr/member/login")
	public String showLogin() {
		return "usr/member/login";
	}

	@RequestMapping("usr/member/doLogin")
	@ResponseBody
	public String doLogin(String loginId, String loginPw) {

		if (Ut.empty(loginId)) {
			return Ut.jsHistoryBack("아이디를 입력하세요");
		}

		if (Ut.empty(loginPw)) {
			return Ut.jsHistoryBack("비밀번호를 입력하세요");
		}

		Member member = memberService.getMemberByLoginId(loginId);

		if (member == null) {
			return Ut.jsHistoryBack("아이디를 잘못 입력했습니다");
		}

		if (member.getLoginPw().equals(loginPw) == false) {
			return Ut.jsHistoryBack("비밀번호가 일치하지 않습니다");
		}

		rq.login(member);

		return Ut.jsReplace(Ut.f("%s님 안녕하세요", member.getNickname()), "/");
	}

	@RequestMapping("usr/member/doLogout")
	@ResponseBody
	public String doLogout() {

		rq.logout();

		return Ut.jsReplace("로그아웃 되었습니다", "/");
	}

	@RequestMapping("usr/member/myPage")
	public String showMyPage() {

		return "usr/member/myPage";
	}

	@RequestMapping("/usr/member/checkPassword")
	public String showcheckPassword() {

		return "usr/member/checkPassword";
	}

	@RequestMapping("/usr/member/doCheckPassword")
	@ResponseBody
	public String doCheckPassword(String loginPw, String replaceUri) {
		if (Ut.empty(loginPw)) {
			return rq.jsHistoryBack("비밀번호를 입력하세요");
		}

		if (rq.getLoginedMember().getLoginPw().equals(loginPw) == false) {
			return rq.jsHistoryBack("비밀번호가 일치하지 않습니다");
		}

		return rq.jsReplace("", replaceUri);
	}

	@RequestMapping("/usr/member/modify")
	public String showModify() {
		return "usr/member/modify";
	}

	@RequestMapping("/usr/member/doModify")
	@ResponseBody
	public String doModify(String loginPw, String name, String nickname, String cellphoneNum, String email) {
		if (Ut.empty(loginPw)) {
			loginPw = null;
		}
		if (Ut.empty(name)) {
			return rq.jsHistoryBack("이름을 입력하세요");
		}
		if (Ut.empty(nickname)) {
			return rq.jsHistoryBack("닉네임을 입력하세요");
		}
		if (Ut.empty(cellphoneNum)) {
			return rq.jsHistoryBack("전화번호를 입력하세요");
		}
		if (Ut.empty(email)) {
			return rq.jsHistoryBack("이메일을 입력하세요");
		}

		ResultData modifyRd = memberService.modify(rq.getLoginedMemberId(), loginPw, name, nickname, cellphoneNum,
				email);

		return rq.jsReplace(modifyRd.getMsg(), "/");

	}

}
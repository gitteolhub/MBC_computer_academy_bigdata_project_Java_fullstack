package com.javateam.healthyFoodProject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.javateam.healthyFoodProject.domain.CustomUser;
import com.javateam.healthyFoodProject.domain.MemberDTO;
import com.javateam.healthyFoodProject.service.MemberService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class AuthController {
	@Autowired
	CaptchaController captchaController;

	@Autowired
	private MemberService memberService;
	private int loginErrorCount = -1;
	private int maxCount = 3;

	// 홈 페이지로 redirection (return문 >> 경로)
	@RequestMapping("/")
	public String root() {

		log.info("[root]");
		return "home";
	}

	@RequestMapping("/home")
	public String home() {

		log.info("[home]");
		return "/home";
	}

	// 당뇨 식단 페이지
	@GetMapping("/foodMenu")
	public String foodMenu() {

		log.info("[foodMenu]");
		return "foodMenu";
	}

	@GetMapping({"/login/oauth2/authorization/naver",
				 "/login/oauth2/authorization/google",
				 "/welcome"})
	public String welcome() {

		log.info("[welcome]");
		return "welcome";
	}


	// 회원 마이 페이지
	@GetMapping("/myPage")
	public String myPage() {

		log.info("[myPage]");
		return "myPage";
	}

	// 관리자용 주소
    @GetMapping("/admin/home")
	public String securedAdminHome(ModelMap model) {

    	log.info("[/admin/home]");

    	// Security pricipal 객체 (사용자 인증/인가 정보 객체)
		Object principal = SecurityContextHolder.getContext()
												.getAuthentication()
												.getPrincipal();

		CustomUser customUser = null;

		if (principal instanceof CustomUser) {
			customUser = ((CustomUser)principal);
		}

		log.info("[customUser]: {}", customUser);

		String id = customUser.getUsername();
		model.addAttribute("userid", id);
		model.addAttribute("msg", "관리자 페이지입니다.");

		return "/admin/home";
    }


//	// (일반)회원용 주소
//	@GetMapping("/secured/home")
//	public String securedHome(ModelMap model) {
//
//		log.info("[/secured/home]");
//
//		// 인증된 회원의 정보를 가져옴
//		Object principal = SecurityContextHolder.getContext()
//												.getAuthentication()
//												.getPrincipal();
//
//		CustomUser customUser = null;
//
//		if (principal instanceof CustomUser) {
//			customUser = ((CustomUser)principal);
//		}
//		log.info("[user: {}]", user);
//
//		String ID = customUser.getUsername();
//		model.addAttribute("username", ID);
//		model.addAttribute("message", "회원 페이지입니다.");
//
//		return "/secured/home";
//	}

//	// 회원가입 폼
//	@GetMapping("/joinAjaxDemo")
//	public String demo(Model model) {
//
//		log.info("회원가입 폼");
//		model.addAttribute("memberDTO", new MemberVO());
//
//		return "joinAjaxDemo";
//	}

	// 회원가입 폼
	@GetMapping("/join")
	public String join(Model model) {

		log.info("회원가입 폼");
		model.addAttribute("memberDTO", new MemberDTO());

		return "join";
	}

	// 회원가입 폼(Demo)
	@GetMapping("/joinDemo")
	public String joinDemo(Model model) {

		log.info("회원가입 폼(Demo)");
		model.addAttribute("memberDTO", new MemberDTO());

		return "joinDemo";
	}

//	// 회원가입 폼(Ajax)
//		@GetMapping("/joinAjax")
//		public String joinAjax(Model model) {
//
//			log.info("회원가입 폼(Ajax)");
//			model.addAttribute("memberDTO", new MemberDTO());
//
//			return "joinAjax";
//		}

	// 로그인 폼
	@GetMapping("/loginForm")
	public String login( HttpServletRequest request, Model model) {	//RedirectAttributes redirectAttributes

		log.info("[loginForm]");
		String error = request.getParameter("error")== null ? "없음" : request.getParameter("error");
		String msg   = request.getParameter("msg")  == null ? "없음" : request.getParameter("msg");
		log.info("[loginForm][error]: {}", error);
		log.info("[loginForm][msg]: {}", msg);

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		log.info("login 인증정보: {}", auth);

		String movePath="";

		if(auth.getPrincipal() == null || auth.getPrincipal().toString().equals("anonymousUser")) {
			loginErrorCount += 1;

			log.info("로그인 인증 안됨");
			model.addAttribute("error", error);
			model.addAttribute("msg", msg);

			log.info("[loginErrorCount: {}]", loginErrorCount);
			if(loginErrorCount >= maxCount) {
				log.info("[if(loginErrorCount >= maxCount)]");
				captchaController.login(model);
			}

			movePath = "loginForm";

		} else {
			log.info("로그인 인증됨");
			movePath = "myPage";
		}

		return movePath;
	}

	// 로그아웃 처리 메서드
	@GetMapping("/logoutProc")
	public String logout(ModelMap model, HttpServletRequest request, HttpServletResponse response) {

		log.info("[logout]");

		// 현재 인증 정보
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		log.info("[auth]:{}", auth);

		// 인증 정보가 존재하는 경우 로그아웃
		if(auth != null) {
			new SecurityContextLogoutHandler().logout(request, response, auth);
		}

		loginErrorCount = -1;
		return "logout";
	}

	// 로그인(인증) 오류 처리 메서드
	@GetMapping("/loginError")
	public String loginError(HttpSession session, RedirectAttributes redirectAttributes) {		// RedirectAttributes redirectAttributes

		// 마지막 보안 예외 가져오기
		Exception secuSess =(Exception)session.getAttribute("SPRING_SECURITY_LAST_EXCEPTION");
		log.info("[인증 오류: {}]", secuSess.getMessage());

		// 오류 flag 추가
		redirectAttributes.addAttribute("error", "true");

		// 오류 메서드 추가
		redirectAttributes.addAttribute("msg", secuSess.getMessage());

		return "redirect:/loginForm";
	}

	// 사용자 ID 중복 확인 메서드
	@RequestMapping("/login/idCheck")
	@ResponseBody
	public String idCheck(@RequestParam("username") String username, Model model) {

		log.info("[username]: {}", username);

		// 아이디 중복 확인
		boolean flag = memberService.hasMemberByFld("ID",username);
		log.info("[flag]: {}", flag);

		return flag + "";
	}

	// 접근 거부 페이지
	@GetMapping("/403")
	public String error403() {

		return "/error/403";
	}
}

package edu.du.proj1120.controller;

import edu.du.proj1120.spring.DuplicateMemberException;
import edu.du.proj1120.spring.MemberRegisterService;
import edu.du.proj1120.spring.RegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


@Controller
public class RegisterController {

	@Autowired
	private MemberRegisterService memberRegisterService;


	@Autowired
	private RegisterRequestValidator registerRequestValidator;  // RegisterRequestValidator 주입




	@GetMapping("/register")
	public String root() {
		return "redirect:/register/step1";
	}

	@RequestMapping("/register/step1")
	public String handleStep1() {
		return "register/step1";
	}

	@PostMapping("/register/step2")
	public String handleStep2(
			@RequestParam(value = "agree", defaultValue = "false") Boolean agree,
			Model model) {
		if (!agree) {
			return "register/step1";
		}
		model.addAttribute("registerRequest", new RegisterRequest());
		return "register/step2";
	}

	@GetMapping("/register/step2")
	public String handleStep2Get() {
		return "redirect:/register/step1";
	}

//	@PostMapping("/register/step3")
//	public String handleStep3(RegisterRequest regReq) {
//		try {
//			memberRegisterService.regist(regReq);
//			return "register/step3";
//		} catch (DuplicateMemberException ex) {
//			return "register/step2";
//		}
//	}

	// 회원가입 3단계
	@PostMapping("/register/step3")
	public String handleStep3(@ModelAttribute RegisterRequest regReq, BindingResult result) {
		// RegisterRequestValidator를 수동으로 호출하여 검증
		registerRequestValidator.validate(regReq, result);

		// 검증 오류가 있으면 다시 step2로 돌아감
		if (result.hasErrors()) {
			return "register/step2";
		}

		try {
			memberRegisterService.regist(regReq);  // 회원가입 처리
			return "register/step3";  // 회원가입 완료 페이지
		} catch (DuplicateMemberException ex) {
			result.rejectValue("email", "duplicate", "이미 존재하는 이메일입니다.");
			return "register/step2";  // 중복 이메일이 있는 경우 다시 step2로 돌아감
		}
	}

}

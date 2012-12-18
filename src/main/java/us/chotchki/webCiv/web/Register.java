package us.chotchki.webCiv.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class Register {
	@RequestMapping("/register/available")
	public @ResponseBody String available(@RequestParam("nickname") String nickname){
		return nickname;
	}
}

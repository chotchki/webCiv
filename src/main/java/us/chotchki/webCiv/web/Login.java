package us.chotchki.webCiv.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class Login {
	@RequestMapping("/login")
	public String index(){
		return "login";
	}
}
package us.chotchki.webCiv.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class Signin {
	@RequestMapping("/signin")
	public String index(){
		return "signin";
	}
}

package us.chotchki.webCiv.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class Profile {
	@RequestMapping("/profile")
	public String index(){
		return "profile";
	}
}

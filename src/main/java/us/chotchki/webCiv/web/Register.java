package us.chotchki.webCiv.web;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import us.chotchki.webCiv.db.pojo.Player;
import us.chotchki.webCiv.db.pojo.User;
import us.chotchki.webCiv.form.pojo.RegistrationForm;
import us.chotchki.webCiv.security.SHA512PasswordEncoder;
import us.chotchki.webCiv.service.PlayerService;

@Controller
public class Register {
	private static final Logger log = LoggerFactory.getLogger(Register.class);
	
	@Autowired
	PlayerService playerService = null;
	
	@RequestMapping("/register/available")
	public @ResponseBody String available(@RequestParam("nickname") String nickname){
		return nickname;
	}
	
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public String register(RedirectAttributes redirect, @Valid RegistrationForm rf, BindingResult br){
		if(br.hasErrors()){
			if(br.hasFieldErrors()){
				for(FieldError fe: br.getFieldErrors()){
					switch(fe.getField()){
					case "user.username":
						redirect.addFlashAttribute("errorUsername", fe.getDefaultMessage());
						break;
					case "user.password":
						redirect.addFlashAttribute("errorPassword", fe.getDefaultMessage());
						break;
					case "retypePassword":
						redirect.addFlashAttribute("errorRetypePassword", fe.getDefaultMessage());
						break;
					default:
						log.error("Got a field error on a field that doesn't exist {}", fe.getField());
					}
				}
			} else {
				redirect.addFlashAttribute("error", "There was an error registering, please try again later.");
			}
			return "redirect:/signin";
		}
		
		if(!rf.getRetypePassword().equals(rf.getUser().getPassword())){
			redirect.addFlashAttribute("errorRetypePassword", "The password and the retype password must match.");
			return "redirect:/signin";
		}
		
		Player p = new Player();
		p.setNickname(rf.getUser().getUsername());
		
		User u = new User();
		u.setUsername(rf.getUser().getUsername());
		u.setPassword(rf.getUser().getPassword());
		
		playerService.register(p, u);
		redirect.addFlashAttribute("successRegister", "Welcome to WebCiv " + rf.getUser().getUsername() + "!");
		
		//Spring log us in
		Authentication auth = new UsernamePasswordAuthenticationToken(u.getUsername(), u.getPassword());
		SecurityContextHolder.getContext().setAuthentication(auth);
		 
		return "redirect:/";
	}
}

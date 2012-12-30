package us.chotchki.webCiv.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class ManageGames {

	@RequestMapping("/games")
	public String index() {
		return "listGames";
	}

	@RequestMapping(value = "/games/create", method = RequestMethod.GET)
	public String createView() {
		return "index";
	}
	
	@RequestMapping(value = "/games/create", method = RequestMethod.POST)
	public String createBackend() {
		return "index";
	}
}

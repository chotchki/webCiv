package us.chotchki.webCiv.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import us.chotchki.webCiv.db.pojo.Player;
import us.chotchki.webCiv.service.PlayerService;

@Controller
public class Index {
	
	@Autowired
	PlayerService playerService = null;
	
	@RequestMapping("/")
	public String index(){
		Player p = new Player();
		p.setNickname("Foo");
		playerService.create(p);
		
		return "index";
	}
}

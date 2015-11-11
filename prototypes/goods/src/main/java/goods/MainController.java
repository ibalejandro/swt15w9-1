package goods;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {
	
	TextBlockDB goodRepository;
	
	@Autowired
	public MainController(){
	}
	
	@RequestMapping({"/","index.html"})
	String index(){
		return "Startseite";
	}
	
	@RequestMapping("/mainframegoods")
	TexBlock getnewestGoods(HttpServletRequest request){
		TexBlock requestedGood=null;
		int i=0;
		for(TexBlock Good:goodRepository.findAll()){
			if(i==Integer.parseInt(request.getParameter("ShownGoodNr"))){
				requestedGood=Good;
				break;
			}
			i++;
		}
		return requestedGood;
	}

}

package userManagement;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.web.LoggedIn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import userManagement.model.GoodEntity;
import userManagement.model.GoodRepository;
/////////////////////////////////////////////////
import userManagement.model.User;
import userManagement.model.UserRepository;
///////////////////////////////////////////////end


@Controller
public class GoodsOfferController {

	//@Autowired GoodRepository repository;
	/////////////////////////////////////////////////////////////
	//@Autowired UserRepository repositoryUser;
	private final UserRepository repositoryUser;
	private final GoodRepository repository;
	
	@Autowired
	public GoodsOfferController(UserRepository userRepository,GoodRepository repository ){
		this.repositoryUser=userRepository;
		this.repository=repository;
	}
	/////////////////////////////////////////////////////////end
	
	@RequestMapping(value = "/saveGood", method = RequestMethod.POST)
	    public String saveGood(HttpServletRequest request, Model model, @LoggedIn Optional<UserAccount> userAccount) {//!!!!
			String name = request.getParameter("name");
	    	String description = request.getParameter("description");
	    	String tagsString = request.getParameter("tags");
	    	
	    	//////////////////////////////////////////////////////////////
	    	if(!(userAccount.isPresent())){
	    		return "errorpage0_empty";
	    	}
			User user= repositoryUser.findByUserAccount(userAccount.get());
	    	//////////////////////////////////////////////////////////////end
	    	
	    	Set<String> tags = new HashSet<String>
	    					   (Arrays.asList(tagsString.split(", ")));
	    	
	    	GoodEntity good = new GoodEntity(name, description, tags/* ,user*/);//!!!!
	    
	    	
	    	/*
	    	 * This is just for the examples. The userId will be the real id of
	    	 * the user, who is offering the good.
	    	 */
	    	long userId = 1;
	    	
	    	repository.save(good);
	    	/////////////////////////////////////////////////////////////
	    	user.addGood(good);
	    	////////////////////////////////////////////////////////////end
	    	
			
			model.addAttribute("result", repository.findAll());
			return "offeredGood";
	    }
	
		@RequestMapping(value = "/listAllGoods", method = RequestMethod.GET)
	    public String listAllGoods(Model model) {
	        model.addAttribute("result", repository.findAll());
			return "home";
	    }
	}

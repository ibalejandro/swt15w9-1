package com.example;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class GoodsController {
	
	private static final Logger log = LoggerFactory.getLogger
								      (GoodsPrototypeApplication.class);
	
	@Autowired GoodRepository repository;

    @RequestMapping("/index")
    public String index(@RequestParam(value="name", required=false,
    				   defaultValue="World") String name, Model model) {
        model.addAttribute("name", name);
        return "index";
    }
    
    @RequestMapping(value = "/saveGood", method = RequestMethod.POST)
    public String savedGood(HttpServletRequest request, Model model) {
    	String addressStreet = request.getParameter("addressStreet");
    	int addressNumber = Integer.parseInt
    					    (request.getParameter("addressNumber"));
    	int addressPostcode = Integer.parseInt
    						  (request.getParameter("addressPostcode"));
    	String addressCity = request.getParameter("addressCity");
    	
    	Address address = new Address(addressStreet, addressNumber,
    								  addressPostcode, addressCity);
    	
    	String userFirstName = request.getParameter("userFirstName");
    	String userLastName = request.getParameter("userLastName");
    	String userEmail = request.getParameter("userEmail");
    	String userOrigin = request.getParameter("userOrigin");
    	
    	User user = new User(userFirstName, userLastName, userEmail, userOrigin, 
    					     address);
    	
    	String goodName = request.getParameter("goodName");
    	String goodDescription = request.getParameter("goodDescription");
    	String goodTag = request.getParameter("goodTag");
    	
    	Good good = new Good(goodName, goodDescription, goodTag, user);
    			
    	repository.save(good);
    	
    	log.info("");
		log.info("Goods found with findAll():");
		log.info("-------------------------------");
		for (Good g : repository.findAll()) {
			log.info(g.toString());
		}
		log.info("-------------------------------");
		
		model.addAttribute("response", repository.findAll());
		return "home";
    }
    
    @RequestMapping(value = "/listAllGoods", method = RequestMethod.GET)
    public String listAllGoods(Model model) {
		
    	log.info("");
		log.info("Goods found with findAll():");
		log.info("-------------------------------");
		for (Good good : repository.findAll()) {
			log.info(good.toString());
		}
		log.info("-------------------------------");
        log.info("");
		
        model.addAttribute("response", repository.findAll());
		return "home";
    }
    
    @RequestMapping(value = "/orderGoodsByTag", method = RequestMethod.GET)
    public String orderGoodsByTag(Model model) {
		
    	log.info("Goods order with findAllByOrderByTagAsc():");
		log.info("-------------------------------");
		for (Good orderAscGood : repository.findAllByOrderByTagAsc()) {
			log.info(orderAscGood.toString());
		}
		log.info("-------------------------------");
		log.info("");
		
		model.addAttribute("response", repository.findAllByOrderByTagAsc());
		return "home";
    }
    
    @RequestMapping(value = "/searchGoodByName", method = RequestMethod.POST)
    public String searchGoodByName(HttpServletRequest request, Model model) {
    	
		String goodName = request.getParameter("goodName");
		log.info("Goods found by name: '" + goodName + "':");
		log.info("-------------------------------");
		for (Good goodFoundByName : 
			 repository.findByNameStartingWithIgnoreCase(goodName)) {
			log.info(goodFoundByName.toString());
		}
		log.info("-------------------------------");
		log.info("");
		
		model
		.addAttribute("response", 
					  repository.findByNameStartingWithIgnoreCase(goodName));
		return "home";
    }
    
    @RequestMapping(value = "/searchGoodByTag", method = RequestMethod.POST)
    public String searchGoodByTag(HttpServletRequest request, Model model) {
    	
		String goodTag = request.getParameter("goodTag");
		log.info("Goods found by tag: '" + goodTag + "':");
		log.info("-------------------------------");
	for (Good goodFoundByTag : 
		 repository.findByTagStartingWithIgnoreCase(goodTag)) {
			log.info(goodFoundByTag.toString());
		}
		log.info("-------------------------------");
		log.info("");
		
		model.addAttribute("response", 
						   repository.findByTagStartingWithIgnoreCase(goodTag));
		return "home";
    }
    
}

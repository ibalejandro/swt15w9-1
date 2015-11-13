package com.example.controller;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.example.entity.GoodEntity;
import com.example.model.Good;
import com.example.repository.GoodRepository;


@Controller
public class GoodsOfferController {

	@Autowired GoodRepository repository;
	
	@RequestMapping(value = "/home", method = RequestMethod.GET)
    public String listAllGoods(Model model) {
        model.addAttribute("result", repository.findAll());
		return "home";
    }
	
	@RequestMapping(value = "/offeredGood", method = RequestMethod.POST)
    public String saveGood(HttpServletRequest request, Model model) {
		String name = request.getParameter("name");
    	String description = request.getParameter("description");
    	String tagsString = request.getParameter("tags");
    	
    	Set<String> tags = new HashSet<String>
    					   (Arrays.asList(tagsString.split(", ")));
    	
    	Good good = new Good(name, description, tags);
    	
    	/*
    	 * This is just for the examples. The userId will be the real id of
    	 * the user, who is offering the good.
    	 */
    	long userId = 1L;
    	
    	GoodEntity goodEntity = good.createGoodEntity(userId);
    	
		model.addAttribute("result", repository.save(goodEntity));
		return "offeredGood";
    }

}

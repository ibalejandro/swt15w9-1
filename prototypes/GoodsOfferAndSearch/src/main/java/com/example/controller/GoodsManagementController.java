package com.example.controller;

import java.util.HashSet;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.example.entity.GoodEntity;
import com.example.repository.GoodRepository;

@Controller
public class GoodsManagementController {

	@Autowired GoodRepository repository;
	
	@RequestMapping(value = "/myOfferedGoods", method = RequestMethod.GET)
    public String listUserOfferedGoods(Model model) {
		/*
    	 * This is just for the examples. The userId will be the real id of
    	 * the user, who wants to see his offered goods.
    	 */
    	long userId = 1L;
		
        model.addAttribute("result", repository.findByUserId(userId));
		return "myOfferedGoods";
    }
	
	@RequestMapping(value = "/deletedGood", method = RequestMethod.POST)
    public String deleteGood(HttpServletRequest request, Model model) {
		long id = Long.parseLong(request.getParameter("id"));
		
		GoodEntity good = repository.findOne(id);
		
		// This statement check if the entity to delete actually exists.
    	if (good != null) repository.delete(id);
    	// If the entity doesn't exist, an empty entity is returned.
    	else {
    		Set<String> emptyTags = new HashSet<>();
    		long invalidUserId = -1L;
    		good = new GoodEntity("", "", emptyTags, invalidUserId);
    	}
    	
    	model.addAttribute("result", good);
		return "deletedGood";
    }
	
}

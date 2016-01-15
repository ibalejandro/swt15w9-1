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
	
	@RequestMapping(value = "/update", method = RequestMethod.POST)
    public String showGoodToUpdate(HttpServletRequest request, Model model) {
		long id = Long.parseLong(request.getParameter("id"));
		
		GoodEntity good = repository.findOne(id);
		
		String tagsAsString = "";
		
		/* 
		 * If the entity exists a parsed string for the tags is built, so that
		 * they can be easily updated by the user.
		 */
		if (good != null) {
			tagsAsString = good.getTagsAsString();
		}
		// If the entity doesn't exist, an empty entity is returned.
		else {
    		Set<String> emptyTags = new HashSet<>();
    		long invalidUserId = -1L;
    		good = new GoodEntity("", "", emptyTags, invalidUserId);
    	}
    	
    	model.addAttribute("result", good);
    	model.addAttribute("parsedTags", tagsAsString);
		return "update";
    }
	
	@RequestMapping(value = "/updatedGood", method = RequestMethod.POST)
    public String updateGood(HttpServletRequest request, Model model) {
		long id = Long.parseLong(request.getParameter("id"));
		
		GoodEntity goodToBeUpdated = repository.findOne(id);
		
		String name = request.getParameter("name");
    	String description = request.getParameter("description");
    	String tagsString = request.getParameter("tags");
    	
    	Set<String> tags = new HashSet<String>
    					   (Arrays.asList(tagsString.split(", ")));
    	
    	goodToBeUpdated.setName(name);
    	goodToBeUpdated.setDescription(description);
    	goodToBeUpdated.setTags(tags);
    	
    	/*
    	 * This is just for the examples. The userId will be the real id of
    	 * the user, who is offering the good.
    	 */
    	long userId = 1L;
    	goodToBeUpdated.setUserId(userId);
    	
    	/*
    	 * Calling save() on an object with predefined id will update the
    	 * corresponding database record rather than insert a new one.
    	 */
    	model.addAttribute("result", repository.save(goodToBeUpdated));
		return "updatedGood";
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

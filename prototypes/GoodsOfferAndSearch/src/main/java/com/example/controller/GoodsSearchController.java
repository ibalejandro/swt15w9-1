package com.example.controller;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.example.repository.GoodRepository;

@Controller
public class GoodsSearchController {
	
	@Autowired GoodRepository repository;
	
	@RequestMapping(value = "/searchResultsByName", method = RequestMethod.POST)
    public String searchGoodByName(HttpServletRequest request, Model model) {
    	
		String name = request.getParameter("name");
		
		/*
		 * The parameter for the search is being sent to the view, so that the
		 * user can see which name he typed.
		 */
		model.addAttribute("resultParameter", name);
		model.addAttribute("result", repository
						   .findByNameStartingWithIgnoreCase(name));
		return "searchResults";
    }
	
	@RequestMapping(value = "/searchResultsByTag", method = RequestMethod.POST)
    public String searchGoodByTag(HttpServletRequest request, Model model) {
    	
		String tag = request.getParameter("tag");
		
		/*
		 * The parameter for the search is being sent to the view, so that the
		 * user can see which tag he typed.
		 */
		model.addAttribute("resultParameter", tag);
		model.addAttribute("result", repository
						   .findByAttributeAndValue(tag));
		return "searchResults";
    }

}

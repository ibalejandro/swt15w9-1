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
		String parameterType = "name";
		String name = request.getParameter("name");
		
		/*
		 * The type of parameter and the parameter itself for the search are 
		 * sent to the view, so that the user can see his search criteria.
		 */
		model.addAttribute("resultParameterType", parameterType);
		model.addAttribute("resultParameter", name);
		model.addAttribute("result", repository
						   .findByNameStartingWithIgnoreCase(name));
		return "searchResults";
    }
	
	@RequestMapping(value = "/searchResultsByTag", method = RequestMethod.POST)
    public String searchGoodByTag(HttpServletRequest request, Model model) {
		String parameterType = "tag";
		String tag = request.getParameter("tag");
		
		/*
		 * The type of parameter and the parameter itself for the search are 
		 * sent to the view, so that the user can see his search criteria.
		 */
		model.addAttribute("resultParameterType", parameterType);
		model.addAttribute("resultParameter", tag);
		model.addAttribute("result",
						   repository.findByTagsContainingIgnoreCase(tag));
		return "searchResults";
    }

}

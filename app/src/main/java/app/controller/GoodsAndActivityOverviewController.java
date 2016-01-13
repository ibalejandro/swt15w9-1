package app.controller;

import org.salespointframework.useraccount.UserAccountManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import app.model.ActivityEntity;
import app.model.GoodEntity;
import app.model.UserRepository;
import app.repository.ActivitiesRepository;
import app.repository.GoodsRepository;
import app.repository.LanguageRepository;

/**
 * <h1>GoodsAndActivityOverviewConroller</h1> The
 * GoodsAndActivityOverviewConroller is used for viewing the goods and
 * activities.
 * 
 *
 * @author Kilian Heret
 * 
 */

@Controller
public class GoodsAndActivityOverviewController {
	private final UserAccountManager userAccountManager;
	private final UserRepository userRepository;
	private final LanguageRepository languageRepository;
	private final GoodsRepository goodsRepository;
	private final ActivitiesRepository activitiesRepository;

	/**
	 * Autowire.
	 * 
	 * @param GoodsAndActivityOverviewController
	 * @return
	 */
	@Autowired
	public GoodsAndActivityOverviewController(UserAccountManager userAccountManager, UserRepository userRepository,
			LanguageRepository languageRepository, GoodsRepository goodsRepository,
			ActivitiesRepository activitiesRepository) {
		Assert.notNull(userAccountManager, "UserAccountManager must not be null!");
		Assert.notNull(userRepository, "UserRepository must not be null!");

		this.userAccountManager = userAccountManager;
		this.userRepository = userRepository;
		this.languageRepository = languageRepository;
		this.goodsRepository = goodsRepository;
		this.activitiesRepository = activitiesRepository;
	}

	@RequestMapping(value = "/item/good1", method = RequestMethod.GET)
	public String showGood0() {
		return "redirect:/item/good/1";
	}

	@RequestMapping(value = "/item/activity1", method = RequestMethod.GET)
	public String ActivityGood0() {
		return "redirect:/item/activity/1";
	}

	@RequestMapping(value = "/item/{typ}/{id}", method = RequestMethod.GET)
	public String showGoodAndActivity(@PathVariable String typ, @PathVariable String id, Model modelMap) {

		if (typ.equals("good")) {
			long idLong = Long.parseLong(id);
			GoodEntity goodEntity = goodsRepository.findOne(idLong);

			if (goodEntity == null) {
				return "redirect:/";
			}
			System.out.println(goodEntity.getItemTypeAndId() + ": " + goodEntity.getName());
			
			modelMap.addAttribute("result", goodEntity);
			modelMap.addAttribute("good", true);
			return "itemNContainer--large";
		}
		if (typ.equals("activity")) {
			long idLong = Long.parseLong(id);
			ActivityEntity activitiesEntity = activitiesRepository.findOne(idLong);

			if (activitiesEntity == null) {
				return "redirect:/";
			}
			System.out.println(activitiesEntity.getItemTypeAndId() + ": " + activitiesEntity.getName());
			
			modelMap.addAttribute("result", activitiesEntity);
			modelMap.addAttribute("good", false);
			return "itemNContainer--large";
		}
		return "redirect:/";

	}

}

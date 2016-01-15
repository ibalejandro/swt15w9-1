package app.controller;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import app.model.TagEntity;
import app.repository.TagsRepository;

/**
 * <h1>TagsFillerController</h1> The TagsFillerController is used to save the
 * whole available tags in the database.
 *
 * @author Alejandro Sánchez Aristizábal
 * @since 10.12.2015
 */
@Controller
public class TagsFillerController {

	private final TagsRepository tagsRepository;

	private ArrayList<TagEntity> tags;

	/**
	 * Autowire.
	 * 
	 * @param TagsRepository
	 *            The repository for the tags
	 */
	@Autowired
	public TagsFillerController(TagsRepository tagsRepository) {
		this.tagsRepository = tagsRepository;
		tags = new ArrayList<>();
	}

	/**
	 * This method creates and saves the whole tags in the database, so that
	 * they can be accessible and the user can select one of them for his
	 * offers.
	 * 
	 * @return Nothing
	 */
	@RequestMapping(value = "/initTags", method = RequestMethod.GET)
	public void initTags() {
		TagEntity tag1 = new TagEntity("Baby");
		TagEntity tag2 = new TagEntity("Books");
		TagEntity tag3 = new TagEntity("Cameras & Photo");
		TagEntity tag4 = new TagEntity("Cell Phones & Accesories");
		TagEntity tag5 = new TagEntity("Clothes, Shoes and Accesories");
		TagEntity tag6 = new TagEntity("Computers/Tablets & Networking");
		TagEntity tag7 = new TagEntity("Consumer Electronics");
		TagEntity tag8 = new TagEntity("Dolls & Bears");
		TagEntity tag9 = new TagEntity("DVDs & Movies");
		TagEntity tag10 = new TagEntity("Gift Cards & Coupons");
		TagEntity tag11 = new TagEntity("Health & Beauty");
		TagEntity tag12 = new TagEntity("Jewelry & Watches");
		TagEntity tag13 = new TagEntity("Music");
		TagEntity tag14 = new TagEntity("Musical Instruments and Gear");
		TagEntity tag15 = new TagEntity("Pottery & Glass");
		TagEntity tag16 = new TagEntity("Sporting Goods");
		TagEntity tag17 = new TagEntity("Sports Mem, Cards & Fan Shop");
		TagEntity tag18 = new TagEntity("Tickets & Experiences");
		TagEntity tag19 = new TagEntity("Toys & Hobbies");
		TagEntity tag20 = new TagEntity("Video Games & Consoles");
		TagEntity tag21 = new TagEntity("Others");

		tags.add(tag1);
		tags.add(tag2);
		tags.add(tag3);
		tags.add(tag4);
		tags.add(tag5);
		tags.add(tag6);
		tags.add(tag7);
		tags.add(tag8);
		tags.add(tag9);
		tags.add(tag10);
		tags.add(tag11);
		tags.add(tag12);
		tags.add(tag13);
		tags.add(tag14);
		tags.add(tag15);
		tags.add(tag16);
		tags.add(tag17);
		tags.add(tag18);
		tags.add(tag19);
		tags.add(tag20);
		tags.add(tag21);

		for (TagEntity tag : tags)
			tagsRepository.save(tag);
	}

}

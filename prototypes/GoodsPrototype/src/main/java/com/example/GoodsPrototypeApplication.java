package com.example;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootApplication
public class GoodsPrototypeApplication {
	
	private static final Logger log = LoggerFactory.getLogger
									  (GoodsPrototypeApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(GoodsPrototypeApplication.class, args);
    }
    
    /* This is used to generate content manually.
    @Bean
	public CommandLineRunner demo(GoodRepository repository) {
		return (args) -> {
			
			// create a couple of users
			Address address1 = new Address("Street 1", 01, 01, "Dresden");
			Address address2 = new Address("Street 2", 02, 02, "Berlin");
			Address address3 = new Address("Street 3", 03, 03, "Hamburg");
			Address address4 = new Address("Street 4", 04, 04, "Mannheim");
			Address address5 = new Address("Street 5", 05, 05, "Hannover");
			Address address6 = new Address("Street 6", 06, 06, "Köln");
			
			// create a couple of users
			User user1 = new User("Mario", "Henze", "mario.henze@tu-dresden.de",
								  "Germany", address1);
			User user2 = new User("Kilian", "Heret", "kilian.heret@tu-dresden"
								  + ".de", "Germany", address2);
			User user3 = new User("Friederike", "Kitzing", "friederike.kitzing"
								  + "@tu-dresden.de", "Germany", address3);
			User user4 = new User("Ferdinand", "Lehmann", "ferdinand.lehmann"
								  + "@tu-dresden.de","Germany", address4);
			User user5 = new User("Lennart", "Schmidt", "lennart.schmidt" + 
								  "@tu-dresden.de", "Germany", address5);
			User user6 = new User("Alejandro", "Sánchez", "alejandro.sanchez" + 
					  			  "@tu-dresden.de", "Germany", address6);
			
			// save a couple of goods
			repository.save(new Good("Bicycle", "A very beautiful one",
									 "bicycle", user1));
			repository.save(new Good("Pullover", "Good for the autumn",
					 			     "cold", user2));
			repository.save(new Good("T-Shirt", "Super Heroes rule",
					 			     "kids", user3));
			repository.save(new Good("Notebook", "Ideal to learn german",
					 			     "write", user4));
			repository.save(new Good("Boots", "Winter is coming", "winter",
									 user5));
			repository.save(new Good("Jacket", "Nobody wants to be frozen",
									 "cold", user6));

			// fetch all goods
			log.info("");
			log.info("Goods found with findAll():");
			log.info("-------------------------------");
			for (Good good : repository.findAll()) {
				log.info(good.toString());
			}
			log.info("-------------------------------");
            log.info("");

			// fetch an individual good by ID
			Good good = repository.findOne(1L);
			log.info("Good found with findOne(1L):");
			log.info("-------------------------------");
			log.info(good.toString());
			log.info("-------------------------------");
            log.info("");

			// fetch goods by name
			log.info("Goods found with findByName('Bicycle'):");
			log.info("-------------------------------");
			for (Good nameGood : repository.findByName("Bicycle")) {
				log.info(nameGood.toString());
			}
			log.info("-------------------------------");
            log.info("");
            
            // fetch goods by tag
 			log.info("Goods found with findByTag('winter'):");
 			log.info("-------------------------------");
 			for (Good tagGood : repository.findByTag("winter")) {
 				log.info(tagGood.toString());
 			}
 			log.info("-------------------------------");
 			log.info("");
 			
 		// order goods by tag asc
 			log.info("Goods order with findAllByOrderByTagAsc():");
 			log.info("-------------------------------");
 			for (Good orderAscGood : repository.findAllByOrderByTagAsc()) {
 				log.info(orderAscGood.toString());
 			}
 			log.info("-------------------------------");
 			log.info("");
		};
	}
	*/
}

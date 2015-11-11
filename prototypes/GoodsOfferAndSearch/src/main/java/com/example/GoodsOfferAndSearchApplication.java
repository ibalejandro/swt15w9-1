package com.example;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import com.example.entity.GoodEntity;
import com.example.model.Address;
import com.example.model.Good;
import com.example.model.User;
import com.example.repository.GoodRepository;


@SpringBootApplication
public class GoodsOfferAndSearchApplication {
	
	private static final Logger log = LoggerFactory.getLogger
									  (GoodsOfferAndSearchApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(GoodsOfferAndSearchApplication.class, args);
    }
    
    /*
     * Basic functionality without views or controllers.
    @Bean
	public CommandLineRunner demo(GoodRepository repository) {
		return (args) -> {
			
			// Create a couple of addresses.
			Address address1 = new Address("Street 1", 01, 01, "Dresden");
			Address address2 = new Address("Street 2", 02, 02, "Berlin");
			Address address3 = new Address("Street 3", 03, 03, "Hamburg");
			Address address4 = new Address("Street 4", 04, 04, "Mannheim");
			Address address5 = new Address("Street 5", 05, 05, "Hannover");
			Address address6 = new Address("Street 6", 06, 06, "Köln");
			
			// Create a couple of users.
			User user1 = new User("Mario", "Henze", "mario.henze@tu-dresden.de",
								  "Germany", address1);
			User user2 = new User("Kilian", "Heret", "kilian.heret@tu-dresden"
								  + ".de", "Germany", address2);
			User user3 = new User("Friederike", "Kitzing", "friederike.kitzing"
								  + "@tu-dresden.de", "Germany", address3);
			User user4 = new User("Ferdinand", "Lehmann", "ferdinand.lehmann"
								  + "@tu-dresden.de","Germany", address4);
			User user5 = new User("Lennart", "Schmidt", "lennart.schmidt"
								  + "@tu-dresden.de", "Germany", address5);
			User user6 = new User("Alejandro", "Sánchez", "alejandro.sanchez"
								  + "@tu-dresden.de", "Germany", address6);
			
			// Create a couple of tag sets.
			Set<String> tags1 = new HashSet<String>(Arrays.asList("a", "b"));
			Set<String> tags2 = new HashSet<String>(Arrays.asList("a", "b"));
			Set<String> tags3 = new HashSet<String>(Arrays.asList("a", "b"));
			Set<String> tags4 = new HashSet<String>(Arrays.asList("a", "b"));
			Set<String> tags5 = new HashSet<String>(Arrays.asList("a", "b"));
			Set<String> tags6 = new HashSet<String>(Arrays.asList("a", "b"));
			
			// Create a couple of goods.
			Good good1 = new Good("Bicycle", "A very beautiful one", tags1);
			Good good2 = new Good("Pullover", "Good for the autumn", tags2);
			Good good3 = new Good("T-Shirt", "Super Heroes rule", tags3);
			Good good4 = new Good("Notebook", "Ideal to learn german", tags4);
			Good good5 = new Good("Boots", "Winter is coming", tags5);
			Good good6 = new Good("Jacket", "Nobody wants to be frozen", tags6);
			
			// Save a the goods.
			repository.save(good1.createGoodEntity(1L));
			repository.save(good2.createGoodEntity(2L));
			repository.save(good3.createGoodEntity(3L));
			repository.save(good4.createGoodEntity(4L));
			repository.save(good5.createGoodEntity(5L));
			repository.save(good6.createGoodEntity(6L));

			// Fetch all goods.
			log.info("");
			log.info("Goods found with findAll():");
			log.info("-------------------------------");
			for (GoodEntity good : repository.findAll()) {
				log.info(good.toString());
			}
			log.info("-------------------------------");
            log.info("");

			// Fetch founded goods by a name.
			log.info("Goods found with findByName('biC'):");
			log.info("-------------------------------");
			for (GoodEntity good :
				 repository.findByNameStartingWithIgnoreCase("biC")) {
				log.info(good.toString());
			}
			log.info("-------------------------------");
            log.info("");
		};
	}
	*/
    
}

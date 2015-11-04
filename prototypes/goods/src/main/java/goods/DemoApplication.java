package goods;

import javax.annotation.PostConstruct;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {

	public static String[] languages[];
	
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
    
    @PostConstruct
    void startup(){
    	TextBlockDB DB = new TextBlockDB();
    	String[] languages={"Deutsch","English"};
    	Goods Fahrrad = new Goods();
    	DB.save(new TextBlock({"Gelb","Yellow"});
    }
}

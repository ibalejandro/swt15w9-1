package goods;

import java.util.NoSuchElementException;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {

	public static String languages[];
	
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
    
    @Autowired TextBlockDB DB;
    
    @PostConstruct
    void startup(){
    	languages=new String[]{"Deutsch","English"};
    	Goods Fahrrad = new Goods();
    	TexBlock Yellow = new TexBlock(new String[]{"Gelb","Yellow"},new TexBlock[]{});
    	DB.save(Yellow);
    	TexBlock Colour = new TexBlock(new String[]{"Farbe","Colour"},new TexBlock[]{Yellow});
    	DB.save(Colour);
    		
    	for(TexBlock tex:DB.findAll()){
    		System.out.println(tex.getTranslation(0));
    		System.out.println(tex.getTranslation(1));
    	}
    		
    }
}

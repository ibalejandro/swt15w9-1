package goods;

import java.util.NoSuchElementException;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {

	public static String[] languages[];
	
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
    
    @Autowired TextBlockDB DB;
    
    @PostConstruct
    void startup(){
    	String[] languages={"Deutsch","English"};
    	Goods Fahrrad = new Goods();
    	TexBlock Yellow = new TexBlock(new String[]{"Gelb","Yellow"} );
    	DB.save(Yellow);
    //	DB.save(new TextBlock(new String[]{"Farbe","Colour"}));
    //	
    //	try{System.out.println(DB.findOne(0L).get().getID());
    //	
    //	} catch (NoSuchElementException e){
    //		System.err.println("NoSuchElementException: "+e.getMessage());
    //	}
    		
    }
}

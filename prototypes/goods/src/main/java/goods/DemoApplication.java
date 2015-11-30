package goods;

import java.util.Vector;
import java.util.Arrays;

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
    	languages=new String[]{"Deutsch","English","Test"};
    	//Goods Fahrrad = new Goods();
    	//Vector<String> YellowV = new Vector<String>();
    	//YellowV.add("Gelb");
    	//YellowV.add("Yellow");
    	//TexBlock Yellow = new TexBlock(YellowV);
    	//DB.save(Yellow);
    	//Vector<String> ColourV = new Vector<String>();
    	//ColourV.add("Farbe");
    	//ColourV.add("Colour");
    	//Vector<TexBlock> ColourV2 = new Vector<TexBlock>();
    	//ColourV2.add(Yellow);
    	//TexBlock Colour = new TexBlock(ColourV,ColourV2);
    	//DB.save(Colour);
    	//DB.findOne(1L).addnewTranslation("Gelb2");
    	//DB.findOne(2L).addnewTranslation("Yellow2");
    	//DB.flush();
    	//for(TexBlock tex:DB.findAll()){
    	//	for(int i=0; i<tex.getLength();i++){
    	//		System.out.println(tex.getTranslation(i));
    	//	}
    	//}
    	
    		
    }
}

package goods;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.springframework.util.Assert;

@Entity
public class TextBlock {
	private @Id @GeneratedValue Long ID;
	TextBlock[] values;
	String[] translations;
	
	public TextBlock(String[] translations, TextBlock[] values){
		Assert.notEmpty(translations, "Keine Übersetzungen angegeben!");
		assert translations.length==DemoApplication.languages.length : "Nicht genügend Übersetzungen angegeben!";
		this.translations=translations;
		this.values=values;
	}
	
	public TextBlock(String[] translations){
		Assert.notEmpty(translations, "Keine Übersetzungen angegeben!");
		assert translations.length==DemoApplication.languages.length : "Nicht genügend Übersetzungen angegeben!";
		this.translations=translations;
		this.values=null;
	}
	
	String  getTranslation(int languageid){
		return "Die Übersetzung von TextBlock #"+ID+" nach "+DemoApplication.languages[languageid]+" wurde angefordert. Sie lautet: "+translations[languageid];
	}
	
	void setTranslation(String newTranslation, int languageid){
		Assert.notNull(newTranslation, "Sie müssen eine neue Übersetzung eingeben!");
		assert languageid<=DemoApplication.languages.length : "Es konnte keine Sprache für diesen Sprachindex gefunden werden.";
		this.translations[languageid]=newTranslation;
	}
	
	Long getID(){
		return ID;
	}
	
	TextBlock[] getValues(){
		return values;
	}
	
	void setValues(TextBlock[] newvalues){
		this.values=newvalues;
	}
}

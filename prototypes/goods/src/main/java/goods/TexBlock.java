package goods;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.springframework.util.Assert;

@Entity
public class TexBlock {
	private @Id @GeneratedValue Long ID;
	TexBlock[] values;
	String[] translations;
	
	public TexBlock(String[] translations, TexBlock[] values){
		Assert.notEmpty(translations, "Keine Übersetzungen angegeben!");
		assert translations.length==DemoApplication.languages.length : "Nicht genügend Übersetzungen angegeben!";
		this.translations=translations;
		this.values=values;
	}
	
	public TexBlock(String[] translations){
		Assert.notEmpty(translations, "Keine Übersetzungen angegeben!");
		assert translations.length==DemoApplication.languages.length : "Nicht genügend Übersetzungen angegeben!";
		this.translations=translations;
		this.values=null;
	}
	
	public String  getTranslation(int languageid){
		return "Die Übersetzung von TextBlock #"+ID+" nach "+DemoApplication.languages[languageid]+" wurde angefordert. Sie lautet: "+translations[languageid];
	}
	
	public void setTranslation(String newTranslation, int languageid){
		Assert.notNull(newTranslation, "Sie müssen eine neue Übersetzung eingeben!");
		assert languageid<=DemoApplication.languages.length : "Es konnte keine Sprache für diesen Sprachindex gefunden werden.";
		this.translations[languageid]=newTranslation;
	}
	
	public Long getID(){
		return ID;
	}
	
	public TexBlock[] getValues(){
		return values;
	}
	
	public void setValues(TexBlock[] newvalues){
		this.values=newvalues;
	}
}

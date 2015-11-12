package goods;

import java.util.Optional;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.springframework.util.Assert;

@Entity
public class TexBlock implements java.io.Serializable {
	
	private @Id @GeneratedValue Long ID;
	private TexBlock[] blockvalues;
	private String[] blocktranslations;
	
	public TexBlock(String[] translations, TexBlock[] blockvalues){
		assert blockvalues==new TexBlock[]{} : "Induktionsalarm!";
		Assert.notEmpty(translations, "Keine Übersetzungen angegeben!");
		assert translations.length==DemoApplication.languages.length : "Nicht genügend Übersetzungen angegeben!";
		this.blocktranslations=translations;
		this.blockvalues=blockvalues;
	}
	
	public TexBlock(String[] translations){
		Assert.notEmpty(translations, "Keine Übersetzungen angegeben!");
		assert translations.length==DemoApplication.languages.length : "Nicht genügend Übersetzungen angegeben!";
		this.blocktranslations=translations;
		this.blockvalues=new TexBlock[]{null};
	}
	
	private TexBlock(){
		this.blocktranslations=null;
		this.blockvalues=null;
	}
	
	public String  getTranslation(int languageid){
		return "Die Übersetzung von TextBlock #"+ID+" nach "+DemoApplication.languages[languageid]+" wurde angefordert. Sie lautet: "+blocktranslations[languageid];
	}
	
	public void setTranslation(String translations, int languageid){
		Assert.notNull(translations, "Sie müssen eine neue Übersetzung eingeben!");
		assert languageid<DemoApplication.languages.length : "Es konnte keine Sprache für diesen Sprachindex gefunden werden.";
		this.blocktranslations[languageid]=translations;
	}
	
	public Long getID(){
		return ID;
	}
	
	public TexBlock[] getValues(){
		return blockvalues;
	}
	
	public void setValues(TexBlock[] blockvalues){
		this.blockvalues=blockvalues;
	}
}

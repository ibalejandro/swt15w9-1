package goods;

import java.util.Vector;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.springframework.util.Assert;

@Entity
public class TexBlock implements java.io.Serializable {
	
	@Id @GeneratedValue
	private  Long ID;
	
	@Column(length=9001)
	private Vector<TexBlock> blockvalues;
	@Column
	private Vector<String> blocktranslations;
	
	//"blocktranslations" ist der Name des Blocks in den verschiedenen Sprachen, "blockvalues" beinhaltet die als Antwortmöglichkeiten zugeordneten Textblöcke. Z. b. "Farbe" wird "Gelb","Rot", etc. zugeordnet.
	
	public TexBlock(Vector<String> translations, Vector<TexBlock> blockvalues){
		Assert.notEmpty(translations, "Keine Übersetzungen angegeben!");
		assert translations.size()==DemoApplication.languages.length : "Nicht genügend Übersetzungen angegeben!";
		this.blocktranslations= new Vector<String>();
		this.blocktranslations=translations;
		this.blockvalues=new Vector<TexBlock>();
		this.blockvalues=blockvalues;
	}
	
	public TexBlock(Vector<String> translations){
		Assert.notEmpty(translations, "Keine Übersetzungen angegeben!");
		assert translations.size()==DemoApplication.languages.length : "Nicht genügend Übersetzungen angegeben!";
		this.blocktranslations= new Vector<String>();
		this.blocktranslations=translations;
		this.blockvalues=new Vector<TexBlock>();
		this.blockvalues=null;
	}
	
	private TexBlock(){
		this.blocktranslations= new Vector<String>();
		this.blocktranslations=null;
		this.blockvalues=new Vector<TexBlock>();
		this.blockvalues=null;
	}
	
	public String  getTranslation(int languageid){
		return "Die Übersetzung von TextBlock #"+ID+" nach "+DemoApplication.languages[languageid]+" wurde angefordert. Sie lautet: "+this.blocktranslations.get(languageid);
	}
	
	public void setTranslation(String translations, int languageid){
		Assert.notNull(translations, "Sie müssen eine neue Übersetzung eingeben!");
		assert languageid<DemoApplication.languages.length : "Es konnte keine Sprache für diesen Sprachindex gefunden werden.";
		this.blocktranslations.set(languageid,translations);
	}
	
	public int getLength(){
		return this.blocktranslations.size();
	}
	
	public Long getID(){
		return ID;
	}
	
	public Vector<TexBlock> getValues(){
		return blockvalues;
	}
	
	public void setValues(Vector<TexBlock> blockvalues){
		this.blockvalues=blockvalues;
	}
	
	public void addnewValue(TexBlock newValue){
		this.blockvalues.add(newValue);
	}
	
	public void addnewTranslation(String newTranslation){
		this.blocktranslations.add(newTranslation);
		System.out.println(this.blocktranslations.indexOf(newTranslation));
	}
}

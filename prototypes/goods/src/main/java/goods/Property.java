package goods;

import java.util.Arrays;

import org.springframework.util.Assert;

public class Property {
	/* Eine Property besteht aus einem TextBlock, dem ein Liste von TextBlöcken
	 * und/oder String zugeordnet wird. Beispiel: Größe: klein, oder Größe: 80cm
	 */
	TexBlock category;
	
	TexBlock value;
	
	String textvalue;
	
	
	public Property(TexBlock textblock, TexBlock textblock2){
		assert Arrays.asList(textblock.values).contains(textblock2) : "'"+textblock2.translations[0]+"' ist kein gültiger Wert für '"+textblock.translations[0]+"'";
		this.category=textblock;
		this.value=textblock;
		this.textvalue=null;
	}
	
	public Property(TexBlock textblock, String text){
		this.category=textblock;
		this.value=null;
		this.textvalue=text;
	}
	
	public TexBlock getCategory(){
		return category;
	}
	
	public String getTextValue(){
		return textvalue;
	}
	
	public TexBlock getValue(){
		return value;
	}
	
	public void setCategory(TexBlock category){
		this.category=category;
	}
	
	public void setTextValue(String text){
		Assert.notNull(this.textvalue, "Für diese Eigenschaft können nur vorgefertigte Werte verwendet werden!");
		this.textvalue=text;
	}
	
	public void setValue(TexBlock value){
		Assert.notNull(this.value, "Dieser Eigenschaft besitzt keine vorgefertigten Werte!");
		this.value=value;
	}
	
	
}

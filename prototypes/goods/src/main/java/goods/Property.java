package goods;

import java.util.Arrays;

import org.springframework.util.Assert;

public class Property {
	/* Eine Property besteht aus einem TextBlock, dem ein Liste von TextBlöcken
	 * und/oder String zugeordnet wird. Beispiel: Größe: klein, oder Größe: 80cm
	 */
	TextBlock category;
	
	TextBlock value;
	
	String textvalue;
	
	
	public Property(TextBlock textblock, TextBlock textblock2){
		assert Arrays.asList(textblock.values).contains(textblock2) : "'"+textblock2.translations[0]+"' ist kein gültiger Wert für '"+textblock.translations[0]+"'";
		this.category=textblock;
		this.value=textblock;
		this.textvalue=null;
	}
	
	public Property(TextBlock textblock, String text){
		this.category=textblock;
		this.value=null;
		this.textvalue=text;
	}
	
	TextBlock getCategory(){
		return category;
	}
	
	String getTextValue(){
		return textvalue;
	}
	
	TextBlock getValue(){
		return value;
	}
	
	void setCategory(TextBlock category){
		this.category=category;
	}
	
	void setTextValue(String text){
		Assert.notNull(this.textvalue, "Für diese Eigenschaft können nur vorgefertigte Werte verwendet werden!");
		this.textvalue=text;
	}
	
	void setValue(TextBlock value){
		Assert.notNull(this.value, "Dieser Eigenschaft besitzt keine vorgefertigten Werte!");
		this.value=value;
	}
	
	
}

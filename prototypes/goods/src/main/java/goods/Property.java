package goods;

import java.util.Arrays;

import org.springframework.util.Assert;

public class Property {
	/* Eine Property besteht aus einem TextBlock, dem ein Liste von TextBlöcken
	 * und/oder String zugeordnet wird. Beispiel: Größe: klein, oder Größe: 80cm
	 */
	TexBlock category;
	
	TexBlock blockvalue;
	
	String textvalue;
	
	
	public Property(TexBlock textblock, TexBlock textblock2){
		assert Arrays.asList(textblock.getValues()).contains(textblock2) : "'"+textblock2.getTranslation(0)+"' ist kein gültiger Wert für '"+textblock.getTranslation(0)+"'";
		this.category=textblock;
		this.blockvalue=textblock;
		this.textvalue=null;
	}
	
	public Property(TexBlock textblock, String text){
		this.category=textblock;
		this.blockvalue=null;
		this.textvalue=text;
	}
	
	public TexBlock getCategory(){
		return category;
	}
	
	public String getTextValue(){
		return textvalue;
	}
	
	public TexBlock getValue(){
		return blockvalue;
	}
	
	public void setCategory(TexBlock category){
		this.category=category;
	}
	
	public void setTextValue(String text){
		Assert.notNull(this.textvalue, "Für diese Eigenschaft können nur vorgefertigte Werte verwendet werden!");
		this.textvalue=text;
	}
	
	public void setValue(TexBlock blockvalue){
		Assert.notNull(this.blockvalue, "Dieser Eigenschaft besitzt keine vorgefertigten Werte!");
		this.blockvalue=blockvalue;
	}
	
	
}

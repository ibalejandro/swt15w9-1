package goods;

import java.util.ArrayList;

import org.springframework.util.Assert;

public class Goods {
	private String foto;
	private ArrayList <Property> properties;
	
	public Goods(String foto){
		
		Assert.hasText(foto, "Kein Foto angegeben!");
		
		this.foto=foto;
		properties=null;
	}
	
	public Goods(){
		this.foto=null;
		properties=null;
	}
	
	void addOffer(){
		//Nicht hier behandelt
	}
	
	void addProperty(Property p){
		Assert.notNull(p, "Keine Eigenschaft angegeben!");
		properties.add(p);
	}
	
	String getFoto(){
		return foto;
	}
	
	void setFoto(String foto){
		this.foto=foto;
	}
}

package nl.politie.predev.notes.api.model;

import java.util.ArrayList;
import java.util.List;

public class RbacRol {

	private String name;
	private List<Autorisatie> autorisaties;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<Autorisatie> getAutorisaties() {
		return autorisaties;
	}
	public void setAutorisaties(List<Autorisatie> autorisaties) {
		this.autorisaties = autorisaties;
	}
	public void addAutorisatie(Autorisatie autorisatie){
		
		if(this.autorisaties == null){
			this.autorisaties = new ArrayList<Autorisatie>();
		}
		this.autorisaties.add(autorisatie);
	}
	
}

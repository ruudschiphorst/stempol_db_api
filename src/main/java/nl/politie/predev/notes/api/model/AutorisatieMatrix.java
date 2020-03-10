package nl.politie.predev.notes.api.model;

import java.util.ArrayList;
import java.util.List;

public class AutorisatieMatrix {

	private List<RbacRol> rbacRollen;

	public List<RbacRol> getRbacRollen() {
		return rbacRollen;
	}

	public void setRbacRollen(List<RbacRol> rbacRollen) {
		this.rbacRollen = rbacRollen;
	}
	public void addRbacRol(RbacRol rbacRol){
		if(this.rbacRollen == null) {
			this.rbacRollen = new ArrayList<RbacRol>();
		}
		this.rbacRollen.add(rbacRol);
	}
	
}

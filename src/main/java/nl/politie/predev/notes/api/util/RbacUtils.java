package nl.politie.predev.notes.api.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.math.NumberUtils;

import nl.politie.predev.notes.api.model.Autorisatie;
import nl.politie.predev.notes.api.model.AutorisatieMatrix;
import nl.politie.predev.notes.api.model.RbacRol;

public class RbacUtils {

	private AutorisatieMatrix autorisatieMatrix;
	
	public RbacUtils() {
		this.autorisatieMatrix = getAutorisatieMatrixFromResources();
	}
	
	public boolean hasAccess(String rol, String systeemsoort, Double grondslag, Integer autorisatieniveau, Integer afhandelcode) {
		for(RbacRol rbacRol : this.autorisatieMatrix.getRbacRollen()) {
			if(rbacRol.getName().equalsIgnoreCase(rol)) {
				for(Autorisatie autorisatie: rbacRol.getAutorisaties()) {
					if(autorisatie.getSysteemsoort().equalsIgnoreCase(systeemsoort) &&
							autorisatie.getGrondslag() == grondslag && 
							autorisatie.getAutorisatieniveau() == autorisatieniveau && 
							autorisatie.getAfhandelcode() == afhandelcode) {
						return true;
					}
				}
			}
		}
		
		return false;
	}
	
	//Ik wil hier een REST service van maken
	@Deprecated
	private AutorisatieMatrix getAutorisatieMatrixFromResources() {

		AutorisatieMatrix retval = new AutorisatieMatrix();
		
		Map<String, RbacRol> rollen = new HashMap<String, RbacRol>();
		
		File file = new File(getClass().getClassLoader().getResource("autorisatiematrix").getFile());

		try (FileReader reader = new FileReader(file); BufferedReader br = new BufferedReader(reader)) {

			String line;
			while ((line = br.readLine()) != null) {
				String[] elements = line.split(";");
				RbacRol rbacRol = rollen.getOrDefault(elements[0], new RbacRol());
				rbacRol.setName(elements[0]);
				
				Autorisatie autorisatie = new Autorisatie();
				autorisatie.setSysteemsoort(elements[1]);
				autorisatie.setGrondslag(NumberUtils.createDouble(elements[2]));
				autorisatie.setAutorisatieniveau(NumberUtils.createInteger(getElement(elements, 3)));
				autorisatie.setAfhandelcode(NumberUtils.createInteger(getElement(elements,4)));
				
				rbacRol.addAutorisatie(autorisatie);
				rollen.put(elements[0], rbacRol);
				
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		for(Map.Entry<String, RbacRol> entry : rollen.entrySet()) {
			retval.addRbacRol(entry.getValue());
		}
		
		return retval;
	}

	private String getElement(String[] elements, int element) {
		if(elements.length >= (element +1)) {
			return elements[element];
		}
		return null;
	}
	
}

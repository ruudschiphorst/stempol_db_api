package nl.politie.predev.notes.api.test;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.imageio.ImageIO;

//import java.time.LocalDateTime;
//import java.util.UUID;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper;

import nl.politie.predev.notes.api.model.Autorisatie;
import nl.politie.predev.notes.api.model.AutorisatieMatrix;
import nl.politie.predev.notes.api.model.Note;
import nl.politie.predev.notes.api.model.RbacRol;
import nl.politie.predev.notes.api.util.RbacUtils;

//import nl.politie.predev.notes.api.model.Note;

//import nl.politie.predev.notes.api.repository.NotesRepository;

public class Tester {

//	@Test
//	public void sadas() throws IOException {
//		
//		String original = "/home/ruud/Downloads/eend.jpg";
//		String thumb ="/home/ruud/Downloads/thumb_eend.jpg";
//		
//		BufferedImage img = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
//		img.createGraphics().drawImage(ImageIO.read(new File(original)).getScaledInstance(100, 100, Image.SCALE_SMOOTH),0,0,null);
//		
//		ByteArrayOutputStream baos = new ByteArrayOutputStream();
//		ImageIO.write( img, "jpg", baos );
//		baos.flush();
//		byte[] imageInByte = baos.toByteArray();
//		baos.close();
//		System.out.println(imageInByte.length);
//		
//		Files.write(Paths.get(thumb), imageInByte);
//		
////		ImageIO.write(img, "jpg", new File(thumb));
//		
//	}
//	
//	@Test
//	public void henk() throws JsonProcessingException {
//	
//		
//		Note note = new Note();
//		note.setCreated_by("ruud");
//		note.setTitle("een titel");
//		note.setIs_deleted(false);
//		note.setIs_public(true);
//		note.setOwner("Ruud");
//		note.setVersion(1);
//		
//		ObjectMapper om = new ObjectMapper();
//		System.out.println( om.writeValueAsString(note));
//		
//	
//		
////		
////		LocalDateTime d = LocalDateTime.parse ("2020-02-11 14:10:59.322672");
////		System.out.println(d);
//		
//	}
	
//	@Test
//	public void blaat() throws IOException {
//		RbacUtils rbacUtils = new RbacUtils();
//		AutorisatieMatrix am = rbacUtils.getAutorisatieMatrixFromResources();
//		for(RbacRol rbacRol : am.getRbacRollen()) {
//			System.out.println(rbacRol.getName());
//			for(Autorisatie a : rbacRol.getAutorisaties()){
//				System.out.println(a.getSysteemsoort() + "..." + a.getGrondslag() + " _ " + a.getAutorisatieniveau() + " _ " + a.getAfhandelcode());
//			}
//		}
//	}
	
}

package nl.politie.predev.notes.api.util;

public class AppConstants {
	
	public static final String ERR_DUPLICATE_KEY = "Er bestaat al een record in de database met deze sleutel";
	public static final String ERR_AUTHORIZED_BUT_NOT_SHARED = "U probeert een notitie te openen die niet met u gedeeld is. Neem contact op met de eigenaar om toegang te krijgen tot deze notitie.";
	public static final String ERR_SHARED_BUT_NOT_AUTHORIZED = "U probeert een notitie te openen die met u gedeeld is, maar u beschikt niet over genoeg rechten om de inhoud daarvan te zien.";
	public static final String ERR_OWNER_NOT_AUTHORIZED = "U probeert een notitie te openen waar u de eigenaar van bent, maar u heeft niet de benodigde autorisaties om deze notitie te openen.";
	
}

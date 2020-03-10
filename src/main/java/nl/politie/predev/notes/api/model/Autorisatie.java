package nl.politie.predev.notes.api.model;

public class Autorisatie {

	private String systeemsoort; //Opsporing of handhaving denk ik
	private double grondslag;
	private Integer autorisatieniveau;
	private Integer afhandelcode;
	
	public double getGrondslag() {
		return grondslag;
	}
	public void setGrondslag(double grondslag) {
		this.grondslag = grondslag;
	}
	public Integer getAutorisatieniveau() {
		return autorisatieniveau;
	}
	public void setAutorisatieniveau(Integer autorisatieniveau) {
		this.autorisatieniveau = autorisatieniveau;
	}
	public Integer getAfhandelcode() {
		return afhandelcode;
	}
	public void setAfhandelcode(Integer afhandelcode) {
		this.afhandelcode = afhandelcode;
	}
	public String getSysteemsoort() {
		return systeemsoort;
	}
	public void setSysteemsoort(String systeemsoort) {
		this.systeemsoort = systeemsoort;
	}
	
}

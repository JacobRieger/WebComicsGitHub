package dataCode;

public class HtmlImageTag {

	private String imageUrl;
	private String altText;
	
	public HtmlImageTag(String image, String alt)
	{
		imageUrl = image;
		altText  = alt;
	}
	
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public String getAltText() {
		return altText;
	}
	public void setAltText(String altText) {
		this.altText = altText;
	}
	
	

}

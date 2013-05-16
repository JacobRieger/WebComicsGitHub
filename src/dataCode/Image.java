package dataCode;

public class Image {

	private String imageUrl;
	private String altText;
	
	public Image(String image, String alt)
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

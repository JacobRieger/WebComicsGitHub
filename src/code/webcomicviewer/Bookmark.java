package code.webcomicviewer;

public class Bookmark {

	private String Name;
	private String Url;
	
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public String getUrl() {
		return Url;
	}
	public void setUrl(String url) {
		Url = url;
	}
	
	public Bookmark(String name, String URL)
	{
		Name = name;
		Url = URL;
	}
}

package comicCode;

import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.ConnectException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import dataCode.Image;

/**
 * Created by Jacob on 8/10/13.
 */
public class ImageRetriever {

    private Comic comic;

    public ImageRetriever(Comic comicPassed)
    {
        comic = comicPassed;
    }

    public List<Image> RetrieveImgs() {
        // Connects to the url and retrieves all the img urls from the site
        // and returns them in strings
        Log.d("RetrieveImgStrings", "Called");
        try {
            List<Image> Images = new ArrayList<Image>();
            // Connecting to the site
            // Long timeout given, as was terminating too quickly
            // Not sure about consequences of that
            HttpConnection c = (HttpConnection) Jsoup.connect(comic.getUrl()).timeout(1000000);
            c.followRedirects(true);

            // Get the html
            Document page = c.get();
            Elements meta = page.select("html head meta");
            if (meta.attr("http-equiv").contains("REFRESH"))
            {
                page = Jsoup.connect(meta.attr("content").split("=")[1]).get();
                System.out.println(page.baseUri());
            }
            // System.out.println(page.html());
            // Now we have the html we need to select the elements we want
            Elements imgs = page.select("img");

            for (Element e : imgs) {

                String attr = e.attr("style");
                //System.out.println(e.attr("alt"))


                if(attr.indexOf("http://") > 0)
                {
                    String img = attr.substring( attr.indexOf("http://"), attr.indexOf(")"));
                    String alt = e.attr("title");
                    System.out.println(alt);

                    Images.add(new Image(img, alt));
                }

            }
            // We are going to iterate through these elements
            // Extracted string url's from the elements go into ImgStrings

            for (Element temp : imgs) {
                // Here we iterate through the elements and extract
                // the url from the src of the element
                //System.out.println(temp.toString());
                String extracted = temp.attr("src");
                String alt = temp.attr("title");
                System.out.println("Src = " + extracted + "alt = " + alt);

                if (extracted.startsWith("/") || !extracted.startsWith("http")) {
                    // This was necessary for DrMcNinja, as the full url
                    // was not posted, I suspect it's true for other sites
                    // as well
                    if (!extracted.startsWith("/") && !comic.getUrl().endsWith("/")) {

                        extracted = "/".concat(extracted);
                    }
                    extracted = comic.getUrl().concat(extracted);
                }
                if (!extracted.startsWith("http")) {
                    // Saves us from the malformed URL exception
                    extracted = "http://".concat(extracted);
                }
                extracted = extracted.replaceAll(" ", "%20");

                String New = extracted;
                //THIS IS UNRELIABLE
                //SERVERS LIE AND MESS IT UP
                //LOOKING AT YOU XKCD
                /*URL testurl = new URL(extracted);
				URLConnection conn = testurl.openConnection();
				int cLength = conn.getContentLength();
				Log.d("Length Checking", New + " " + cLength);
				if (cLength > 6000) {
					// Log.d("Length Checking", New + " " + cLength);
					ImgStrings.add(New);
				}
				else if(cLength == -1)
				{
					ImgStrings.add(New);
				}
				else
				{
					//System.out.println("Rejected " + New + " for " + cLength);
				}*/
                Images.add(new Image(New, alt));
            }

            // Return all the img tagged src strings in the html
            if (Images.size() == 0) {
                System.out.println(comic.getName() + " "
                        + "Zero images returned");
            }
            return Images;
        } catch (MalformedURLException e) {
            System.out.println("Malformed URL : " + comic.getUrl());
            // e.printStackTrace();
            List<Image> empty = new ArrayList<Image>();
            empty.add(new Image("Unset", "Unset"));
            return empty;
        } catch (ConnectException e) {
            System.out.println("Could not connect to : " + comic.getUrl());
            List<Image> empty = new ArrayList<Image>();
            empty.add(new Image("Unset","Unset"));
            return empty;
        } catch (IOException e) {
            System.out.println("General IO Exception Caught");
            e.printStackTrace();
            List<Image> empty = new ArrayList<Image>();
            empty.add(new Image("Unset", "Unset"));
            return empty;
        }
    }

}

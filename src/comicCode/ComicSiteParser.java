package comicCode;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import dataCode.HtmlImageTag;

/**
 * Created by Jacob on 8/10/13.
 */
public class ComicSiteParser {

    private Comic _comic;

    public ComicSiteParser(Comic comicPassed)
    {
        _comic = comicPassed;

    }

    public HtmlImageTag ParseForComicHtmlTag()
    {
        List<HtmlImageTag> htmlImageTags = GetComicHtmlImageTags();
        HtmlImageTag current;
        int height;
        int width;
        float AR;
        int area;
        float rating;
        String max = "unset";
        float maxRating = 0;

        for(int i = 0; i < htmlImageTags.size(); i++)
        {
            height = 0; width = 0; AR = 0; area = 0; rating = 0;
            current = htmlImageTags.get(i);
            //Log.d("ImageUrl", current);
            try {
                //Now we get the sizes
                URL oururl = new URL(current.getImageUrl());
                HttpURLConnection connection = (HttpURLConnection) oururl.openConnection();
                connection.setDoInput(true);
                connection.setFollowRedirects(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);

                if (myBitmap != null) {

                    //Find the width and height
                    width = myBitmap.getWidth();
                    height = myBitmap.getHeight();
                    //Print the dimmensions for testing purposes
                    //System.out.println(width + " " + height);
                    //System.out.println((float)imgHeight/(float)imgWidth);

                    AR = ((float) height / (float) width);
                    area = height * width;
                    if (AR > 1) {
                        rating = AR * area;
                    } else {
                        rating = area / AR;
                    }
                    rating = rating / 1000000;
                    if (area > 280000) {
                        rating = rating * (float) 1.5;
                    }
                    if (AR < .2 || AR > 3.25) {
                        Log.d("AR deduction" , current.getImageUrl());
                        rating = rating - (float)0.25;
                    }
                    if (max == "unset") {
                        max = current.getImageUrl();
                    }
                    Log.d("Area", Integer.toString(area));
                    Log.d("AR", Float.toString(AR));
                    Log.d("Rating", current + " " + rating);

                    if (rating > maxRating) {
                        Log.d("Rating", "Max set to " + current);
                        max = current.getImageUrl();
                        maxRating = rating;
                        _comic.setImageUrl(max);
                        _comic.setAltText(current.getAltText());
                    }

                }

            } catch (MalformedURLException e) {

                System.out.println("findImageUrl "+ current);
                e.printStackTrace();
            } catch (IOException e) {

                System.out.println("findImageUrl "+ current);
                e.printStackTrace();
            }
        }
    }

    public List<HtmlImageTag> GetComicHtmlImageTags() {
        // Connects to the url and retrieves all the img urls from the site
        // and returns them in strings
        try {
            List<HtmlImageTag> htmlImageTags = new ArrayList<HtmlImageTag>();
            // Connecting to the site
            // Long timeout given, as was terminating too quickly
            // Not sure about consequences of that
            HttpConnection c = (HttpConnection) Jsoup.connect(_comic.getUrl()).timeout(1000000);
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

                    htmlImageTags.add(new HtmlImageTag(img, alt));
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
                    if (!extracted.startsWith("/") && !_comic.getUrl().endsWith("/")) {

                        extracted = "/".concat(extracted);
                    }
                    extracted = _comic.getUrl().concat(extracted);
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
                htmlImageTags.add(new HtmlImageTag(New, alt));
            }

            // Return all the img tagged src strings in the html
            if (htmlImageTags.size() == 0) {
                System.out.println(_comic.getName() + " "
                        + "Zero images returned");
            }
            return htmlImageTags;
        } catch (MalformedURLException e) {
            System.out.println("Malformed URL : " + _comic.getUrl());
            // e.printStackTrace();
            List<HtmlImageTag> empty = new ArrayList<HtmlImageTag>();
            empty.add(new HtmlImageTag("Unset", "Unset"));
            return empty;
        } catch (ConnectException e) {
            System.out.println("Could not connect to : " + _comic.getUrl());
            List<HtmlImageTag> empty = new ArrayList<HtmlImageTag>();
            empty.add(new HtmlImageTag("Unset","Unset"));
            return empty;
        } catch (IOException e) {
            System.out.println("General IO Exception Caught");
            e.printStackTrace();
            List<HtmlImageTag> empty = new ArrayList<HtmlImageTag>();
            empty.add(new HtmlImageTag("Unset", "Unset"));
            return empty;
        }
    }

}

package asyncTasks;

import java.util.ArrayList;

import comicCode.Comic;

import dataCode.DataBaseHandler;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;

public class ComicsInitializer extends AsyncTask<Void, Void, Void> {

	
	private DataBaseHandler db;
	private Context context;
	ArrayList<Comic> dbl;
	
	public ComicsInitializer(Context passedContext)
	{
		context = passedContext;
	}
	
	
	@Override
	protected Void doInBackground(Void... params) {
		
		dbl = new ArrayList<Comic>();
		db  = new DataBaseHandler(context);
		
		//Hard Code all the comics
		
		/** Template for adding new additions to the list
		Comic = new Comic();
		.setName("");
		.setImageUrl("");
		.setUrl("");
		.setComicBitmap(Bitmap.createBitmap(10, 10, Bitmap.Config.ALPHA_8));
		**/
		
		Comic DrMcNinja = new Comic();
		DrMcNinja.setName("The Adventures of DrMcNinja");
		DrMcNinja.setImageUrl("http://drmcninja.com/comics/2013-06-05-26p7.jpg");
		DrMcNinja.setUrl("http://drmcninja.com/");
		DrMcNinja.setComicBitmap(Bitmap.createBitmap(10, 10, Bitmap.Config.ALPHA_8)); 	
		
		Comic Menage = new Comic();
		Menage.setName("Menage a 3");
		Menage.setImageUrl("http://zii.ma3comic.com/comics/mat20130606.png");
		Menage.setUrl("http://www.ma3comic.com/");
		Menage.setComicBitmap(Bitmap.createBitmap(10, 10, Bitmap.Config.ALPHA_8));
		
		Comic QC = new Comic();
		QC.setName("Questionable Content");
		QC.setImageUrl("http://www.questionablecontent.net/comics/2463.png");
		QC.setUrl("http://questionablecontent.net");
		QC.setComicBitmap(Bitmap.createBitmap(10, 10, Bitmap.Config.ALPHA_8));
		
		Comic xkcd= new Comic();
		xkcd.setName("XKCD");
		xkcd.setImageUrl("http://imgs.xkcd.com/comics/nomenclature.png");
		xkcd.setUrl("http://www.xkcd.com");
		xkcd.setComicBitmap(Bitmap.createBitmap(10, 10, Bitmap.Config.ALPHA_8));
		
		Comic Goblins = new Comic();
		Goblins.setName("Goblins");
		Goblins.setImageUrl("http://www.goblinscomic.org/comics/20130527.jpg");
		Goblins.setUrl("http://www.goblinscommic.org");
		Goblins.setComicBitmap(Bitmap.createBitmap(10, 10, Bitmap.Config.ALPHA_8));
		
		Comic Penny = new Comic();
		Penny.setName("Penny Arcade");
		Penny.setImageUrl("http://art.penny-arcade.com/photos/i-bXgsFrr/0/950x10000/i-bXgsFrr-950x10000.jpg");
		Penny.setUrl("http://www.penny-arcade.com/comic");
		Penny.setComicBitmap(Bitmap.createBitmap(10, 10, Bitmap.Config.ALPHA_8));
		
		Comic SamFuzzy = new Comic();
		SamFuzzy.setName("Sam and Fuzzy");
		SamFuzzy.setImageUrl("http://samandfuzzy.com/comics/00001740.gif");
		SamFuzzy.setUrl("http://www.samandfuzzy.com/");
		SamFuzzy.setComicBitmap(Bitmap.createBitmap(10, 10, Bitmap.Config.ALPHA_8));
		
		Comic Awkard = new Comic();
		Awkard.setName("Awkward Zombie");
		Awkard.setImageUrl("http://i49.photobucket.com/albums/f278/katietiedrich/comic303_zps2faf3d4b.png");
		Awkard.setUrl("http://www.awkwardzombie.com/index.php");
		Awkard.setComicBitmap(Bitmap.createBitmap(10, 10, Bitmap.Config.ALPHA_8));
		
		Comic explosm = new Comic();
		explosm.setName("Cyanide and Happiness");
		explosm.setImageUrl("http://www.explosm.net/db/files/Comics/Kris/tv2.png");
		explosm.setUrl("http://www.explosm.net/comics");
		explosm.setComicBitmap(Bitmap.createBitmap(10, 10, Bitmap.Config.ALPHA_8));
		
		Comic trenches = new Comic();
		trenches.setName("Trenches");
		trenches.setImageUrl("http://art.penny-arcade.com/photos/i-3CkdKM8/0/O/i-3CkdKM8.jpg");
		trenches.setUrl("http://trenchescomic.com/");
		trenches.setComicBitmap(Bitmap.createBitmap(10, 10, Bitmap.Config.ALPHA_8));
		
		Comic gws = new Comic();
		gws.setName("Girls with Slingshots");
		gws.setImageUrl("http://www.girlswithslingshots.com/comics/1370557145-GWS1636.jpg");
		gws.setUrl("http://www.girlswithslingshots.com/");
		gws.setComicBitmap(Bitmap.createBitmap(10, 10, Bitmap.Config.ALPHA_8));
		
		Comic fanboys = new Comic();
		fanboys.setName("Fanboys");
		fanboys.setImageUrl("http://www.fanboys-online.com/comics/1370236689-Unrest%20in%20the%20house%20of%20light.png");
		fanboys.setUrl("http://www.fanboys-online.com/");
		fanboys.setComicBitmap(Bitmap.createBitmap(10, 10, Bitmap.Config.ALPHA_8));
		
		Comic smbc = new Comic();
		smbc.setName("Saturday Morning Breakfast Cereal");
		smbc.setImageUrl("http://www.smbc-comics.com/comics/20130608.png");
		smbc.setUrl("http://www.smbc-comics.com/	");
		smbc.setComicBitmap(Bitmap.createBitmap(10, 10, Bitmap.Config.ALPHA_8));
		
		Comic nerfnow = new Comic();
		nerfnow.setName("Nerf Now!");
		nerfnow.setImageUrl("http://www.nerfnow.com/comic/image/1035");
		nerfnow.setUrl("http://www.nerfnow.com/");
		nerfnow.setComicBitmap(Bitmap.createBitmap(10, 10, Bitmap.Config.ALPHA_8));
		
		Comic oglaf = new Comic();
		oglaf.setName("Oglaf");
		oglaf.setImageUrl("http://media.oglaf.com/comic/glamazon_way_fix.jpg");
		oglaf.setUrl("http://oglaf.com/");
		oglaf.setComicBitmap(Bitmap.createBitmap(10, 10, Bitmap.Config.ALPHA_8));
		
		Comic arg = new Comic();
		arg.setName("ARG!");
		arg.setImageUrl("http://iamarg.com/comics/2013-06-05.jpg");
		arg.setUrl("http://iamarg.com/");
		arg.setComicBitmap(Bitmap.createBitmap(10, 10, Bitmap.Config.ALPHA_8));
		
		Comic loading = new Comic();
		loading.setName("Loading Artist");
		loading.setImageUrl("http://www.loadingartist.com/comics/2013-05-08-i-made-this.png");
		loading.setUrl("http://www.loadingartist.com/");
		loading.setComicBitmap(Bitmap.createBitmap(10, 10, Bitmap.Config.ALPHA_8));
		
		dbl.add(DrMcNinja);
		dbl.add(Menage);
		dbl.add(QC);
		dbl.add(xkcd);
		dbl.add(Goblins);
		dbl.add(Penny);
		dbl.add(SamFuzzy);
		dbl.add(Awkard);
		dbl.add(explosm);
		dbl.add(trenches);
		dbl.add(gws);
		dbl.add(fanboys);
		dbl.add(smbc);
		dbl.add(nerfnow);
		dbl.add(oglaf);
		dbl.add(arg);
		dbl.add(loading);
		
		for(int i = 0; i < dbl.size(); i++)
		{
			dbl.get(i).retrieveImageBitmap();
			dbl.get(i).setUpdated(true);
			db.addComic(dbl.get(i));
		}
		
		return null;
	}



}

package pl.psnc.dl.wf4ever.myexpimport;

import java.util.Locale;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.protocol.http.WebApplication;

import pl.psnc.dl.wf4ever.myexpimport.pages.ErrorPage;
import pl.psnc.dl.wf4ever.myexpimport.pages.HelpPage;
import pl.psnc.dl.wf4ever.myexpimport.pages.HomePage;
import pl.psnc.dl.wf4ever.myexpimport.pages.MyExpImportPage;


/**
 * 
 * @author Piotr Hołubowicz
 *
 */
public class ImportApplication
	extends WebApplication
{

	public ImportApplication()
	{
		super();
	}


	@Override
	public void init()
	{
		super.init();
		mountPage("/home", HomePage.class);
		mountPage("/import", MyExpImportPage.class);
		mountPage("/error", ErrorPage.class);
		mountPage("/help", HelpPage.class);
		
		Locale.setDefault(Locale.ENGLISH);
	}


	/**
	 * Return the "Home" page used by the application. Wicket will redirect
	 * here if you don't explicitly supply a Page destination.
	 */
	public Class< ? extends WebPage> getHomePage()
	{
		return HomePage.class;
	}

}

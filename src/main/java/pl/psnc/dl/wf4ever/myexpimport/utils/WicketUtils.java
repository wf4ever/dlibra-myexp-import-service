/**
 * 
 */
package pl.psnc.dl.wf4ever.myexpimport.utils;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.mapper.parameter.PageParameters;

/**
 * @author Piotr Hołubowicz
 *
 */
public class WicketUtils
{

	/**
	 * Generates the returnToUrl parameter that is passed to the OP. The
	 * User Agent (i.e., the browser) will be directed to this page following
	 * authentication.
	 * 
	 */
	public static String getCompleteUrl(WebPage page,
			Class< ? extends WebPage> pageClass)
	{
		PageParameters params = new PageParameters();
		return RequestCycle
				.get()
				.getUrlRenderer()
				.renderFullUrl(
					Url.parse(page.urlFor(pageClass, params).toString()));
	}

}

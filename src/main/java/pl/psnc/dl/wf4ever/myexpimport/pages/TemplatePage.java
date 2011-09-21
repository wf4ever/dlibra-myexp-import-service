package pl.psnc.dl.wf4ever.myexpimport.pages;

import java.io.StringReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.log4j.Logger;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.request.http.handler.RedirectRequestHandler;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;

import pl.psnc.dl.wf4ever.myexpimport.model.myexp.User;
import pl.psnc.dl.wf4ever.myexpimport.services.MyExpApi;
import pl.psnc.dl.wf4ever.myexpimport.services.OAuthException;
import pl.psnc.dl.wf4ever.myexpimport.services.OAuthHelpService;
import pl.psnc.dl.wf4ever.myexpimport.utils.Constants;

/**
 * 
 * @author Piotr Hołubowicz
 *
 */
public abstract class TemplatePage
	extends WebPage
{

	private static final Logger log = Logger.getLogger(TemplatePage.class);

	private static final long serialVersionUID = 4677896071331937974L;

	protected WebMarkupContainer content;

	protected boolean willBeRedirected = false;

	private static final Class< ? >[] publicPages = { HomePage.class,
			AboutPage.class, HelpPage.class};


	public TemplatePage(PageParameters pageParameters)
	{
		content = new WebMarkupContainer("content");
		if ((getMyExpAccessToken() == null || getDlibraAccessToken() == null)
				&& !ArrayUtils.contains(publicPages, this.getClass())) {
			content.setVisible(false);
			willBeRedirected = true;
			goToPage(HomePage.class, pageParameters);
		}

		add(new BookmarkablePageLink<Void>("home", getApplication()
				.getHomePage()));
		add(new BookmarkablePageLink<Void>("about", AboutPage.class));
		add(new BookmarkablePageLink<Void>("help", HelpPage.class));

		add(content);
		content.add(new FeedbackPanel("feedback"));
	}


	public Token getDlibraAccessToken()
	{
		return (Token) getSession().getAttribute(
			Constants.SESSION_DLIBRA_ACCESS_TOKEN);
	}


	public Token getMyExpAccessToken()
	{
		return (Token) getSession().getAttribute(
			Constants.SESSION_MYEXP_ACCESS_TOKEN);
	}


	public void setDlibraAccessToken(Token token)
	{
		getSession().setAttribute(Constants.SESSION_DLIBRA_ACCESS_TOKEN, token);
	}


	public void setMyExpAccessToken(Token token)
	{
		getSession().setAttribute(Constants.SESSION_MYEXP_ACCESS_TOKEN, token);
	}


	protected void goToPage(Class< ? extends TemplatePage> pageClass,
			PageParameters pageParameters)
	{
		String url = urlFor(pageClass, pageParameters).toString();
		log.debug("Will redirect to: " + url);
		getRequestCycle().scheduleRequestHandlerAfterCurrent(
			new RedirectRequestHandler(url));
	}


	/**
	 * @param user
	 * @param service
	 * @return
	 * @throws OAuthException
	 * @throws JAXBException
	 */
	protected User retrieveMyExpUser(Token accessToken, OAuthService service)
		throws OAuthException, JAXBException
	{
		User myExpUser;
		Response response = OAuthHelpService.sendRequest(service, Verb.GET,
			MyExpApi.WHOAMI_URL, accessToken);
		myExpUser = createMyExpUserModel(response.getBody());

		response = OAuthHelpService.sendRequest(service, Verb.GET,
			String.format(MyExpApi.GET_USER_URL, myExpUser.getId()),
			accessToken);
		myExpUser = createMyExpUserModel(response.getBody());
		return myExpUser;
	}


	private User createMyExpUserModel(String xml)
		throws JAXBException
	{
		JAXBContext jc = JAXBContext.newInstance(User.class);

		Unmarshaller u = jc.createUnmarshaller();
		StringBuffer xmlStr = new StringBuffer(xml);
		return (User) u.unmarshal(new StreamSource(new StringReader(xmlStr
				.toString())));
	}

}

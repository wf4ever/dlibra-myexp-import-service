package pl.psnc.dl.wf4ever.myexpimport.pages;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.Logger;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.request.http.handler.RedirectRequestHandler;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.scribe.model.Token;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

import pl.psnc.dl.wf4ever.myexpimport.services.DlibraApi;
import pl.psnc.dl.wf4ever.myexpimport.services.MyExpApi;
import pl.psnc.dl.wf4ever.myexpimport.utils.Constants;
import pl.psnc.dl.wf4ever.myexpimport.utils.WicketUtils;

/**
 * 
 * @author Piotr Hołubowicz
 *
 */
public class HomePage
	extends TemplatePage
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final Logger log = Logger.getLogger(HomePage.class);


	/**
	 * Default Constructor
	 */
	public HomePage()
	{
		this(new PageParameters());
	}


	@SuppressWarnings("serial")
	public HomePage(PageParameters pageParameters)
	{
		super(pageParameters);

		processOAuth(pageParameters);

		Form< ? > form = new Form<Void>("form");
		content.add(form);

		final Button authMyExpButton = new Button("authMyExp") {

			@Override
			public void onSubmit()
			{
				Token at = tryLoadMyExpTestToken();
				if (at == null) {
					startMyExpAuthorization();
				}
				else {
					setMyExpAccessToken(at);
					getSession().debug("Loaded test token for myExperiment");
					setResponsePage(HomePage.class);
					goToPage(HomePage.class, null);
				}
			}
		};
		authMyExpButton.setEnabled(getMyExpAccessToken() == null);
		form.add(authMyExpButton).setOutputMarkupId(true);

		final Button authDlibraButton = new Button("authDlibra") {

			@Override
			public void onSubmit()
			{
				Token at = tryLoadDlibraTestToken();
				if (at == null) {
					startDlibraAuthorization();
				}
				else {
					setDlibraAccessToken(at);
					getSession().debug("Loaded test token for dLibra");
					setResponsePage(HomePage.class);
					goToPage(HomePage.class, null);
				}
			}
		};
		authDlibraButton.setEnabled(getMyExpAccessToken() != null
				&& getDlibraAccessToken() == null);
		form.add(authDlibraButton).setOutputMarkupId(true);

		final Button importButton = new Button("myExpImportButton") {

			@Override
			public void onSubmit()
			{
				goToPage(MyExpImportPage.class, null);
			}
		};
		importButton.setEnabled(getMyExpAccessToken() != null
				&& getDlibraAccessToken() != null);
		form.add(importButton).setOutputMarkupId(true);
	}


	private void processOAuth(PageParameters pageParameters)
	{
		if (getMyExpAccessToken() == null) {
			OAuthService service = MyExpApi.getOAuthService(WicketUtils
					.getCompleteUrl(this, HomePage.class));
			Token token = retrieveMyExpAccessToken(pageParameters, service);
			setMyExpAccessToken(token);
			if (token != null) {
				info("Successfully received myExperiment access token");
			}
		}
		else if (getDlibraAccessToken() == null) {
			OAuthService service = DlibraApi.getOAuthService(WicketUtils
					.getCompleteUrl(this, HomePage.class));
			Token token = retrieveDlibraAccessToken(pageParameters, service);
			setDlibraAccessToken(token);
			if (token != null) {
				info("Successfully received dLibra access token");
			}
		}
	}


	private Token tryLoadDlibraTestToken()
	{
		Properties props = new Properties();
		try {
			props.load(getClass().getClassLoader().getResourceAsStream(
				"testToken.properties"));
			String token = props.getProperty("dLibraToken");
			if (token != null) {
				return new Token(token, null);
			}
		}
		catch (Exception e) {
			log.debug("Failed to load properties: " + e.getMessage());
		}
		return null;
	}


	private Token tryLoadMyExpTestToken()
	{
		Properties props = new Properties();
		try {
			props.load(getClass().getClassLoader().getResourceAsStream(
				"testToken.properties"));
			String token = props.getProperty("token");
			String secret = props.getProperty("secret");
			if (token != null && secret != null) {
				return new Token(token, secret);
			}
		}
		catch (Exception e) {
			log.debug("Failed to load properties: " + e.getMessage());
		}
		return null;
	}


	private void startMyExpAuthorization()
	{
		String oauthCallbackURL = WicketUtils.getCompleteUrl(this,
			HomePage.class);

		OAuthService service = MyExpApi.getOAuthService(oauthCallbackURL);
		Token requestToken = service.getRequestToken();
		getSession()
				.setAttribute(Constants.SESSION_REQUEST_TOKEN, requestToken);
		String authorizationUrl = service.getAuthorizationUrl(requestToken);
		log.debug("Request token: " + requestToken.toString() + " service: "
				+ service.getAuthorizationUrl(requestToken));
		getRequestCycle().scheduleRequestHandlerAfterCurrent(
			new RedirectRequestHandler(authorizationUrl));
	}


	protected void startDlibraAuthorization()
	{
		//		String oauthCallbackURL = WicketUtils.getCompleteUrl(this,
		//			HomePage.class);
		String oauthCallbackURL = "http://localhost:8080";

		OAuthService service = DlibraApi.getOAuthService(oauthCallbackURL);
		String authorizationUrl = service.getAuthorizationUrl(null);
		getRequestCycle().scheduleRequestHandlerAfterCurrent(
			new RedirectRequestHandler(authorizationUrl));
	}


	/**
	 * @param pageParameters
	 * @param service
	 * @return
	 */
	private Token retrieveMyExpAccessToken(PageParameters pageParameters,
			OAuthService service)
	{
		Token accessToken = null;
		if (!pageParameters.get(MyExpApi.OAUTH_VERIFIER).isEmpty()) {
			Verifier verifier = new Verifier(pageParameters.get(
				MyExpApi.OAUTH_VERIFIER).toString());
			Token requestToken = (Token) getSession().getAttribute(
				Constants.SESSION_REQUEST_TOKEN);
			log.debug("Request token: " + requestToken.toString()
					+ " verifier: " + verifier.getValue() + " service: "
					+ service.getAuthorizationUrl(requestToken));
			accessToken = service.getAccessToken(requestToken, verifier);
		}
		return accessToken;
	}


	/**
	 * @param pageParameters
	 * @param service
	 * @return
	 */
	private Token retrieveDlibraAccessToken(PageParameters pageParameters,
			OAuthService service)
	{
		Token accessToken = null;
		//TODO in the OAuth 2.0 implicit grant flow the access token is sent 
		//in URL fragment - how to retrieve it in Wicket?
		if (!pageParameters.get("access_token").isEmpty()
				&& !pageParameters.get("token_type").isEmpty()) {
			if (pageParameters.get("token_type").equals("bearer")) {
				accessToken = new Token(pageParameters.get("access_token")
						.toString(), null);
			}
			else {
				error("Unsupported token type: "
						+ pageParameters.get("token_type").toString());
			}
		}
		else if (!pageParameters.get("code").isEmpty()) {
			String url = new DlibraApi().getAccessTokenEndpoint()
					+ "?grant_type=authorization_code&code="
					+ pageParameters.get("code").toString();
			try {
				HttpClient httpclient = new DefaultHttpClient();
				HttpGet httpget = new HttpGet(url);
				HttpResponse response = httpclient.execute(httpget);
				HttpEntity entity = response.getEntity();
				ObjectMapper mapper = new ObjectMapper();
				@SuppressWarnings("unchecked")
				Map<String, String> responseData = mapper.readValue(
					entity.getContent(), Map.class);
				entity.getContent().close();
				if (response.getStatusLine().getStatusCode() == 200) {
					if (responseData.containsKey("access_token")
							&& responseData.containsKey("token_type")) {
						if (responseData.get("token_type").equalsIgnoreCase(
							"bearer")) {
							accessToken = new Token(
									responseData.get("access_token"), null);
						}
						else {
							error("Unsupported access token type: "
									+ responseData.get("token_type"));
						}
					}
					else {
						error("Missing keys from access token endpoint response");
					}
				}
				else {
					error(String.format(
						"Access token endpoint returned error %s (%s)",
						responseData.get("error"),
						responseData.get("error_description")));
				}
			}
			catch (JsonParseException e) {
				error("Error in parsing access token endpoint response: "
						+ e.getMessage());
			}
			catch (JsonMappingException e) {
				error("Error in parsing access token endpoint response: "
						+ e.getMessage());
			}
			catch (IOException e) {
				error("Error in parsing access token endpoint response: "
						+ e.getMessage());
			}
		}
		return accessToken;
	}

}

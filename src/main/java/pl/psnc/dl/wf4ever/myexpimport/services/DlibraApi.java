/**
 * 
 */
package pl.psnc.dl.wf4ever.myexpimport.services;

import org.apache.log4j.Logger;
import org.scribe.builder.api.DefaultApi20;
import org.scribe.model.OAuthConfig;
import org.scribe.oauth.OAuthService;

import pl.psnc.dl.wf4ever.myexpimport.utils.OAuth20ServiceImpl;

/**
 * @author Piotr Hołubowicz
 *
 */
public class DlibraApi
	extends DefaultApi20
{

	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger(DlibraApi.class);

	// client id
	private static final String CONSUMER_KEY = "b3c31959-ced6-41aa-b";

	private static final String SHARED_SECRET = "foobar";


	public static OAuthService getOAuthService(String callbackURL)
	{
		//		return new ServiceBuilder().provider(DlibraApi.class)
		//				.apiKey(DlibraApi.CONSUMER_KEY)
		//				.apiSecret(DlibraApi.SHARED_SECRET).build();
		return new OAuth20ServiceImpl(new DlibraApi(), new OAuthConfig(
				CONSUMER_KEY, SHARED_SECRET, callbackURL, null, null));
	}


	/* (non-Javadoc)
	 * @see org.scribe.builder.api.DefaultApi10a#getAccessTokenEndpoint()
	 */
	@Override
	public String getAccessTokenEndpoint()
	{
		return "http://sandbox.wf4ever-project.org/users2/accesstoken";
	}


	@Override
	public String getAuthorizationUrl(OAuthConfig config)
	{
		// note: in response type "token" it is required to add redirection URI
		return String
				.format(
					"http://sandbox.wf4ever-project.org/users2/authorize?client_id=%s&response_type=%s",
					CONSUMER_KEY, "code");
	}
}

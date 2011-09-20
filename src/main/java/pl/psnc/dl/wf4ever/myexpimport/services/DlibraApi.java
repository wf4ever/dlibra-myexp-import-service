/**
 * 
 */
package pl.psnc.dl.wf4ever.myexpimport.services;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

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

	private static final Logger log = Logger.getLogger(DlibraApi.class);

	// client id
	private static final String CONSUMER_KEY = "439c06d6-744a-4b1d-9";

	private static final String SHARED_SECRET = "bar";

	public static final String OAUTH_TOKEN = "oauth_token";


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
		return null;
	}


	@Override
	public String getAuthorizationUrl(OAuthConfig config)
	{
		try {
			return String.format(
				"http://sandbox.wf4ever-project.org/users2/authorize?redirect_uri=%s&client_id=%s&response_type=%s",
				URLEncoder.encode(config.getCallback(), "UTF-8"), CONSUMER_KEY, "token");
		}
		catch (UnsupportedEncodingException e) {
			log.error(e);
			return null;
		}
	}

}

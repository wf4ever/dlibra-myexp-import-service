/**
 * 
 */
package pl.psnc.dl.wf4ever.myexpimport.services;

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

	private static final String CONSUMER_KEY = "foo";

	private static final String SHARED_SECRET = "bar";


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
		return "foobar";
	}


	@Override
	public String getAuthorizationUrl(OAuthConfig config)
	{
		return null;
	}

}

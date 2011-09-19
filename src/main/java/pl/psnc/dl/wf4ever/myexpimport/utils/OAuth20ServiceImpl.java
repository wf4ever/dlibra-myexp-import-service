package pl.psnc.dl.wf4ever.myexpimport.utils;

import org.scribe.builder.api.DefaultApi20;
import org.scribe.model.OAuthConfig;
import org.scribe.model.OAuthConstants;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Token;

public class OAuth20ServiceImpl
	extends org.scribe.oauth.OAuth20ServiceImpl
{

	public OAuth20ServiceImpl(DefaultApi20 api, OAuthConfig config)
	{
		super(api, config);
	}


	@Override
	public void signRequest(Token accessToken, OAuthRequest request)
	{
		request.addHeader(OAuthConstants.HEADER,
			"Bearer " + accessToken.getToken());
	}

}

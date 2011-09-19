/**
 * 
 */
package pl.psnc.dl.wf4ever.myexpimport.services;

import org.scribe.model.Response;

/**
 * @author Piotr Hołubowicz
 *
 */
public class OAuthException
	extends Exception
{

	private static final long serialVersionUID = 4341885401436426808L;

	private Response response;


	public OAuthException(Response response, String explanation)
	{
		super(String.format("Error %d: %s", response.getCode(), explanation));
		this.response = response;
	}


	public OAuthException(Response response)
	{
		super(String.format("Error %d", response.getCode()));
		this.response = response;
	}


	/**
	 * @return the response
	 */
	public Response getResponse()
	{
		return response;
	}

}

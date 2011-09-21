/**
 * 
 */
package pl.psnc.dl.wf4ever.myexpimport.services;

import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.wicket.util.crypt.Base64;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;

/**
 * @author Piotr Hołubowicz
 *
 */
public class DlibraService
{

	private static final Logger log = Logger.getLogger(DlibraService.class);

	public static final String DEFAULT_VERSION = "main";

	private static final String URI_SCHEME = "http";

	private static final String URI_HOST = "sandbox.wf4ever-project.org";

	private static final String URI_PATH_BASE = "/rosrs4";

	private static final String URI_WORKSPACE = URI_PATH_BASE + "/workspaces";

	private static final String URI_WORKSPACE_ID = URI_WORKSPACE + "/%s";

	private static final String URI_ROS = URI_WORKSPACE_ID + "/ROs";

	private static final String URI_RO_ID = URI_ROS + "/%s";

	private static final String URI_VERSION_ID = URI_RO_ID + "/%s";

	private static final String URI_RESOURCE = URI_VERSION_ID + "/%s";

	private static final OAuthService dLibraService = DlibraApi.getOAuthService(null);


	public static boolean createWorkspace(String workspaceId, Token dLibraToken)
		throws Exception
	{
		boolean created = true;

		String url = createWorkspaceURL().toString();
		OAuthRequest request = new OAuthRequest(Verb.POST, url);
		request.addHeader("Content-Type", "text/plain");
		request.addPayload(workspaceId);
		dLibraService.signRequest(dLibraToken, request);
		Response response = request.send();
		if (response.getCode() != HttpURLConnection.HTTP_CREATED) {
			if (response.getCode() == HttpURLConnection.HTTP_CONFLICT) {
				log.warn("Creating a workspace that already exists in dLibra");
				created = false;
			}
			else {
				throw new Exception("Error when creating workspace, response: "
						+ response.getCode() + " " + response.getBody());
			}
		}
		return created;
	}


	public static void deleteWorkspace(String workspaceId, Token dLibraToken)
		throws Exception
	{
		String url = createWorkspaceIdURL(workspaceId).toString();
		OAuthRequest request = new OAuthRequest(Verb.DELETE, url);
		dLibraService.signRequest(dLibraToken, request);
		Response response = request.send();
		
		if (response.getCode() != HttpURLConnection.HTTP_CONFLICT) {
			throw new Exception("Error when deleting workspace, response: "
					+ response.getCode() + " " + response.getBody());
		}
	}


	public static boolean createResearchObjectAndVersion(String workspaceId, String name,
			Token dLibraToken, boolean ignoreIfExists)
		throws Exception
	{
		return createResearchObject(workspaceId, name, dLibraToken, ignoreIfExists)
				&& createVersion(workspaceId, name, dLibraToken, ignoreIfExists);
	}


	/**
	 * Creates a Research Object.
	 * @param name RO identifier
	 * @param user dLibra user model
	 * @param ignoreIfExists should it finish without throwing exception if ROSRS returns 409?
	 * @return true only if ROSRS returns 201 Created
	 * @throws Exception if ROSRS doesn't return 201 Created (or 409 if ignoreIfExists is true)
	 */
	public static boolean createResearchObject(String workspaceId, String name, Token dLibraToken,
			boolean ignoreIfExists)
		throws Exception
	{
		String url = createROsURL(workspaceId).toString();
		OAuthRequest request = new OAuthRequest(Verb.POST, url);
		request.addHeader("Content-type", "text/plain");
		request.addPayload(name);
		dLibraService.signRequest(dLibraToken, request);
		Response response = request.send();
		if (response.getCode() == HttpURLConnection.HTTP_CREATED) {
			return true;
		}
		else if (response.getCode() == HttpURLConnection.HTTP_CONFLICT
				&& ignoreIfExists) {
			return false;
		}
		else {
			throw new Exception("Error when creating RO " + name
					+ ", response: " + response.getCode() + " "
					+ response.getBody());
		}
	}


	/**
	 * Creates a version "main" in a RO.
	 * @param roName RO identifier
	 * @param user dLibra user model
	 * @param ignoreIfExists should it finish without throwing exception if ROSRS returns 409?
	 * @return true only if ROSRS returns 201 Created
	 * @throws Exception if ROSRS doesn't return 201 Created (or 409 if ignoreIfExists is true)
	 */
	public static boolean createVersion(String workspaceId, String roName, Token dLibraToken, 
			boolean ignoreIfExists)
		throws Exception
	{
		String url = createROIdURL(workspaceId, roName).toString();
		OAuthRequest request = new OAuthRequest(Verb.POST, url);
		request.addHeader("Content-type", "text/plain");
		request.addPayload(DEFAULT_VERSION);
		dLibraService.signRequest(dLibraToken, request);
		Response response = request.send();
		if (response.getCode() == HttpURLConnection.HTTP_CREATED) {
			return true;
		}
		else if (response.getCode() == HttpURLConnection.HTTP_CONFLICT
				&& ignoreIfExists) {
			return false;
		}
		else {
			throw new Exception("Error when creating version, response: "
					+ response.getCode() + " " + response.getBody());
		}
	}


	public static void sendResource(String workspaceId, String path, String roName, String content,
			String contentType, Token dLibraToken)
		throws Exception
	{
		String url = createResourceURL(workspaceId, roName,
			DEFAULT_VERSION, path).toString();
		OAuthRequest request = new OAuthRequest(Verb.PUT, url);
		request.addHeader("Content-Type", contentType != null ? contentType
				: "text/plain");
		request.addPayload(content);
		dLibraService.signRequest(dLibraToken, request);
		Response response = request.send();
		if (response.getCode() != HttpURLConnection.HTTP_OK) {
			throw new Exception("Error when sending resource " + path
					+ ", response: " + response.getCode() + " "
					+ response.getBody());
		}
	}



	public static Token generateAccessToken(String username, String password)
	{
		String token = Base64.encodeBase64String((username + ":" + password)
				.getBytes());
		token = StringUtils.trim(token);
		log.debug(String.format("Username %s, password %s, access token %s",
			username, password, token));
		return new Token(token, null);
	}


	private static URL createWorkspaceURL()
	{
		try {
			return new URI(URI_SCHEME, URI_HOST, URI_WORKSPACE, null).toURL();
		}
		catch (Exception e) {
			log.error(e);
			return null;
		}
	}


	private static URL createWorkspaceIdURL(String workspaceId)
	{
		try {
			String path = String.format(URI_WORKSPACE_ID, workspaceId);
			return new URI(URI_SCHEME, URI_HOST, path, null).toURL();
		}
		catch (Exception e) {
			log.error(e);
			return null;
		}
	}


	private static URL createROsURL(String workspaceId)
	{
		try {
			String path = String.format(URI_ROS, workspaceId);
			return new URI(URI_SCHEME, URI_HOST, path, null).toURL();
		}
		catch (Exception e) {
			log.error(e);
			return null;
		}
	}


	private static URL createROIdURL(String workspaceId, String roId)
	{
		try {
			String path = String.format(URI_RO_ID, workspaceId, roId);
			return new URI(URI_SCHEME, URI_HOST, path, null).toURL();
		}
		catch (Exception e) {
			log.error(e);
			return null;
		}
	}


	private static URL createResourceURL(String workspaceId, String roId,
			String versionId, String resource)
	{
		try {
			String path = String.format(URI_RESOURCE, workspaceId, roId, DEFAULT_VERSION,
				resource);
			return new URI(URI_SCHEME, URI_HOST, path, null).toURL();
		}
		catch (Exception e) {
			log.error(e);
			return null;
		}
	}

}

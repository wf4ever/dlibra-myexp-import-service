/**
 * 
 */
package pl.psnc.dl.wf4ever.myexpimport.services;

import java.io.StringReader;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import org.apache.log4j.Logger;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;

import pl.psnc.dl.wf4ever.myexpimport.model.ImportModel;
import pl.psnc.dl.wf4ever.myexpimport.model.ImportModel.ImportStatus;
import pl.psnc.dl.wf4ever.myexpimport.model.ImportModel.WorkspaceType;
import pl.psnc.dl.wf4ever.myexpimport.model.ResearchObject;
import pl.psnc.dl.wf4ever.myexpimport.model.myexp.InternalPackItem;
import pl.psnc.dl.wf4ever.myexpimport.model.myexp.InternalPackItemHeader;
import pl.psnc.dl.wf4ever.myexpimport.model.myexp.Pack;
import pl.psnc.dl.wf4ever.myexpimport.model.myexp.PackHeader;
import pl.psnc.dl.wf4ever.myexpimport.model.myexp.Resource;
import pl.psnc.dl.wf4ever.myexpimport.model.myexp.ResourceHeader;
import pl.psnc.dl.wf4ever.myexpimport.model.myexp.SimpleResource;
import pl.psnc.dl.wf4ever.myexpimport.model.myexp.SimpleResourceHeader;

/**
 * @author Piotr Hołubowicz
 *
 */
public class MyExpImportService
{

	public static void startImport(ImportModel model, Token myExpAccessToken,
			Token dLibraAccessToken, String consumerKey, String consumerSecret)
	{
		new ImportThread(model, myExpAccessToken, dLibraAccessToken, consumerKey, consumerSecret).start();
	}

	private static class ImportThread
		extends Thread
	{

		private static final Logger log = Logger.getLogger(ImportThread.class);

		private final OAuthService service;

		private ImportModel model;

		private Token myExpToken;

		private Token dLibraToken;


		public ImportThread(ImportModel importModel, Token myExpAccessToken,
				Token dLibraToken, String consumerKey, String consumerSecret)
		{
			super();
			model = importModel;
			myExpToken = myExpAccessToken;
			this.dLibraToken = dLibraToken;
			service = MyExpApi.getOAuthService(consumerKey, consumerSecret);
		}


		public void run()
		{
			model.setStatus(ImportStatus.RUNNING);
			try {
				if (model.getWorkspaceType() == WorkspaceType.NEW) {
					createWorkspace(model.getWorkspaceId());
				}
				for (ResearchObject ro : model.getResearchObjects()) {
					try {
						importRO(ro);
					}
					catch (Exception e) {
						log.error("Error during import", e);
						model.setMessage(e.getMessage());
					}
				}
			}
			catch (Exception e1) {
				log.error("Error when creating workspace", e1);
				model.setMessage(e1.getMessage());
			}
			model.setMessage("Finished");
			model.setStatus(ImportStatus.FINISHED);
		}


		/**
		 * @param model
		 * @param ro
		 * @param dLibraUser
		 * @throws Exception
		 */
		private void createWorkspace(String workspaceId)
			throws Exception
		{
			model.setMessage(String.format("Creating a workspace \"%s\"",
				workspaceId));
			if (!DlibraService.createWorkspace(workspaceId, dLibraToken)) {
				model.setMessage("Merged with an existing workspace");
			}
		}


		/**
		 * @param model
		 * @param dLibraUser
		 * @param ro
		 * @throws Exception 
		 */
		private void importRO(ResearchObject ro)
			throws Exception
		{
			createRO(ro);
			importSimpleResources(ro.getFiles(), ro.getName());
			importSimpleResources(ro.getWorkflows(), ro.getName());
			importPacks(ro);
		}


		/**
		 * @param model
		 * @param ro
		 * @param myExpToken
		 * @param dLibraUser
		 * @throws JAXBException
		 * @throws Exception
		 */
		private void importSimpleResources(
				List< ? extends SimpleResourceHeader> resourceHeaders,
				String roName)
			throws JAXBException, Exception
		{
			for (SimpleResourceHeader header : resourceHeaders) {
				SimpleResource r = importSimpleResource(header, roName, "",
					header.getResourceClass());
				importResourceMetadata(r, r.getFilename() + ".rdf", roName, "");
			}
		}


		/**
		 * @param model
		 * @param ro
		 * @param myExpToken
		 * @param dLibraUser
		 * @throws JAXBException
		 * @throws Exception
		 */
		private void importPacks(ResearchObject ro)
			throws JAXBException, Exception
		{
			for (PackHeader packHeader : ro.getPacks()) {
				Pack pack = (Pack) getResource(packHeader, Pack.class);
				importResourceMetadata(pack, pack.getId() + ".rdf",
					ro.getName(), "");

				for (InternalPackItemHeader packItemHeader : pack
						.getResources()) {
					importInternalPackItem(ro, pack, packItemHeader);
				}
			}
		}


		/**
		 * @param model
		 * @param ro
		 * @param myExpToken
		 * @param user
		 * @param pack
		 * @param r
		 * @throws JAXBException
		 * @throws Exception
		 */
		private void importInternalPackItem(ResearchObject ro, Pack pack,
				InternalPackItemHeader packItemHeader)
			throws JAXBException, Exception
		{
			InternalPackItem internalItem = (InternalPackItem) getResource(
				packItemHeader, InternalPackItem.class);
			SimpleResourceHeader resourceHeader = internalItem.getItem();
			importSimpleResource(resourceHeader, ro.getName(), pack.getId()
					+ "/", resourceHeader.getResourceClass());
		}


		private SimpleResource importSimpleResource(SimpleResourceHeader res,
				String roName, String path,
				Class< ? extends SimpleResource> resourceClass)
			throws Exception
		{
			SimpleResource r = (SimpleResource) getResource(res, resourceClass);

			String filename = path + r.getFilename();
			model.setMessage(String.format("Uploading %s", filename));
			DlibraService.sendResource(model.getWorkspaceId(), filename,
				roName, r.getContentDecoded(), r.getContentType(), dLibraToken);

			return r;
		}


		/**
		 * @param res
		 * @param path
		 * @param resourceClass
		 * @return
		 * @throws OAuthException
		 * @throws JAXBException
		 */
		private Resource getResource(ResourceHeader res,
				Class< ? extends Resource> resourceClass)
			throws OAuthException, JAXBException
		{
			model.setMessage(String.format("Downloading %s",
				res.getResourceUrl()));
			Response response = OAuthHelpService.sendRequest(service, Verb.GET,
				res.getResourceUrl(), myExpToken);
			Resource r = (Resource) createMyExpResource(response.getBody(),
				resourceClass);
			return r;
		}


		private void importResourceMetadata(Resource res, String filename,
				String roName, String path)
			throws Exception
		{
			model.setMessage(String.format("Downloading metadata file %s",
				res.getResource()));
			Response response = OAuthHelpService.sendRequest(service, Verb.GET,
				res.getResource(), myExpToken, "application/rdf+xml");
			// in the future, the RDF could be parsed (and somewhat validated) and the filename can be extracted from it
			String rdf = response.getBody();

			model.setMessage(String.format("Uploading metadata file %s",
				filename));
			DlibraService.sendResource(model.getWorkspaceId(), filename,
				roName, rdf.getBytes(), "application/rdf+xml", dLibraToken);
		}


		/**
		 * @param model
		 * @param ro
		 * @param dLibraUser
		 * @throws Exception
		 */
		private void createRO(ResearchObject ro)
			throws Exception
		{
			model.setMessage(String.format("Creating a Research Object \"%s\"",
				ro.getName()));
			if (!DlibraService.createResearchObjectAndVersion(
				model.getWorkspaceId(), ro.getName(), dLibraToken,
				model.isMergeROs())) {
				model.setMessage("Merged with an existing Research Object");
			}
		}


		private static Object createMyExpResource(String xml,
				Class< ? extends Resource> resourceClass)
			throws JAXBException
		{
			JAXBContext jc = JAXBContext.newInstance(resourceClass);
			Unmarshaller u = jc.createUnmarshaller();
			StringBuffer xmlStr = new StringBuffer(xml);
			return u.unmarshal(new StreamSource(new StringReader(xmlStr
					.toString())));
		}

	}
}
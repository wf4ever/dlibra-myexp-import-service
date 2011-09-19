/**
 * 
 */
package pl.psnc.dl.wf4ever.myexpimport.model.myexp;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlValue;

/**
 * @author Piotr Hołubowicz
 *
 */
public abstract class ResourceHeader
	implements Serializable

{

	/**
	 * 
	 */
	private static final long serialVersionUID = -9038815722609845400L;
	
	private String uri;

	private String resource;

	private String title;


	public ResourceHeader()
	{

	}


	/**
	 * @return the uri
	 */
	@XmlAttribute
	public String getUri()
	{
		return uri;
	}


	/**
	 * @param uri the uri to set
	 */
	public void setUri(String uri)
	{
		this.uri = uri;
	}


	/**
	 * @return the resource
	 */
	@XmlAttribute
	public String getResource()
	{
		return resource;
	}


	/**
	 * @param resource the resource to set
	 */
	public void setResource(String resource)
	{
		this.resource = resource;
	}


	/**
	 * @return the title
	 */
	@XmlValue
	public String getTitle()
	{
		return title;
	}


	/**
	 * @param title the title to set
	 */
	public void setTitle(String title)
	{
		this.title = title;
	}


	public abstract String getResourceUrl();
	
	public abstract Class<? extends Resource> getResourceClass();

}

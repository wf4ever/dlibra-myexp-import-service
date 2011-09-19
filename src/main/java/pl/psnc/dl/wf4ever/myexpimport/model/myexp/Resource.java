/**
 * 
 */
package pl.psnc.dl.wf4ever.myexpimport.model.myexp;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

/**
 * @author Piotr Hołubowicz
 *
 */
public abstract class Resource
	implements Serializable

{

	/**
	 * 
	 */
	private static final long serialVersionUID = -9038815722609845400L;

	private String uri;

	private String resource;

	private String title;

	private int id;


	public Resource()
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
	@XmlElement
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


	/**
	 * @return the id
	 */
	@XmlElement
	public int getId()
	{
		return id;
	}


	/**
	 * @param id the id to set
	 */
	public void setId(int id)
	{
		this.id = id;
	}
}

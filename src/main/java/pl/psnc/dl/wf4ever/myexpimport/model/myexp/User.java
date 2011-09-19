/**
 * 
 */
package pl.psnc.dl.wf4ever.myexpimport.model.myexp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Piotr Hołubowicz
 *
 */
@XmlRootElement(name = "user")
public class User
	implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4428655508723987304L;

	private String uri;

	private String resource;

	private int id;

	private String openId;

	private String name;

	private String email;

	private String city;

	private String country;

	private String website;

	private List<PackHeader> packs = new ArrayList<PackHeader>();

	private List<WorkflowHeader> workflows = new ArrayList<WorkflowHeader>();

	private List<FileHeader> files = new ArrayList<FileHeader>();


	public User()
	{

	}


	public int getId()
	{
		return id;
	}


	public void setId(int id)
	{
		this.id = id;
	}


	/**
	 * @return the openId
	 */
	@XmlElement(name = "openid-url")
	public String getOpenId()
	{
		return openId;
	}


	/**
	 * @param openId the openId to set
	 */
	public void setOpenId(String openId)
	{
		this.openId = openId;
	}


	public String getName()
	{
		return name;
	}


	public void setName(String name)
	{
		this.name = name;
	}


	public String getEmail()
	{
		return email;
	}


	public void setEmail(String email)
	{
		this.email = email;
	}


	public String getCity()
	{
		return city;
	}


	public void setCity(String city)
	{
		this.city = city;
	}


	public String getCountry()
	{
		return country;
	}


	public void setCountry(String country)
	{
		this.country = country;
	}


	public String getWebsite()
	{
		return website;
	}


	public void setWebsite(String website)
	{
		this.website = website;
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
	 * @return the workflows
	 */
	@XmlElementWrapper(name = "workflows")
	@XmlElement(name = "workflow")
	public List<WorkflowHeader> getWorkflows()
	{
		return workflows;
	}


	/**
	 * @param workflows the workflows to set
	 */
	public void setWorkflows(List<WorkflowHeader> workflows)
	{
		this.workflows = workflows;
	}


	/**
	 * @return the packs
	 */
	@XmlElementWrapper(name = "packs")
	@XmlElement(name = "pack")
	public List<PackHeader> getPacks()
	{
		return packs;
	}


	/**
	 * @param packs the packs to set
	 */
	public void setPacks(List<PackHeader> packs)
	{
		this.packs = packs;
	}


	/**
	 * @return the files
	 */
	@XmlElementWrapper(name = "files")
	@XmlElement(name = "file")
	public List<FileHeader> getFiles()
	{
		return files;
	}


	/**
	 * @param files the files to set
	 */
	public void setFiles(List<FileHeader> files)
	{
		this.files = files;
	}

}

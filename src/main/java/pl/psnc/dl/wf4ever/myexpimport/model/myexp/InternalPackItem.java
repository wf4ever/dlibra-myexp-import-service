/**
 * 
 */
package pl.psnc.dl.wf4ever.myexpimport.model.myexp;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Piotr Hołubowicz
 *
 */
@XmlRootElement(name = "internal-pack-item")
public class InternalPackItem
	extends Resource

{

	/**
	 * 
	 */
	private static final long serialVersionUID = -9038815722609845400L;

	private String uri;

	private String resource;

	private int id;

	private List<SimpleResourceHeader> items;


	public InternalPackItem()
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


	/**
	 * @return the item
	 */
	public SimpleResourceHeader getItem()
	{
		if (items == null || items.isEmpty())
			return null;
		else
			return items.get(0);
	}


	/**
	 * @return the items
	 */
	@XmlElementWrapper(name = "item")
	@XmlElements({ @XmlElement(name = "file", type = FileHeader.class),
			@XmlElement(name = "workflow", type = WorkflowHeader.class)})
	public List<SimpleResourceHeader> getItems()
	{
		return items;
	}


	/**
	 * @param items the items to set
	 */
	public void setItems(List<SimpleResourceHeader> items)
	{
		this.items = items;
	}
}

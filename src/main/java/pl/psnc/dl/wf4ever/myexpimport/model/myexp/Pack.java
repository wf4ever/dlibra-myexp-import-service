/**
 * 
 */
package pl.psnc.dl.wf4ever.myexpimport.model.myexp;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Piotr Hołubowicz
 *
 */
@XmlRootElement(name = "pack")
public class Pack
	extends Resource
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3146768347985843474L;

	private List<InternalPackItemHeader> resources = new ArrayList<InternalPackItemHeader>();


	/**
	 * @return the resources
	 */
	@XmlElementWrapper(name = "internal-pack-items")
	@XmlElements({ @XmlElement(name = "file", type = InternalPackItemHeader.class),
			@XmlElement(name = "workflow", type = InternalPackItemHeader.class)})
	public List<InternalPackItemHeader> getResources()
	{
		return resources;
	}


	/**
	 * @param resources the resources to set
	 */
	public void setResources(List<InternalPackItemHeader> resources)
	{
		this.resources = resources;
	}

	@Override
	public String toString()
	{
		return String.format("pack \"%s\"", getId());
	}

}

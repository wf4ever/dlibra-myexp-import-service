/**
 * 
 */
package pl.psnc.dl.wf4ever.myexpimport.model.myexp;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Piotr Hołubowicz
 *
 */
@XmlRootElement(name = "pack")
public class PackHeader
	extends ResourceHeader
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3146768347985843474L;


	@Override
	public String getResourceUrl()
	{
		return getUri() + "&elements=internal-pack-items,id,title";
	}


	@Override
	public Class<Pack> getResourceClass()
	{
		return Pack.class;
	}

}

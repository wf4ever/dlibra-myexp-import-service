/**
 * 
 */
package pl.psnc.dl.wf4ever.myexpimport.model.myexp;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Piotr Hołubowicz
 *
 */
@XmlRootElement(name = "workflow")
public class WorkflowHeader
	extends SimpleResourceHeader
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3019438116219497825L;


	@Override
	public String getResourceUrl()
	{
		return getUri() + "&elements=content,content-uri,content-type,id,title";
	}


	@Override
	public Class<Workflow> getResourceClass()
	{
		return Workflow.class;
	}
}

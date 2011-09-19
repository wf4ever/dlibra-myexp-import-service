/**
 * 
 */
package pl.psnc.dl.wf4ever.myexpimport.model.myexp;


/**
 * @author Piotr Hołubowicz
 *
 */
public class InternalPackItemHeader
	extends ResourceHeader
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1547898914095065327L;


	@Override
	public String getResourceUrl()
	{
		return getUri() + "&elements=id,item";
	}


	@Override
	public Class<InternalPackItem> getResourceClass()
	{
		return InternalPackItem.class;
	}

}

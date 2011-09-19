/**
 * 
 */
package pl.psnc.dl.wf4ever.myexpimport.model.myexp;

/**
 * @author Piotr Hołubowicz
 *
 */
public abstract class SimpleResourceHeader
	extends ResourceHeader

{

	/**
	 * 
	 */
	private static final long serialVersionUID = -9038815722609845400L;


	public SimpleResourceHeader()
	{

	}


	public abstract Class< ? extends SimpleResource> getResourceClass();

}

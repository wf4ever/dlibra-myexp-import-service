/**
 * 
 */
package pl.psnc.dl.wf4ever.myexpimport.model.myexp;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Piotr Hołubowicz
 *
 */
@XmlRootElement(name = "file")
public class File
	extends SimpleResource
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1547898914095065327L;

	private String filename;


	/**
	 * @return the filename
	 */
	@XmlElement
	public String getFilename()
	{
		return filename.trim();
	}


	/**
	 * @param filename the filename to set
	 */
	public void setFilename(String filename)
	{
		this.filename = filename;
	}


	@Override
	public String toString()
	{
		return String.format("file \"%s\"", filename);
	}

}

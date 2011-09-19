/**
 * 
 */
package pl.psnc.dl.wf4ever.myexpimport.wizard;

import org.apache.wicket.extensions.wizard.dynamic.DynamicWizardStep;
import org.apache.wicket.extensions.wizard.dynamic.IDynamicWizardStep;
import org.apache.wicket.model.Model;

import pl.psnc.dl.wf4ever.myexpimport.model.ImportModel;

/**
 * @author Piotr Hołubowicz
 *
 */
public abstract class AbstractStep
	extends DynamicWizardStep
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 429662828637124100L;


	public AbstractStep(IDynamicWizardStep previousStep, String title,
			ImportModel model)
	{
		super(previousStep, "<h2 class=\"title\">" + title + "</h2>", null,
				new Model<ImportModel>(model));
	}

}

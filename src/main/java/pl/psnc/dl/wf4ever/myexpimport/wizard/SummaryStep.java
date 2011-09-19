/**
 * 
 */
package pl.psnc.dl.wf4ever.myexpimport.wizard;

import org.apache.wicket.extensions.wizard.dynamic.IDynamicWizardStep;

import pl.psnc.dl.wf4ever.myexpimport.model.ImportModel;


/**
 * @author Piotr Hołubowicz
 *
 */
public class SummaryStep
	extends AbstractStep
{

	private static final long serialVersionUID = -4003286657493791544L;


	public SummaryStep(IDynamicWizardStep previousStep,
			ImportModel model)
	{
		super(previousStep, "Summary", model);
	}


	/* (non-Javadoc)
	 * @see org.apache.wicket.extensions.wizard.dynamic.IDynamicWizardStep#isLastStep()
	 */
	@Override
	public boolean isLastStep()
	{
		return true;
	}


	/* (non-Javadoc)
	 * @see org.apache.wicket.extensions.wizard.dynamic.IDynamicWizardStep#next()
	 */
	@Override
	public IDynamicWizardStep next()
	{
		return null;
	}

}

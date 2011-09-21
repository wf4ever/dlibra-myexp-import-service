/**
 * 
 */
package pl.psnc.dl.wf4ever.myexpimport.wizard;

import org.apache.wicket.extensions.wizard.dynamic.IDynamicWizardStep;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.validation.validator.PatternValidator;

import pl.psnc.dl.wf4ever.myexpimport.model.ImportModel;

/**
 * @author Piotr Hołubowicz
 *
 */
public class ChooseWorkspaceStep
	extends AbstractStep
{

	private static final long serialVersionUID = -3238571883021517707L;


	public ChooseWorkspaceStep(IDynamicWizardStep previousStep,
			ImportModel model)
	{
		super(previousStep, "Choose workspace", model);

		Form<ImportModel> form = new Form<ImportModel>("form",
				new CompoundPropertyModel<ImportModel>(model));
		add(form);
		form.add(new RequiredTextField<String>("workspaceId").add(
			new PatternValidator("[\\w]+")));
	}


	/* (non-Javadoc)
	 * @see org.apache.wicket.extensions.wizard.dynamic.IDynamicWizardStep#isLastStep()
	 */
	@Override
	public boolean isLastStep()
	{
		return false;
	}


	@Override
	public IDynamicWizardStep next()
	{
		return new SelectResourcesStep(this,
				(ImportModel) this.getDefaultModelObject());
	}

}

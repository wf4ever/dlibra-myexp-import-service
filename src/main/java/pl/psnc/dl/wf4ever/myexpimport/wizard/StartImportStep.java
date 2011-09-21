/**
 * 
 */
package pl.psnc.dl.wf4ever.myexpimport.wizard;

import org.apache.wicket.extensions.wizard.dynamic.IDynamicWizardStep;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.Model;

import pl.psnc.dl.wf4ever.myexpimport.model.ImportModel;

/**
 * @author Piotr Hołubowicz
 *
 */
public class StartImportStep
	extends AbstractStep
{

	private static final long serialVersionUID = 4637256013660809942L;


	public StartImportStep(ImportModel model)
	{
		super(null, "Start", model);

		add(new Label("userName", new Model<String>(model.getMyExpUser()
				.getName())));
		add(new Label("packsCnt", new Model<Integer>(model.getMyExpUser()
				.getPacks().size())));
		add(new Label("workflowsCnt", new Model<Integer>(model.getMyExpUser()
				.getWorkflows().size())));
		add(new Label("filesCnt", new Model<Integer>(model.getMyExpUser()
				.getFiles().size())));

	}


	@Override
	public boolean isLastStep()
	{
		return false;
	}


	@Override
	public IDynamicWizardStep next()
	{
		return new ChooseWorkspaceStep(this,
				(ImportModel) this.getDefaultModelObject());
	}

}

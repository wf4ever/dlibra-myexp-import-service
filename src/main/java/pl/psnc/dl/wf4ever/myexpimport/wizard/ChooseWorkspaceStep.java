/**
 * 
 */
package pl.psnc.dl.wf4ever.myexpimport.wizard;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.wizard.dynamic.IDynamicWizardStep;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.Radio;
import org.apache.wicket.markup.html.form.RadioGroup;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.validation.validator.PatternValidator;
import org.scribe.model.Token;

import pl.psnc.dl.wf4ever.myexpimport.model.ImportModel;
import pl.psnc.dl.wf4ever.myexpimport.model.ImportModel.WorkspaceType;
import pl.psnc.dl.wf4ever.myexpimport.services.DlibraService;

/**
 * @author Piotr Hołubowicz
 *
 */
public class ChooseWorkspaceStep
	extends AbstractStep
{

	private static final long serialVersionUID = -3238571883021517707L;


	@SuppressWarnings("serial")
	public ChooseWorkspaceStep(IDynamicWizardStep previousStep,
			ImportModel model, Token token)
	{
		super(previousStep, "Choose workspace", model);

		List<String> workspaces = null;
		try {
			workspaces = DlibraService.getWorkspaceList(token);
		}
		catch (Exception e) {
			error(e.getMessage());
			workspaces = new ArrayList<String>();
		}
		if (!workspaces.isEmpty()) {
			model.setExistingWorkspaceId(workspaces.get(0));
		}

		Form<ImportModel> form = new Form<ImportModel>("form",
				new CompoundPropertyModel<ImportModel>(model));
		add(form);

		final DropDownChoice<String> existingWorkspacesList = new DropDownChoice<String>(
				"existingWorkspaceId", workspaces);
		existingWorkspacesList.setOutputMarkupId(true);
		final TextField<String> newWorkspaceInput = new RequiredTextField<String>(
				"newWorkspaceId");
		newWorkspaceInput.setOutputMarkupId(true);
		newWorkspaceInput.add(new PatternValidator("[\\w]+"));

		RadioGroup<WorkspaceType> radioGroup = new RadioGroup<WorkspaceType>(
				"radioGroup", new PropertyModel<WorkspaceType>(model,
						"workspaceType"));
		Radio<WorkspaceType> existingRadio = new Radio<WorkspaceType>(
				"existing", new Model<WorkspaceType>(WorkspaceType.EXISTING));
		existingRadio.add(new AjaxEventBehavior("onclick") {

			@Override
			protected void onEvent(AjaxRequestTarget target)
			{
				existingWorkspacesList.setEnabled(true);
				newWorkspaceInput.setEnabled(false);
				target.add(existingWorkspacesList);
				target.add(newWorkspaceInput);
			}

		});
		Radio<WorkspaceType> newRadio = new Radio<WorkspaceType>("new",
				new Model<WorkspaceType>(WorkspaceType.NEW));
		newRadio.add(new AjaxEventBehavior("onclick") {

			@Override
			protected void onEvent(AjaxRequestTarget target)
			{
				existingWorkspacesList.setEnabled(false);
				newWorkspaceInput.setEnabled(true);
				target.add(existingWorkspacesList);
				target.add(newWorkspaceInput);
			}

		});
		if (workspaces.isEmpty()) {
			existingRadio.setEnabled(false);
			existingWorkspacesList.setEnabled(false);
			model.setWorkspaceType(WorkspaceType.NEW);
		}
		else {
			newWorkspaceInput.setEnabled(false);
			model.setWorkspaceType(WorkspaceType.EXISTING);
		}

		radioGroup.add(existingRadio);
		radioGroup.add(newRadio);
		radioGroup.add(existingWorkspacesList);
		radioGroup.add(newWorkspaceInput);

		form.add(radioGroup);
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

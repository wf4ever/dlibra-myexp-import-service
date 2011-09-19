/**
 * 
 */
package pl.psnc.dl.wf4ever.myexpimport.wizard;

import java.util.Arrays;

import org.apache.wicket.extensions.wizard.dynamic.IDynamicWizardStep;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.RadioChoice;
import org.apache.wicket.markup.html.form.validation.IFormValidator;
import org.apache.wicket.model.PropertyModel;

import pl.psnc.dl.wf4ever.myexpimport.model.ImportModel;
import pl.psnc.dl.wf4ever.myexpimport.model.ResourceSelectionModel;
import pl.psnc.dl.wf4ever.myexpimport.model.ResourceSelectionModel.ImportType;

/**
 * @author Piotr Hołubowicz
 *
 */
public class SelectResourcesStep
	extends AbstractStep
{

	private static final long serialVersionUID = -7984392838783804920L;

	private ResourceSelectionModel selectionModel;


	@SuppressWarnings("serial")
	public SelectResourcesStep(IDynamicWizardStep previousStep,
			ImportModel model)
	{
		super(previousStep, "Select resources", model);

		selectionModel = new ResourceSelectionModel();

		RadioChoice<ImportType> importType = new RadioChoice<ImportType>(
				"importType", new PropertyModel<ImportType>(selectionModel,
						"importType"), Arrays.asList(ImportType.values()),
				new ImportTypeChoiceRenderer());

		add(importType);

		final ResourceListPanel filesDiv;
		if (model.getMyExpUser().getFiles().isEmpty()) {
			filesDiv = null;
			add(createUnvisibileDiv("filesDiv"));
		}
		else {
			filesDiv = new ResourceListPanel("filesDiv", "Files", model
					.getMyExpUser().getFiles(),
					selectionModel.getSelectedFiles(), model.getImportedFiles());
			add(filesDiv);
		}
		final ResourceListPanel workflowsDiv;
		if (model.getMyExpUser().getWorkflows().isEmpty()) {
			workflowsDiv = null;
			add(createUnvisibileDiv("workflowsDiv"));
		}
		else {
			workflowsDiv = new ResourceListPanel("workflowsDiv", "Workflows",
					model.getMyExpUser().getWorkflows(),
					selectionModel.getSelectedWorkflows(),
					model.getImportedWorkflows());
			add(workflowsDiv);
		}
		final ResourceListPanel packsDiv;
		if (model.getMyExpUser().getPacks().isEmpty()) {
			packsDiv = null;
			add(createUnvisibileDiv("packsDiv"));
		}
		else {
			packsDiv = new ResourceListPanel("packsDiv", "Packs", model
					.getMyExpUser().getPacks(),
					selectionModel.getSelectedPacks(), model.getImportedPacks());
			add(packsDiv);
		}

		add(new IFormValidator() {

			@Override
			public void validate(Form< ? > form)
			{
				if (filesDiv != null)
					filesDiv.commit();
				if (workflowsDiv != null)
					workflowsDiv.commit();
				if (packsDiv != null)
					packsDiv.commit();
				if (!selectionModel.isValid()) {
					error("You must select at least one resource.");
				}
			}


			@Override
			public FormComponent< ? >[] getDependentFormComponents()
			{
				return null;
			}
		});
	}


	/**
	 * @return
	 */
	private WebMarkupContainer createUnvisibileDiv(String id)
	{
		WebMarkupContainer div = new WebMarkupContainer(id);
		div.setVisible(false);
		return div;
	}


	@Override
	public boolean isLastStep()
	{
		return false;
	}


	@Override
	public IDynamicWizardStep next()
	{
		return new ConfirmRONamesStep(this,
				(ImportModel) this.getDefaultModelObject(), selectionModel.createResearchObjects());
	}


	@Override
	public boolean isComplete()
	{
		return selectionModel.isValid();
	}

	private class ImportTypeChoiceRenderer
		implements IChoiceRenderer<ImportType>
	{

		private static final long serialVersionUID = 1558067857104879971L;


		@Override
		public Object getDisplayValue(ImportType type)
		{
			switch (type) {
				case ALL_AS_ONE:
					return "Import all selected resources as 1 Research Object";
				case EACH_SEPARATELY:
					return "Import each selected resource as a separate Research Object";
			}
			return null;
		}


		@Override
		public String getIdValue(ImportType type, int idx)
		{
			return type.toString();
		}

	}

}
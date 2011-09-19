/**
 * 
 */
package pl.psnc.dl.wf4ever.myexpimport.wizard;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.extensions.wizard.dynamic.IDynamicWizardStep;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.list.OddEvenListItem;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.validation.validator.PatternValidator;

import pl.psnc.dl.wf4ever.myexpimport.model.ImportModel;
import pl.psnc.dl.wf4ever.myexpimport.model.ResearchObject;

/**
 * @author Piotr Hołubowicz
 *
 */
public class ConfirmRONamesStep
	extends AbstractStep
{

	private static final long serialVersionUID = -3238571883021517707L;

	private boolean addMoreROs = true;

	private List<ResearchObject> researchObjectsEdited;


	@SuppressWarnings("serial")
	public ConfirmRONamesStep(IDynamicWizardStep previousStep,
			ImportModel model, List<ResearchObject> researchObjectsEdited)
	{
		super(previousStep, "Confirm names", model);

		this.researchObjectsEdited = researchObjectsEdited;

		final List<FormComponent<String>> fields = new ArrayList<FormComponent<String>>();
		final Form< ? > form = new Form<Void>("form");
		add(form);
		ListView<ResearchObject> list = new ListView<ResearchObject>(
				"resourceListView", researchObjectsEdited) {

			@Override
			protected ListItem<ResearchObject> newItem(int index,
					IModel<ResearchObject> itemModel)
			{
				return new OddEvenListItem<ResearchObject>(index, itemModel);
			};


			protected void populateItem(ListItem<ResearchObject> item)
			{
				ResearchObject ro = (ResearchObject) item.getModelObject();
				ro.setDefaultName();
				FormComponent<String> field = new RequiredTextField<String>(
						"name", new PropertyModel<String>(ro, "name"),
						String.class).add(new PatternValidator("[\\w]+"));
				fields.add(field);
				item.add(field);
				item.add(new Label("content", ro.getContentDesc()));
			}
		};
		list.setReuseItems(true);
		form.add(list);
		form.add(new RONamesValidator(fields, model));

		Form<ConfirmRONamesStep> formAddMore = new Form<ConfirmRONamesStep>(
				"formAddMore", new CompoundPropertyModel<ConfirmRONamesStep>(
						this));
		formAddMore.add(new CheckBox("addMoreROs"));
		add(formAddMore);
	}
	
	@Override
	protected void onBeforeRender()
	{
		super.onBeforeRender();
		ImportModel model = (ImportModel) this.getDefaultModelObject();
		model.getResearchObjects().removeAll(researchObjectsEdited);
	}


	@Override
	public void applyState()
	{
		super.applyState();
		ImportModel model = (ImportModel) this.getDefaultModelObject();
		model.getResearchObjects().addAll(researchObjectsEdited);
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
		if (addMoreROs) {
			return new SelectResourcesStep(this,
					(ImportModel) this.getDefaultModelObject());
		}
		else {
			return new ImportDataStep(this,
					(ImportModel) this.getDefaultModelObject());
		}
	}


	/**
	 * @return the addMoreROs
	 */
	public boolean isAddMoreROs()
	{
		return addMoreROs;
	}


	/**
	 * @param addMoreROs the addMoreROs to set
	 */
	public void setAddMoreROs(boolean addMoreROs)
	{
		this.addMoreROs = addMoreROs;
	}

}

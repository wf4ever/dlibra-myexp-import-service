/**
 * 
 */
package pl.psnc.dl.wf4ever.myexpimport.wizard;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.validation.AbstractFormValidator;

import pl.psnc.dl.wf4ever.myexpimport.model.ImportModel;
import pl.psnc.dl.wf4ever.myexpimport.model.ResearchObject;

/**
 * @author Piotr Hołubowicz
 *
 */
public class RONamesValidator
	extends AbstractFormValidator
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1337197226405912697L;

	private List<FormComponent<String>> components;

	private Map<String, Integer> names = new HashMap<String, Integer>();

	private Set<String> repeated = new HashSet<String>();

	private ImportModel importModel;


	public RONamesValidator(List<FormComponent<String>> components,
			ImportModel model)
	{
		this.components = components;
		this.importModel = model;
	}


	/* (non-Javadoc)
	 * @see org.apache.wicket.markup.html.form.validation.IFormValidator#getDependentFormComponents()
	 */
	@Override
	public FormComponent< ? >[] getDependentFormComponents()
	{
		return this.components.toArray(new FormComponent[components.size()]);
	}


	/* (non-Javadoc)
	 * @see org.apache.wicket.markup.html.form.validation.IFormValidator#validate(org.apache.wicket.markup.html.form.Form)
	 */
	@Override
	public void validate(Form< ? > form)
	{
		names.clear();
		repeated.clear();
		FormComponent<String> first = null;
		for (ResearchObject ro : importModel.getResearchObjects()) {
			names.put(ro.getName(), 1);
		}
		for (FormComponent<String> component : components) {
			String input = component.getInput();
			int cnt = names.containsKey(input) ? names.get(input) + 1 : 1;
			names.put(input, cnt);
			if (first == null && cnt > 1)
				first = component;

		}
		for (Map.Entry<String, Integer> entry : names.entrySet()) {
			if (entry.getValue() > 1) {
				repeated.add(entry.getKey());
			}
		}
		if (!repeated.isEmpty()) {
			Map<String, Object> variables = new HashMap<String, Object>();
			variables.put("names", StringUtils.join(repeated, ", "));
			error(first, variables);
		}
	}
}

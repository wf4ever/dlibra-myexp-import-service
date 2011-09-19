/**
 * 
 */
package pl.psnc.dl.wf4ever.myexpimport.wizard;

import java.io.Serializable;
import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Check;
import org.apache.wicket.markup.html.form.CheckGroup;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.list.OddEvenListItem;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.IModel;

import pl.psnc.dl.wf4ever.myexpimport.model.myexp.ResourceHeader;

/**
 * @author Piotr Hołubowicz
 *
 */
public class ResourceListPanel
	extends Panel
{

	private static final long serialVersionUID = -3775797988389365540L;

	private String resourceName;

	private Form< ? > form;


	@SuppressWarnings("serial")
	public ResourceListPanel(String id, String name,
			final List< ? extends ResourceHeader> resources,
			final List< ? extends ResourceHeader> selectedResources,
			final List< ? extends ResourceHeader> importedResources)
	{
		super(id);
		this.resourceName = name;
		final VisibilityModel visModel = new VisibilityModel(true);

		add(new Label("resourceName", resourceName));

		final WebMarkupContainer resourceList = new WebMarkupContainer(
				"resourceList");
		resourceList.setOutputMarkupId(true);
		resourceList
				.add(new AttributeModifier("style", new PropertyModel<String>(
						visModel, "resourceListDisplayStyle")));
		add(resourceList);

		form = new Form<Void>("form");
		resourceList.add(form);
		@SuppressWarnings("unchecked")
		CheckGroup<ResourceHeader> group = new CheckGroup<ResourceHeader>(
				"group", (List<ResourceHeader>) selectedResources);
		form.add(group);
		ListView<ResourceHeader> list = new ListView<ResourceHeader>(
				"resourceListView", resources) {

			@Override
			protected ListItem<ResourceHeader> newItem(int index,
					IModel<ResourceHeader> itemModel)
			{
				return new OddEvenListItem<ResourceHeader>(index, itemModel);
			};


			protected void populateItem(ListItem<ResourceHeader> item)
			{
				ResourceHeader resource = (ResourceHeader) item
						.getModelObject();
				Check<ResourceHeader> check = new Check<ResourceHeader>(
						"checkbox", item.getModel());
				item.add(check);
				String msg = importedResources.contains(resource) ? " <i>(already selected for import)</i>"
						: "";
				Label label = new Label("title", resource.getTitle() + msg);
				label.add(new AttributeModifier("for", new Model<String>(check
						.getMarkupId())));
				label.setEscapeModelStrings(false);
				item.add(label);
				item.add(new ExternalLink("link", resource.getResource()));
			}
		};
		list.setReuseItems(true);
		group.add(list);

		AjaxFallbackLink<String> link = new AjaxFallbackLink<String>(
				"resourceListLink") {

			@Override
			public void onClick(AjaxRequestTarget target)
			{
				visModel.setResourceListVisible(!visModel
						.isResourceListVisible());
				target.add(this);
				target.add(resourceList);
			}
		};
		link.add(new Label("resourceListLinkLabel", new PropertyModel<String>(
				visModel, "hideShowResourceListLabel")));
		add(link);

		add(new AjaxFallbackLink<String>("selectAll") {

			@SuppressWarnings("unchecked")
			@Override
			public void onClick(AjaxRequestTarget target)
			{
				((List<ResourceHeader>) selectedResources).addAll(resources);
				target.add(resourceList);
			}
		});

		add(new AjaxFallbackLink<String>("deselectAll") {

			@Override
			public void onClick(AjaxRequestTarget target)
			{
				selectedResources.clear();
				target.add(resourceList);
			}
		});
	}


	public void commit()
	{
		form.process(null);
	}

	class VisibilityModel
		implements Serializable
	{

		private static final long serialVersionUID = 6277636176664446588L;

		private boolean resourceListVisible;


		public VisibilityModel(boolean visible)
		{
			this.resourceListVisible = visible;
		}


		/**
		 * @return the resourceListVisible
		 */
		public boolean isResourceListVisible()
		{
			return resourceListVisible;
		}


		/**
		 * @param resourceListVisible the resourceListVisible to set
		 */
		public void setResourceListVisible(boolean resourceListVisible)
		{
			this.resourceListVisible = resourceListVisible;
		}


		public String getResourceListDisplayStyle()
		{
			if (isResourceListVisible()) {
				return "display:block";
			}
			else {
				return "display:none";
			}
		}


		public String getHideShowResourceListLabel()
		{
			if (isResourceListVisible()) {
				return "Hide";
			}
			else {
				return "Show";
			}
		}

	}

}

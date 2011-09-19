/**
 * 
 */
package pl.psnc.dl.wf4ever.myexpimport.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import pl.psnc.dl.wf4ever.myexpimport.model.myexp.FileHeader;
import pl.psnc.dl.wf4ever.myexpimport.model.myexp.PackHeader;
import pl.psnc.dl.wf4ever.myexpimport.model.myexp.WorkflowHeader;

/**
 * @author Piotr Hołubowicz
 *
 */
public class ResourceSelectionModel
	implements Serializable
{

	private static final long serialVersionUID = 5913502930927105275L;

	public enum ImportType {
		ALL_AS_ONE, EACH_SEPARATELY
	}

	private ImportType importType = ImportType.ALL_AS_ONE;

	private List<FileHeader> selectedFiles;

	private List<WorkflowHeader> selectedWorkflows;

	private List<PackHeader> selectedPacks;


	public ResourceSelectionModel()
	{
		this.selectedFiles = new ArrayList<FileHeader>();
		this.selectedWorkflows = new ArrayList<WorkflowHeader>();
		this.selectedPacks = new ArrayList<PackHeader>();
	}


	public List<FileHeader> getSelectedFiles()
	{
		return selectedFiles;
	}


	public List<WorkflowHeader> getSelectedWorkflows()
	{
		return selectedWorkflows;
	}


	public List<PackHeader> getSelectedPacks()
	{
		return selectedPacks;
	}


	public List<ResearchObject> createResearchObjects()
	{
		List<ResearchObject> ros = new ArrayList<ResearchObject>();
		if (importType == ImportType.ALL_AS_ONE) {
			ResearchObject ro = new ResearchObject();
			ro.setFiles(selectedFiles);
			ro.setWorkflows(selectedWorkflows);
			ro.setPacks(selectedPacks);
			ros.add(ro);
		}
		else {
			for (FileHeader file : selectedFiles) {
				ros.add(new ResearchObject(file));
			}
			for (WorkflowHeader workflow : selectedWorkflows) {
				ros.add(new ResearchObject(workflow));
			}
			for (PackHeader pack : selectedPacks) {
				ros.add(new ResearchObject(pack));
			}
		}
		return ros;
	}


	public boolean isValid()
	{
		return !selectedFiles.isEmpty() || !selectedWorkflows.isEmpty()
				|| !selectedPacks.isEmpty();
	}


	/**
	 * @return the importType
	 */
	public ImportType getImportType()
	{
		return importType;
	}


	/**
	 * @param importType the importType to set
	 */
	public void setImportType(ImportType importType)
	{
		this.importType = importType;
	}
}

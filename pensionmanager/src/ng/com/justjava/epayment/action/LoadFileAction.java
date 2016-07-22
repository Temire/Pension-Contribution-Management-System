package ng.com.justjava.epayment.action;

import javax.inject.*;

import org.openxava.actions.*;

public class LoadFileAction extends ViewBaseAction implements ILoadFileAction {

	@Inject
	private String newImageProperty;

	public void execute() throws Exception {
		showDialog();
	}

	public String[] getNextControllers() {
		return new String[] { "LoadFile" };
	}

	public String getCustomView() {
		return "xava/editors/uploadFile";
	}

	public boolean isLoadFile() {
		return true;
	}

	public String getNewImageProperty() {
		return newImageProperty;
	}

	public void setNewImageProperty(String string) {
		newImageProperty = string;
	}
		
}
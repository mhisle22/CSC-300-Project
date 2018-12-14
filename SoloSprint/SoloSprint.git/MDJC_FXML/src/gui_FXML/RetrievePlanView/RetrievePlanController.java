package gui_FXML.RetrievePlanView;

import gui_FXML.Model.ClientModel;
import gui_FXML.Model.TransitionModel;
import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;

public class RetrievePlanController {

    @FXML
    private ScrollPane scrollPane;
    
    @FXML
    private RadioButton clone;
    
    @FXML
    private RadioButton original;
    
	private ClientModel clientModel;
	private TransitionModel transitionModel;

	@FXML
	private boolean getCloneStatus()
	{
		return clone.isSelected();
	}
	
	@FXML
	private boolean getOriginalStatus()
	{
		return original.isSelected();
	}
	
	//Create buttons for each plan in department on server
	private void addButtons()
	{
		
		scrollPane.setContent(clientModel.getCurrentState().buttonHelper(getCloneStatus()));
	}
	
	public void setClientModel(ClientModel clientModel)
	{
		this.clientModel = clientModel;
		//Create plan buttons when clientModel is assigned
		addButtons();
	}

	public void setTransitionModel(TransitionModel transitionModel)
	{
		this.transitionModel = transitionModel;
	}

}
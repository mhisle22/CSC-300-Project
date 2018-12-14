package gui_FXML.PlanSelection;

import java.io.IOException;

import gui_FXML.MainWindow.MainWindowController;
import gui_FXML.Model.ClientModel;
import gui_FXML.Model.TransitionModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;
import mdjcBusinessPlanClasses.BusinessPlan;

public class PlanSelection
{
	@FXML
	private RadioButton cloneRadio;
	@FXML
	private RadioButton originalRadio;
	@FXML
	private ScrollPane scrollPane;
	@FXML
	private Button newButton;
	
	Stage stage;
	
	TransitionModel transitionModel;
	ClientModel clientModel;
	
	//model control
	public void setClientModel(ClientModel model)
	{
		this.clientModel = model;
		addButtons();
	}
	

	//called when this current window is made
	//sets up buttons to corresponding plans on server
	//the quarterly state controls which are shown
	private void addButtons()
	{
		scrollPane.setContent(clientModel.getCurrentState().buttonHelper(getCloneStatus()));
	}
	
	//called when button is pushed.
	//will make new business plan, then send you to the next window
	private void makeNewPlan()
	{
		//makes a new plan and sets it. Easy enough
		BusinessPlan businessPlan = new BusinessPlan();
		businessPlan.setQuarter(clientModel.getCurrentState().getName());
		clientModel.getClient().setLocalCopy(businessPlan);
		
		FXMLLoader loader = new FXMLLoader(MainWindowController.class.getResource("MainWindow.fxml"));
		try
		{
			//load the main window and set controllers 
			Scene scene = new Scene(loader.load());
			MainWindowController cont = loader.getController();
			cont.setTransitionModel(transitionModel);
			cont.setClientModel(clientModel);
			
			
			transitionModel.getStage().setScene(scene);
			transitionModel.getStage().show();
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
	}
	
	//check if clone radio is selected
	@FXML
	private boolean getCloneStatus()
	{
		return cloneRadio.isSelected();
	}
	
	//check if original radio is selected
	@FXML
	private boolean getOriginalStatus()
	{
		return originalRadio.isSelected();
	}
	
	//code for pressing new button
	@FXML
	public void newPressed(ActionEvent event)
	{
		makeNewPlan();
	}
	
	public void setTransitionModel(TransitionModel model)
	{
		this.transitionModel = model;
	}
	
	public ClientModel getClientModel()
	{
		return this.clientModel;
	}
	
	public TransitionModel getTransitionModel()
	{
		return this.transitionModel;
	}
	
	
}

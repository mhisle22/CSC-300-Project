package gui_FXML.Quarterly;

import java.io.IOException;

import gui_FXML.Model.ClientModel;
import gui_FXML.Model.TransitionModel;
import gui_FXML.PlanSelection.PlanSelection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;

public class QuarterlyController
{
	ClientModel model;
	private TransitionModel transitionModel;
	
	ObservableList<String> quarterList = FXCollections.observableArrayList("Yearly", "First", "Second", "Third", "Fourth", "All");
	
	@FXML
	private Button advanceButton;
	
	@FXML
	private ChoiceBox selectionBox;
	
	@FXML
	private void initialize()
	{
		selectionBox.setItems(quarterList);
	}
	
	@FXML
	void advancePressed(ActionEvent event)
	{
		String option = (String)selectionBox.getValue();
		
		// If successful, move to plan selection window (same as the back button)
		if(option != null)
		{
			if(option.equals("Yearly"))
			{
				model.setCurrentState(model.states[0]);
			}
			else if(option.equals("First"))
			{
				model.setCurrentState(model.states[1]);
			}
			else if(option.equals("Second"))
			{
				model.setCurrentState(model.states[2]);
			}
			else if(option.equals("Third"))
			{
				model.setCurrentState(model.states[3]);
			}
			else if(option.equals("Fourth"))
			{
				model.setCurrentState(model.states[4]);
			}
			else
			{
				model.setCurrentState(model.states[5]);
			}
			model.getCurrentState().setModel(model);
			model.getCurrentState().setTransitionModel(transitionModel);
			// This loads planSelection's fxml into a loader
			
			FXMLLoader loader = new FXMLLoader(PlanSelection.class.getResource("PlanSelection.fxml"));
			// Set the load of the loader to a new scene//
			Scene scene;
			try
			{
				scene = new Scene(loader.load());
				PlanSelection cont = loader.getController();
				cont.setTransitionModel(transitionModel);
				cont.setClientModel(model);
			
				// Set newly created scene to stage and show it
				transitionModel.getStage().setScene(scene);
				transitionModel.getStage().show();
			}
			catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
				
					
			}
			else
			{
				// Let the popUp classes know to show a popup giving a warning//
				transitionModel.setPopUpTitle("Selection Failed");
				transitionModel.setOKPopUpMessage("You did not make a selection.");
			}
	}
	
	// Getters and setters
	public void setClientModel(ClientModel model)
	{
		this.model = model;
	}
		
	public ClientModel getClientModel()
	{
		return model;
	}
		
	public void setTransitionModel(TransitionModel transitionModel)
	{	
		this.transitionModel = transitionModel;	
	}
		
	public TransitionModel getTransitionModel()
	{
		return transitionModel;
	}
}

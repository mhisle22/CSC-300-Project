package gui_FXML.popupA;

import java.io.IOException;

import gui_FXML.Model.TransitionModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class PopupA
{

	@FXML
	private Label labelA;
	@FXML
	private Button OkayButton;
	private static Stage thisStage;
	private static TransitionModel transitionModel;
	
	//sets the model, then make this popup listen for when its needed
	public static void setModel(TransitionModel model)
	{
		transitionModel = model;
		
		//CHANGE LISTENER EXAMPLE:
		//You can add a change listener to any of the BooleanProperties in the model
		//If any of the properties change, any attached change listener will react
		//newValue == current Boolean value
		//oldValue == Boolean value before change
		//Basically, if the needOKPopup property changes to true, run a method that sets the String attributes and show it
		model.OKPopUpMessageProperty().addListener((v, oldValue, newValue) -> {{showPopup();}});
		
		thisStage = new Stage();
		
		////This should be in its own method run by the change listener at a property change///
		//labelA.setText(model.getPopupText());
	}
	
	//since there is only an okay button, all this does is close. Yay.
	@FXML
	public void buttonPressed(ActionEvent event)
	{
		//this doesn't actually do anything. Its just there to make you feel better
		//close now
		thisStage.close();
	}
	
	//called when listener knows to make this show up
	private static void showPopup()
	{
		
		String message = transitionModel.getOKPopUpMessage();
		
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(PopupA.class.getResource("PopupA.fxml"));
		try 
		{
			Scene scene = new Scene(loader.load());
			PopupA cont = loader.getController();
			cont.changeLabel(message);
			//important thing to note is that this window is modal
			thisStage.initModality(Modality.APPLICATION_MODAL);
			thisStage.setScene(scene);
			//gets correct error message from model
			thisStage.setTitle(transitionModel.getPopUpTitle());
			
			thisStage.showAndWait();
		}
		catch (IOException e)
		{
			
			e.printStackTrace();
		}
		
		
		
	}
	
	private void changeLabel(String message)
	{
		labelA.setText(message);
	}
	
	public static Stage getStage()
	{
		return thisStage;
	}
	
	public static void setStage(Stage stage)
	{
		thisStage = stage;
	}

}

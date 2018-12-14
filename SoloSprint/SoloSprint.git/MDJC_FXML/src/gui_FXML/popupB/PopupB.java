package gui_FXML.popupB;

import java.io.IOException;

import gui_FXML.Model.ClientModel;
import gui_FXML.Model.TransitionModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class PopupB
{
	@FXML
	private Label labelB;
	@FXML
	private Button saveButton;
	@FXML
	private Button dontButton;
	@FXML
	private Button cancelButton;
	
	private static TransitionModel transitionModel;
	static Stage stage;
	private static ClientModel clientModel;
	
	public PopupB()
	{
		
	}
	
	//sets the model, then make this popup listen for when its needed
	public static void setTransitionModel(TransitionModel model)
	{
		transitionModel = model;
		
		model.SavePopUpMessageProperty().addListener((v, oldValue, newValue) -> {if(!newValue.isEmpty()) {showPopup();}});
		stage = new Stage();
		//important thing to note is that this window is modal
		stage.initModality(Modality.APPLICATION_MODAL);
	}
	
	public static void setClientModel(ClientModel model)
	{
		clientModel = model;
	}
	
	//popups will "return" a value to the model so that it may make the necessary changes
	//2
	@FXML
	public void savePressed(ActionEvent event)
	{
		//close now
		transitionModel.setPopupBReturnProperty(2);
		stage.close();
	}
	
	//3
	@FXML
	public void dontPressed(ActionEvent event)
	{
		//close now
		transitionModel.setPopupBReturnProperty(3);
		stage.close();
	}
	
	//4. 1 is for nerds.
	@FXML
	public void cancelPressed(ActionEvent event)
	{
		//close now
		transitionModel.setPopupBReturnProperty(4);
		stage.close();
	}
	
	//called when listener knows to make this show up
	private static void showPopup()
	{
		transitionModel.setPopUpClosed(false);
		
		
		//gets correct error message from model
		stage.setTitle(transitionModel.getPopUpTitle());
		
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(PopupB.class.getResource("PopupB.fxml"));
		try 
		{
			Scene scene = new Scene(loader.load());
			//gets correct error message from model
			String message = transitionModel.getSavePopUpMessage();
			PopupB cont = loader.getController();
			cont.changeLabel(message);
			
			
			stage.setScene(scene);
			stage.showAndWait();
		}
		catch (IOException e)
		{
			
			e.printStackTrace();
		}
	}
	
	
	public void changeLabel(String message)
	{
		labelB.textProperty().set(message);
	}

}

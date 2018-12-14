package gui_FXML.RecentChange;

import java.io.IOException;
import java.rmi.RemoteException;

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

public class RecentChange
{

	@FXML
	private Button okayButton;
	private static Stage thisStage;
	private static ClientModel clientModel;
	
	//sets the model, then make this popup listen for when its needed
	public static void setModel(ClientModel model)
	{
		clientModel = model;

		model.getClient().getLocalCopy().recentlyChangedProperty().addListener((v, oldValue, newValue) -> 
		{
			//if this change wasn't made by the current user
			if(!model.getClient().getLocalCopy().getUserChangee().equals(model.getClient().getUserToken()))
			{
					showPopup();
			} 

		});
		
		thisStage = new Stage();
	}
	
	//since there is only an okay button, all this does is close and change property.
	@FXML
	public void okayPressed(ActionEvent event)
	{
		//make sure it doesn't remake itself
		//close now
		clientModel.getClient().getLocalCopy().setRecentlyChanged(false);
		thisStage.close();
	}
	
	//called when listener knows to make this show up
	private static void showPopup()
	{
				
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(RecentChange.class.getResource("RecentChange.fxml"));
		try 
		{
			Scene scene = new Scene(loader.load());
			RecentChange cont = loader.getController();
			//important thing to note is that this window is modal
			thisStage.initModality(Modality.APPLICATION_MODAL);
			thisStage.setScene(scene);			
			thisStage.showAndWait();
		}
		catch (IOException e)
		{
			
			e.printStackTrace();
		}
			
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


package gui_FXML.ServerLoginView;
import java.io.IOException;

import clientServerClasses.ServerStartup;
import gui_FXML.Model.ClientModel;
import gui_FXML.Model.TransitionModel;
import gui_FXML.UserLoginView.UserLoginController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class ServerLoginController {

	private ClientModel model;
	private TransitionModel transitionModel;
	
	@FXML
	private Button cancelButton;
	
	@FXML
	private Button connectButton;
	
	@FXML
	private TextField servertextboxid;

	@FXML
	void serverCancel(ActionEvent event) 
	{
		transitionModel.closeStage();
		// To make sure server ends gracefully/safely and we don't lose data
		ServerStartup.unbindServer();
	}

	@FXML
	void serverConnect(ActionEvent event) 
	{

		String hostname = servertextboxid.getText();
		boolean boolinOut = model.getNewClient(hostname);
		
		// If successful (getClient returns boolean), move to next scene
		if(boolinOut)
		{
			
			// This loads user login's fxml into a loader
			FXMLLoader loader = new FXMLLoader(UserLoginController.class.getResource("UserLoginWindow.fxml"));
		
			// Set the load of the loader to a new scene//
			Scene scene;
			try
			{
				scene = new Scene(loader.load());
				// Set models and stage for controller (which you can get from the loader)//
				UserLoginController cont = loader.getController();
				cont.setTransitionModel(transitionModel);
				cont.setClientModel(model);
			
				// Set newly created scene to stage and show it//
				transitionModel.getStage().setScene(scene);
				transitionModel.getStage().show();
			}
			catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
			
			
		}
		

		// Else, do nothing
		
	}
	
	// Getters and setters
	public void setClientModel(ClientModel model)
	{
		
		this.model = model;
		
	}
	
	public void setTransitionModel(TransitionModel transModel)
	{
		
		this.transitionModel = transModel;
		
	}

	public ClientModel getClientModel()
	{
		return model;
	}

	public TransitionModel getTransitionModel()
	{
		return transitionModel;
	}
}


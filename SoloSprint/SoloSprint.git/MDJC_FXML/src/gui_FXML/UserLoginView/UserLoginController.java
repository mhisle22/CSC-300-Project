package gui_FXML.UserLoginView;
import java.io.IOException;

import clientServerClasses.ServerStartup;
import gui_FXML.Model.ClientModel;
import gui_FXML.Model.TransitionModel;
import gui_FXML.PlanSelection.PlanSelection;
import gui_FXML.Quarterly.QuarterlyController;
import gui_FXML.ServerLoginView.ServerLoginController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class UserLoginController {

	private ClientModel model;
	private TransitionModel transModel;
	
	@FXML
	private TextField usernameid;

	@FXML
	private PasswordField passwordid;
	
	@FXML
	private Label hostText;
	
	@FXML
	private Button cancelButton2;

	@FXML
	void userBack(ActionEvent event) {

		// Use FXMLLoader, then set the scene/controller (See PlanSelection on newPlan clicked for example)
		
		// This loads planSelection's fxml into a loader
		FXMLLoader loader = new FXMLLoader(QuarterlyController.class.getResource("Quarterly.fxml"));
	
		// Set the load of the loader to a new scene//
		Scene scene;
		try
		{
			scene = new Scene(loader.load());
			// Set models and stage for controller (which you can get from the loader)//
			ServerLoginController cont = loader.getController();
			cont.setTransitionModel(transModel);
			cont.setClientModel(model);

		
			// Set newly created scene to stage and show it//
			transModel.getStage().setScene(scene);
			transModel.getStage().show();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		
		
	}

	@FXML
	void userCancel(ActionEvent event) {

		transModel.getStage().close();
		ServerStartup.unbindServer();
		
	}

	@FXML
	void userConnect(ActionEvent event)
	{

		String userName = usernameid.getText();
		String password = passwordid.getText();
		boolean success = model.getClient().login(userName, password);
		
		// If successful, move to plan selection window (same as the back button)
		if(success)
		{
			// This loads planSelection's fxml into a loader
			FXMLLoader loader = new FXMLLoader(QuarterlyController.class.getResource("Quarterly.fxml"));
		
			// Set the load of the loader to a new scene//
			Scene scene;
			try
			{
				scene = new Scene(loader.load());
				QuarterlyController cont = loader.getController();
				cont.setTransitionModel(transModel);
				cont.setClientModel(model);
			
				// Set newly created scene to stage and show it
				transModel.getStage().setScene(scene);
				transModel.getStage().show();
			} catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
			
		}
		else
		{
			// Let the popUp classes know to show a popup giving a warning//
			transModel.setPopUpTitle("User Login Failed");
			transModel.setOKPopUpMessage("You have entered an invalid username and password combination");
		}
		
	}

	
	// Getters and setters
	public void setClientModel(ClientModel model)
	{
		
		this.model = model;
		hostText.setText("Connected to: " + model.getClient().getHost());
		
	}
	
	public ClientModel getClientModel()
	{
		return model;
	}
	
	public void setTransitionModel(TransitionModel transModel)
	{
		
		this.transModel = transModel;
		
	}
	
	public TransitionModel getTransitionModel()
	{
		return transModel;
	}
	
}

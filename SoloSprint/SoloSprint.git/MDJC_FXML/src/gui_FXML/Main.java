package gui_FXML;

import clientServerClasses.ServerStartup;
import gui_FXML.Model.ClientModel;
import gui_FXML.Model.TransitionModel;
import gui_FXML.ServerLoginView.ServerLoginController;
import gui_FXML.popupA.PopupA;
import gui_FXML.popupB.PopupB;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application
{
	@Override
	public void start(Stage primaryStage) 
	{
		try 
		{
			
			FXMLLoader loader = new FXMLLoader(ServerLoginController.class.getResource("ServerLoginWindow.fxml"));
			
			Scene scene = new Scene(loader.load());
			ServerLoginController cont = loader.getController();
			
			//Instantiates models and sets to server login controller
			TransitionModel transModel = new TransitionModel();
			transModel.setStage(primaryStage);
			cont.setClientModel(new ClientModel(transModel));
			cont.setTransitionModel(transModel);
			
			primaryStage.setScene(scene);
			
			//Start popups with models to begin listening
			PopupA.setModel(transModel);
			PopupB.setTransitionModel(transModel);
			
			primaryStage.show();
			ServerStartup.startLocalServer();
			
		} 
		catch(Exception e) 
		{
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) 
	{
		launch(args);
	}
}

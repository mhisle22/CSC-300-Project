package gui_FXML;

import clientServerClasses.ServerStartup;
import gui_FXML.MainWindow.MainWindowController;
import gui_FXML.Model.ClientModel;
import gui_FXML.Model.TransitionModel;
import gui_FXML.popupA.PopupA;
import gui_FXML.popupB.PopupB;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import mdjcBusinessPlanClasses.BusinessPlan;

public class TestMainWindow extends Application
{
	@Override
	public void start(Stage primaryStage) 
	{
		try {
			
			FXMLLoader loader = new FXMLLoader(MainWindowController.class.getResource("MainWindow.fxml"));
			
			Scene scene = new Scene(loader.load());
			MainWindowController cont = loader.getController();
			
			ServerStartup.startLocalServer();
			
			//Instantiates models and sets to server login controller
			TransitionModel transitionModel = new TransitionModel();
			transitionModel.setStage(primaryStage);
			ClientModel model = new ClientModel(transitionModel);
			model.getNewClient("localhost");
			model.getClient().login("admin", "CSC300");
			model.getClient().setLocalCopy(new BusinessPlan());
			
			cont.setTransitionModel(transitionModel);
			cont.setClientModel(model);
			
			primaryStage.setScene(scene);
			
			//Start popups with models to begin listening
			PopupA.setModel(transitionModel);
			PopupB.setTransitionModel(transitionModel);
			
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) 
	{
		launch(args);
	}
}

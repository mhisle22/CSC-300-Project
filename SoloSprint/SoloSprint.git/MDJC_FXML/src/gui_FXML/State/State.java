package gui_FXML.State;

import java.io.IOException;
import java.util.ArrayList;

import gui_FXML.MainWindow.MainWindowController;
import gui_FXML.Model.ClientModel;
import gui_FXML.Model.TransitionModel;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

public abstract class State
{
	TransitionModel transitionModel;
	ClientModel model;
	String name;
	
	public VBox buttonHelper(boolean isClone)
	{
		//adds buttons representing plans to a VBox, and returns the VBox
		ArrayList<Button> buttons = new ArrayList<Button>();
		VBox vbox = new VBox();
		String[][] plans = model.getClient().view();
		for(String[] plan : plans)
		{
			if(this.getName().equals(plan[3]))
			{
			String id = plan[0];
			String name = plan[1];
			String year = plan[2];
			Button button = new Button(id + ": " + name + ", " + year + ", " + plan[3]);
			button.setMinWidth(285);
			button.setMaxWidth(285);
			buttons.add(button);
			button.setOnAction(e -> {retrievePlan(id, isClone);});
			button.setId(id); //needed for testing 
			vbox.getChildren().add(button);
			button.setAlignment(Pos.CENTER);
			}
		}
		return vbox;
	}

	public State()
	{
	}
	
	public String getName()
	{
		return name;
	}
	public TransitionModel getTransitionModel()
	{
		return transitionModel;
	}

	public void setTransitionModel(TransitionModel transitionModel)
	{
		this.transitionModel = transitionModel;
	}

	public ClientModel getModel()
	{
		return model;
	}

	public void setModel(ClientModel model)
	{
		this.model = model;
	}
	
	
	//helper methods needed to make setButtons work
	//called when a current plan's button is pushed.
		//sends you to main window
		public void retrievePlan(String id, boolean isClone)
		{
			//retrieve the plan based on the button's id
			model.getClient().retrieve(id);
			if(isClone)
			{
				//copies everything other than year and id
				model.getClient().getLocalCopy().setYear("");
				model.getClient().getLocalCopy().setId("");
				model.getClient().getLocalCopy().setIsEditable(true);
			}
			
			FXMLLoader loader = new FXMLLoader(MainWindowController.class.getResource("MainWindow.fxml"));
			try
			{
				//load the main window and set controllers 
				Scene scene = new Scene(loader.load());
				MainWindowController cont = loader.getController();
				cont.setTransitionModel(transitionModel);
				cont.setClientModel(model);

				transitionModel.getStage().setScene(scene);
				transitionModel.getStage().show();
			} 
			catch (IOException e)
			{
				e.printStackTrace();
			}
			
		}
	
}

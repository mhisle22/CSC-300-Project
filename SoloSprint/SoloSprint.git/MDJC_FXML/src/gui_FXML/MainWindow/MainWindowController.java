package gui_FXML.MainWindow;

import java.io.IOException;

import clientServerClasses.Client;
import clientServerClasses.ServerStartup;
import gui_FXML.CategoryView.CategoryViewController;
import gui_FXML.InfoView.InfoViewController;
import gui_FXML.Model.ClientModel;
import gui_FXML.Model.TransitionModel;
import gui_FXML.PlanInfoView.PlanInfoController;
import gui_FXML.RecentChange.RecentChange;
import gui_FXML.StatementView.StatementViewController;
import gui_FXML.popupA.PopupA;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class MainWindowController 
{

	@FXML
    private BorderPane borderPane;
	
    private Stage stage;
    private ClientModel clientModel;
    private TransitionModel transitionModel;

	private StatementViewController statementViewCont;

	private InfoViewController infoViewCont;
    
	//Makes and displays the PlanInfoView (TOP) and InfoView (CENTER)
    private void makeCenterAndTop()
	{
		FXMLLoader loader = new FXMLLoader(PlanInfoController.class.getResource("PlanInfoView.fxml"));
		try
		{
			Node layout = (Node)loader.load();
			PlanInfoController cont = loader.getController();
			cont.setTransitionModel(transitionModel);
			cont.setClientModel(clientModel);
			
			borderPane.setTop(layout);
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		loader = new FXMLLoader(InfoViewController.class.getResource("InfoView.fxml"));
		try
		{
			Node layout = (Node)loader.load();
			infoViewCont = loader.getController();
			infoViewCont.setTransitionModel(transitionModel);
			infoViewCont.setClientModel(clientModel);
			
			
			borderPane.setCenter(layout);
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
		//set up recently changed popup now. It wont work otherwise
		//RecentChange.setModel(clientModel);
		transitionModel.getStage().show();
	}
    
    //Makes and displays the category tree on the right of the Main Window
	private void makeCategoryView()
	{
    	FXMLLoader loader = new FXMLLoader(CategoryViewController.class.getResource("CategoryView.fxml"));
		try
		{
			Node layout = (Node)loader.load();
			CategoryViewController cont = loader.getController();
			cont.setTransitionModel(transitionModel);
			cont.setClientModel(clientModel);
			
			borderPane.setRight(layout);
			transitionModel.getStage().show();
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
	}
	
	//Makes and displays the statement tree on the left of the Main Window
	private void makeStatementView()
	{
    	FXMLLoader loader = new FXMLLoader(StatementViewController.class.getResource("StatementView.fxml"));
		try
		{
			Node layout = (Node)loader.load();
			statementViewCont = loader.getController();
			statementViewCont.setTransitionModel(transitionModel);
			statementViewCont.setClientModel(clientModel);
			
			borderPane.setLeft(layout);
			transitionModel.getStage().show();
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	//Exit protocol for the program
	private void closeProgram(WindowEvent e)
	{
		//If the plan has not been recently saved, prompt the user to SAVE, DON'T SAVE, or CANCEL
    	if(!transitionModel.isRecentlySaved())
    	{
    		//Listen for user input from upcoming popup
    		transitionModel.popupBReturnProperty().addListener((v, oldValue, newValue)-> 
    		{
    			//IF SAVE, save plan and safely unbind local server
    			if(newValue.intValue() == 5) 
    			{
    				clientModel.getClient().save();
    				ServerStartup.unbindServer();
    			}
    			//If DON'T SAVE, safely unbind server
    			else if(newValue.intValue() == 3)
    			{
    				ServerStartup.unbindServer();
    			}
    			//If CANCEL, consume the exit request and do nothing
    			else if(newValue.intValue() == 4)
    			{
    				e.consume();
    			}
    		}
    		);
    		transitionModel.setPopUpTitle("Plan Not Submitted");
    		transitionModel.setSavePopUpMessage("This plan has not been submitted recently."
    				+ " Would you like to submit to the server?");
    		transitionModel.setPopupBReturnProperty(0);
    		
    	}
    	//If recently saved, safely unbind the local server and close the program
    	else
    	{
    		ServerStartup.unbindServer();
    	}
	}

    public void setClientModel(ClientModel model)
    {
    	this.clientModel = model;
    	
    	makeStatementView();
    	makeCategoryView();
    	makeCenterAndTop();
    }
    
    public void setTransitionModel(TransitionModel model)
    {
    	this.transitionModel = model;
    	
    	//Set window close request to run closeProgram()
    	transitionModel.getStage().setOnCloseRequest(e -> closeProgram(e));
    }
    
    public Client getClient()
    {
    	return clientModel.getClient();
    }

	public ClientModel getClientModel()
	{
		return clientModel;
	}

	public TransitionModel getTransitionModel()
	{
		return transitionModel;
	}

	public StatementViewController getStatementViewController()
	{
		return statementViewCont;
	}

	public InfoViewController getInfoViewController()
	{
		return infoViewCont;
	}

}

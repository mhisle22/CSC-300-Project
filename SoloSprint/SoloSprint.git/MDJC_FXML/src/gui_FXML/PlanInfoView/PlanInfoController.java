package gui_FXML.PlanInfoView;

import java.io.IOException;
import java.rmi.RemoteException;

import clientServerClasses.ServerStartup;
import gui_FXML.MainWindow.MainWindowController;
import gui_FXML.Model.ClientModel;
import gui_FXML.Model.TransitionModel;
import gui_FXML.popupB.PopupB;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import mdjcBusinessPlanClasses.BusinessPlan;

public class PlanInfoController{

    @FXML
    private Button newButton;
    
    @FXML
    private Label quarterLabel;

    @FXML
    private Button retrieveButton;

    @FXML
    private Button submitButton;

    @FXML
    private Button addUserButton;

    @FXML
    private Button exitButton;

    @FXML
    private TextField nameField;

    @FXML
    private TextField idField;

    @FXML
    private TextField yearField;

    @FXML
    private RadioButton canEdit;

    @FXML
    private RadioButton noEdit;

    private TransitionModel transitionModel;
    private ClientModel clientModel;
	private Stage stage;
    
    private void onLoadClientModel()
    {
    	if(!clientModel.getClient().isAdmin())
    	{
    		addUserButton.setDisable(true);
    		noEdit.setDisable(true);
    		canEdit.setDisable(true);
    	}
    	else
    	{
    		//Whenever focus changes off of the radio buttons, save their status
    		noEdit.focusedProperty().addListener((v, oldValue, newValue)-> 
    		{if(!newValue.booleanValue()) {clientModel.getBusinessPlan().setIsEditable(false);;}});
    		canEdit.focusedProperty().addListener((v, oldValue, newValue)-> 
    		{if(!newValue.booleanValue()) {clientModel.getBusinessPlan().setIsEditable(true);;}});
    	}
    	
    	nameField.setText(clientModel.getBusinessPlan().getName());
    	idField.setText(clientModel.getBusinessPlan().getId());
    	yearField.setText(clientModel.getBusinessPlan().getYear());
    	quarterLabel.setText(clientModel.getBusinessPlan().getQuarter());
    	
    	//If the plan is editable, set up text fields to allow edits to plan
    	if(clientModel.getBusinessPlan().isEditable())
    	{
    		canEdit.setSelected(true);
    		
    		//Whenever text fields lose focus, save their contents to the business plan
    		nameField.focusedProperty().addListener((v, oldValue, newValue)-> 
    		{if(!newValue.booleanValue()) {clientModel.getBusinessPlan().setName(nameField.getText());;}});
    		
    		idField.focusedProperty().addListener((v, oldValue, newValue)-> 
    		{if(!newValue.booleanValue()) {clientModel.getBusinessPlan().setId(idField.getText());}});
    		
    		yearField.focusedProperty().addListener((v, oldValue, newValue)-> 
    		{if(!newValue.booleanValue()) {clientModel.getBusinessPlan().setYear(yearField.getText());}});
    	}
    	//If the plan is not editable, set radio button to not editable
    	else
    	{
    		noEdit.setSelected(true);
    		//If the user is not an admin, also disable the text fields from editing
    		if(!clientModel.getClient().isAdmin())
    		{
    			nameField.setDisable(true);
    			idField.setDisable(true);
    			yearField.setDisable(true);
    		}
    	}
    }

	public void setTransitionModel(TransitionModel model)
    {
    	this.transitionModel = model;
    }

	public void setClientModel(ClientModel model)
    {
    	this.clientModel = model;
    	onLoadClientModel();
    }
    
    public void setStage(Stage stage)
	{
		this.stage = stage;	
	}
    
    //Requests the AddUserView to be shown
    @FXML
    void addUserPressed(ActionEvent event) 
    {
    	transitionModel.setSelectedTreeName("");
		transitionModel.setAddUserView(false);
    	//Let AddUserView know to show and take user input
    	transitionModel.setAddUserView(true);
    }
    
    //If Exit Program button is clicked, carry out correct program exit protocol
    //See closeProgram() in MainWindow
    @FXML
    void exitPressed(ActionEvent event) 
    {
    	transitionModel.setPopupBReturnProperty(0);
    	if(!transitionModel.isRecentlySaved())
    	{
    		transitionModel.popupBReturnProperty().addListener((v, oldValue, newValue)-> 
    		
    		{
    			if(transitionModel.getPopupBReturn() == 5) 
    			{
    			clientModel.getClient().save();
    			transitionModel.closeStage();
    			ServerStartup.unbindServer();
    			}
    			else if(transitionModel.getPopupBReturn() == 3)
    			{
    			transitionModel.getStage().close();
    			ServerStartup.unbindServer();}
    			}
    		);
    		transitionModel.setPopUpTitle("Plan Not Submitted");
    		transitionModel.setSavePopUpMessage("This plan has not been submitted recently."
    				+ " Would you like to submit to the server?");
    		
    	}
    	else
    	{
    		transitionModel.getStage().close();
    		ServerStartup.unbindServer();
    	}
    }
    //Requests SAVE/DON'T SAVE/CANCEL input from user, then pulls up new plan
    @FXML
    void newPlanPressed(ActionEvent event) 
    {
    	transitionModel.setPopupBReturnProperty(0);
    	if(!transitionModel.isRecentlySaved())
    	{
    		transitionModel.popupBReturnProperty().addListener((v, oldValue, newValue)-> 
    		
    		{
    			if(newValue.intValue() == 5) 
    			{if(dataValidate()) {clientModel.getClient().save();
    			clientModel.getClient().setLocalCopy(new BusinessPlan());loadMainWindow();}}
    			else if(newValue.intValue() == 3)
    			{clientModel.getClient().setLocalCopy(new BusinessPlan());loadMainWindow();}
    		}
    		);
    		
    		transitionModel.setPopUpTitle("Plan Not Submitted");
    		transitionModel.setSavePopUpMessage("This plan has not been submitted recently."
    				+ " Would you like to submit to the server?");
    	}
    	else
    	{
    		clientModel.getClient().setLocalCopy(new BusinessPlan());
    		loadMainWindow();
    	}

    }

    //Re-initializes the MainWindow (when new plan is requested)
    private void loadMainWindow()
	{
    	FXMLLoader loader = new FXMLLoader(MainWindowController.class.getResource("MainWindow.fxml"));
    	try
		{
			Scene scene = new Scene(loader.load());
			MainWindowController cont = loader.getController();
			cont.setTransitionModel(transitionModel);
			cont.setClientModel(clientModel);
			
			transitionModel.setSavePopUpMessage("");
			transitionModel.getStage().setScene(scene);
			transitionModel.getStage().show();
		} 
    	catch (IOException e)
		{
			e.printStackTrace();
		}
	}
    
    //Validate name, ID, and year fields before plan is saved (make sure they're not empty, year is a valid integer)
	private boolean dataValidate()
    {
    	if(!clientModel.getBusinessPlan().getName().isEmpty())
		{
			if(!clientModel.getBusinessPlan().getId().isEmpty())
			{
				if(!clientModel.getBusinessPlan().getYear().isEmpty())
				{
					try 
					{
						Integer.parseInt(clientModel.getBusinessPlan().getYear());
						return true;
					}
					catch(NumberFormatException e)
					{
						transitionModel.setPopUpTitle("Invalid Year Input");
						transitionModel.setOKPopUpMessage("Input for 'year' is not a valid integer."
								+ " Please enter a valid input for the business plan year."); 
						
						return false;
					}
					
				}
				else
				{
					transitionModel.setPopUpTitle("Empty Input Field");
					transitionModel.setOKPopUpMessage("The input field year is empty. Please input a year for this business plan before saving");
					
					return false;
				}
				
			}
			else
			{
				transitionModel.setPopUpTitle("Empty Input Field");
				transitionModel.setOKPopUpMessage("The input field ID is empty. Please input an ID for this business plan before saving");
				
				return false;
			}
		}
		else
		{
			transitionModel.setPopUpTitle("Empty Input Field");
			transitionModel.setOKPopUpMessage("The input field name is empty. Please input a name for this business plan before saving");
			
			return false;
		}
    }
    
	//Request RetrievePlanView be shown
    @FXML
    void retrievePlanPressed(ActionEvent event) 
    {
    	transitionModel.setRetrievePlan(true);
    }
    
    //If the text fields hold valid data, save plan to server
    @FXML
    void submitPlanPressed(ActionEvent event) 
    {
    	if(dataValidate())
    	{
    		clientModel.getClient().save();
    		//recently changed code. 
    		//sets property to true, since someone has changed now
			//clientModel.getClient().getLocalCopy().setRecentlyChanged(true);
			//clientModel.getClient().getLocalCopy().setUserChangee(clientModel.getClient().getUserToken());
    		transitionModel.setRecentlySaved(true);
    	}
    }

	public TransitionModel getTransitionModel()
	{
		return transitionModel;
	}

	//needed for testing
	public Label getQuarterLabel()
	{
		return quarterLabel;
	}


}

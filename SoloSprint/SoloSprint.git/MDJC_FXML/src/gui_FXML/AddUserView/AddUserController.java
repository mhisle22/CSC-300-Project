package gui_FXML.AddUserView;

import gui_FXML.Model.ClientModel;
import gui_FXML.Model.TransitionModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;

public class AddUserController {

    @FXML
    private TextField usernameField;

    @FXML
    private TextField passwordField;

    @FXML
    private TextField departmentField;

    @FXML
    private RadioButton isAdmin;

    @FXML
    private RadioButton notAdmin;

    @FXML
    private Button addUButton;

	private TransitionModel transitionModel;

	private ClientModel clientModel;

    public void setTransitionModel(TransitionModel model)
    {
    	this.transitionModel = model;
    }
    
    public void setClientModel(ClientModel model)
    {
    	this.clientModel = model;
    }
    
    //Add user to server based on given data in the necessary text fields
    @FXML
    void addUserClicked(ActionEvent event) 
    {
    	//If the text fields hold legitimate/valid inputs, attempt to add the new user
    	if(dataValidate())
    	{
    		boolean success = clientModel.getClient().addUser(usernameField.getText(), passwordField.getText(),
    				departmentField.getText(), isAdmin.isSelected());
    		if(!success)
    		{
    			transitionModel.setPopUpTitle("Username Already Exists");
    			transitionModel.setOKPopUpMessage("This username already exists. Please input a different unique username.");
    			
    		}
    		else
    		{
    			transitionModel.setAddUserView(false);
    		}
    	}
    }
    
  //Validate name, ID, and year fields before plan is saved (make sure they're not empty)
  	private boolean dataValidate()
      {
      	if(!passwordField.getText().trim().isEmpty())
  		{
  			if(!usernameField.getText().trim().isEmpty())
  			{
  				if(!departmentField.getText().trim().isEmpty())
  				{
  					return true;
  					
  				}
  				else
  				{
  					transitionModel.setPopUpTitle("Empty Input Field");
  					transitionModel.setOKPopUpMessage("The input field Department is empty."
  							+ " Please input a Department for this user before adding.");
  					return false;
  				}
  				
  			}
  			else
  			{
  				transitionModel.setPopUpTitle("Empty Input Field");
  				transitionModel.setOKPopUpMessage("The input field Username is empty. Please input a Username for this user before adding.");
  				return false;
  			}
  		}
  		else
  		{
  			transitionModel.setPopUpTitle("Empty Input Field");
  			transitionModel.setOKPopUpMessage("The input field Password is empty. Please input a Password for this user before adding.");
  			
  			return false;
  		}
      }

}

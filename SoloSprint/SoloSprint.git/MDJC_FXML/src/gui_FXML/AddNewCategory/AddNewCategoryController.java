package gui_FXML.AddNewCategory;
import gui_FXML.Model.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;


public class AddNewCategoryController
{
	    @FXML
	    private TextField catNameField;

	    @FXML
	    private TextField rankField;

	    @FXML
	    private TextField minField;

	    @FXML
	    private TextField maxField;
	    
	    @FXML
	    private Button addToPlanButton;

	    private ClientModel clientModel;
	    
	    private TransitionModel transitionModel;
	    
	    public void setClientModel(ClientModel model)
	    {
	    	this.clientModel = model;
	    }
	    
	    public void setTransitionModel(TransitionModel model)
	    {
	    	this.transitionModel = model;
	    }
	    
	    @FXML
	    void clickedAddCat(ActionEvent event) {
	    	    	
	    	String errorLocation = "";
	    	//Get the data the user provided
	    	String name = catNameField.getText();
	    	String rank = rankField.getText();
	    	String min = minField.getText();
	    	String max = maxField.getText();
			
			if(!min.trim().isEmpty())
			{
				try
				{
					int minInt = Integer.parseInt(min);
					if(!max.trim().isEmpty())
					{
						try
						{
							int maxInt = Integer.parseInt(max);
							if(!rank.trim().isEmpty())
							{
								try
								{
									int rankInt = Integer.parseInt(rank);
									if(name.equals("") != true)
									{
										boolean success = clientModel.getClient().getLocalCopy().addCategory(name, rankInt, minInt, maxInt);
										if(success)
										{
											//If the category was successfully added, set the new category list and make appropriate changes
											clientModel.getClient().getLocalCopy().setCategoryList();
											
											catNameField.clear();
											rankField.clear();
											minField.clear();
											maxField.clear();
											transitionModel.recentlySavedProperty().setValue(false);
											transitionModel.setRedrawTree(true);
										}
										else
										{
											//Warns the user that the name presented already exists
											transitionModel.setPopUpTitle("Existing Category");
											transitionModel.setOKPopUpMessage("There's already a category with this rank or name."
													+ " Please check the Category view and input a different name or rank.");
										}
									}
									else
									{
										//Warns the user that the name field is empty and needs to be filled
										errorLocation = "name";
										String emptyError = "The input field " + errorLocation + " is empty. Please input a " 
												+ errorLocation + " for this category before continuing";
										transitionModel.setPopUpTitle("Empty Name Field");
										transitionModel.setOKPopUpMessage(emptyError);
										
										
									}
								}
								catch(NumberFormatException e)
								{
									//Warns the user that the given rank was not a valid integer
									errorLocation = "Rank of Statement";
									String nonNumError = "The input for " + errorLocation + " is not a valid integer. Please"
											+ " input a valid integer for " + errorLocation + " before continuing";
									transitionModel.setPopUpTitle("Invalid Integer Input");
									transitionModel.setOKPopUpMessage(nonNumError);
									
								}
							}
							else
							{
								//Warns the uer that the rank input box is empty and needs to be filled
								errorLocation = "Rank of Statement";
								String emptyError = "The input field " + errorLocation + " is empty. Please input a " 
										+ errorLocation + " for this category before continuing";
								transitionModel.setPopUpTitle("Empty Rank Field");
								transitionModel.setOKPopUpMessage(emptyError);
								
							}
						}
						catch(NumberFormatException e)
						{
							//Warns the user that the maximum data is not a valid integer
							errorLocation = "Maximum Number of Statements";
							String nonNumError = "The input for " + errorLocation + " is not a valid integer. Please"
									+ "input a valid integer for " + errorLocation + " before continuing";
							transitionModel.setPopUpTitle("Invalid Integer Input");
							transitionModel.setOKPopUpMessage(nonNumError);
							
							
						}	
					}
					else
					{
						//Warns the user that the Maximum input is empty and needs to be filled
						errorLocation = "Maximum Number of Statements";
						String emptyError = "The input field " + errorLocation + " is empty. Please input a " 
								+ errorLocation + " for this category before continuing";
						transitionModel.setPopUpTitle("Emtpy Maximum Statements Field");
						transitionModel.setOKPopUpMessage(emptyError);
						
					}
				}
				catch(NumberFormatException e)
				{
					//Warns the user that what they've input for a minimum is not a valid integer
					errorLocation = "Minimum Number of Statements";
					String nonNumError = "The input for " + errorLocation + " is not a valid integer. Please"
							+ "input a valid integer for " + errorLocation + " before continuing";
					transitionModel.setPopUpTitle("Invalid Integer Input");
					transitionModel.setOKPopUpMessage(nonNumError);
					
				}
			}
			else
			{
				//Calls a PopUp that warns the user to add data to the minimum box
				errorLocation = "Minimum Number of Statements";
				String emptyError = "The input field " + errorLocation + " is empty. Please input a " 
						+ errorLocation + " for this category before continuing";
				transitionModel.setPopUpTitle("Empty Minimum Statements Field");
				transitionModel.setOKPopUpMessage(emptyError);
				
			}
	
			
					
		}
	    

}

	



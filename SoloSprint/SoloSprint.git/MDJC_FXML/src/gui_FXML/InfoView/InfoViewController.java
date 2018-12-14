package gui_FXML.InfoView;

import java.io.IOException;

import gui_FXML.AddNewCategory.AddNewCategoryController;
import gui_FXML.AddNewStatement.AddNewStatementController;
import gui_FXML.AddUserView.AddUserController;
import gui_FXML.CommentView.*;
import gui_FXML.Model.ClientModel;
import gui_FXML.Model.TransitionModel;
import gui_FXML.RetrievePlanView.RetrievePlanController;
import gui_FXML.StatementInfo.StatementInfoController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;

//Middle section of the GUI: doesn't inherently have anything interesting, but it handles the swapping of
//Other views into the middle section
public class InfoViewController
{
	
	private TransitionModel transitionModel;
    private ClientModel clientModel;
    //Used to swap out different InfoViews
    @FXML
    private BorderPane centerBorderPane;
    
    @FXML
    private Text emptyInfoText;
    
    private Node emptyView;
    
	private AddUserController addUserCont;
	private Parent addUserLayout;
	private Parent statementInfoLayout;
	private StatementInfoController statementInfoCont;
	private Parent addNewStatementLayout;
	private AddNewStatementController addNewStatementCont;
	private Parent addCategoryLayout;
	private AddNewCategoryController addNewCategoryCont;
	private Parent retrievePlanLayout;
	private RetrievePlanController retrievePlanCont;
	private CommentViewController commentViewCont;
	private Parent commentViewLayout;
	
	@FXML
	public void initialize()
	{
		//Assign variable to default empty view
	    emptyView = centerBorderPane.getCenter();
	    //Initialize all view layouts/controllers and assign to attributes
	    getStatementInfo();
	    getAddUserView();
	    getAddStatementView();
	    getAddCategoryView();
	    getRetrievePlanView();
	    getCommentsView();
	}
	
	public void setTransitionModel(TransitionModel model)
    {
    	this.transitionModel = model;
    	
    	//If the addCategoryView property is changed to true, display the AddCategoryView
    	transitionModel.addCategoryViewProperty().addListener((v, oldValue, newValue) -> 
    	{if(newValue.booleanValue()) {showAddCategoryView();}});
    	
    	//If the addStatementView property is changed to true, display the AddStatementView
    	transitionModel.addStatementViewProperty().addListener((v, oldValue, newValue) -> 
    	{if(newValue.booleanValue()) {showAddStatementView();}});
    	
    	//If the comments property is changed to true, display the AddStatementView
    	transitionModel.commentsViewProperty().addListener((v, oldValue, newValue) -> 
    	{if(newValue.booleanValue()) {showCommentsView();}else{showEmptyLayout();}});
    	
    	//If the selectedTreeName property is a non-empty string and not the root name, 
    	//display the StatementInfoView
    	//Else, show the default empty info view
    	transitionModel.selectedTreeNameProperty().addListener((v, oldValue, newValue) -> 
    	{if(!newValue.isEmpty() && !newValue.equals("Business Plan")) {showStatementInfo();}
    	else if(newValue.equals("Business Plan")){showEmptyLayout();}});
    	
    	//If the addUserView property is changed to true, display the AddUserView
    	//Else, show the default empty info view
    	transitionModel.addUserViewProperty().addListener((v, oldValue, newValue) -> 
    	{if(newValue.booleanValue()) {showAddUserView();}else{showEmptyLayout();}});
    	
    	//If the retrievePlan property is changed to true, display the RetrievePlanView
    	//Else, show the default empty info view
    	transitionModel.retrievePlanProperty().addListener((v, oldValue, newValue) ->
    	{if(newValue.booleanValue()) {showRetrievePlanView();}else{showEmptyLayout();}});
    	
    	//If the redrawTree property is changed to true, display the default empty info view and set the
    	//property to false
    	transitionModel.redrawTreeProperty().addListener((v, oldValue, newValue) ->
    	{if(newValue.booleanValue()) {showEmptyLayout();transitionModel.setRedrawTree(false);}});
    }
	
	//Display an empty info section when nothing is currently active
	public void showEmptyLayout()
	{
		centerBorderPane.setCenter(emptyView);
	}

	public void setClientModel(ClientModel model)
    {
    	this.clientModel = model;
    }
    
	//Initializes the StatementInfoView layout and controller
    private void getStatementInfo()
	{
    	FXMLLoader loader = new FXMLLoader(StatementInfoController.class.getResource("StatementInfoView.fxml"));
		try
		{
			statementInfoLayout = (Parent)loader.load();
			statementInfoCont = loader.getController();
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
    
    //Displays the StatementInfoView
    private void showStatementInfo()
    {
    	statementInfoCont.setClientModel(clientModel);
		statementInfoCont.setTransitionModel(transitionModel);
		centerBorderPane.setCenter(statementInfoLayout);
		transitionModel.getStage().show();
    }

    //Initializes the AddUserView layout and controller
	private void getAddUserView()
	{
		FXMLLoader loader = new FXMLLoader(AddUserController.class.getResource("AddUserView.fxml"));
		try
		{
			addUserLayout = (Parent)loader.load();
			addUserCont = loader.getController();
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
	}
	
	//Initializes the CommentsView layout and controller
	private void getCommentsView()
	{
		FXMLLoader loader = new FXMLLoader(CommentViewController.class.getResource("CommentView.fxml"));
		try
		{
			commentViewLayout = (Parent)loader.load();
			commentViewCont = loader.getController();
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
			
	}
	
	//Shows the CommentView
	private void showCommentsView()
	{
		commentViewCont.setClientModel(clientModel);
		commentViewCont.setTransitionModel(transitionModel);
		centerBorderPane.setCenter(commentViewLayout);
		transitionModel.getStage().show();
	}
	
	//Shows the AddUserView
	private void showAddUserView()
	{
		addUserCont.setClientModel(clientModel);
		addUserCont.setTransitionModel(transitionModel);
		centerBorderPane.setCenter(addUserLayout);
		transitionModel.getStage().show();
	}

	//Initializes the AddStatementView layout and controller
	private void getAddStatementView()
	{
		
		FXMLLoader loader = new FXMLLoader(AddNewStatementController.class.getResource("AddNewStatementView.fxml"));
		try
		{
			addNewStatementLayout = (Parent)loader.load();
			addNewStatementCont = loader.getController();
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	//Shows the AddStatementView
	private void showAddStatementView()
	{
		addNewStatementCont.setClientModel(clientModel);
		addNewStatementCont.setTransitionModel(transitionModel);
		centerBorderPane.setCenter(addNewStatementLayout);
		transitionModel.setAddStatementView(false);
		transitionModel.getStage().show();
	}
	
	//Initializes the AddCategoryView layout and controller
	private void getAddCategoryView()
	{
		
		FXMLLoader loader = new FXMLLoader(AddNewCategoryController.class.getResource("AddNewCategory.fxml"));
		try
		{
			addCategoryLayout = (Parent)loader.load();
			addNewCategoryCont = loader.getController();
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	//Shows the AddCategoryView
	private void showAddCategoryView()
	{
		addNewCategoryCont.setClientModel(clientModel);
		addNewCategoryCont.setTransitionModel(transitionModel);
		centerBorderPane.setCenter(addCategoryLayout);
		transitionModel.setAddCategoryView(false);
		transitionModel.getStage().show();
	}
	
	//Initializes the RetrievePlanView layout and controller
	private void getRetrievePlanView()
	{
		FXMLLoader loader = new FXMLLoader(RetrievePlanController.class.getResource("RetrievePlanView.fxml"));
		try
		{
			retrievePlanLayout = (Parent)loader.load();
			retrievePlanCont = loader.getController();
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	//Shows the RetrievePlanView
	private void showRetrievePlanView()
	{
		retrievePlanCont.setClientModel(clientModel);
		retrievePlanCont.setTransitionModel(transitionModel);
		centerBorderPane.setCenter(retrievePlanLayout);
		transitionModel.getStage().show();
	}
	
	//Gets the center of the InfoView layout
	public Node getCenter()
	{
		return centerBorderPane.getCenter();
	}
}

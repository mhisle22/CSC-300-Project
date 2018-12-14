package gui_FXML.Model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.stage.Stage;
import mdjcBusinessPlanClasses.Category;

//THIS IS SOLELY TO TRACK CHANGES BETWEEN VIEWS. By setting change listeners to these properties, one view
//can alert another to carry out a particular task (i.e. redraw the statement tree after a new statement
//was added
public class TransitionModel
{
	
	// Allow views to retrieve data set by other views
	private StringProperty selectedTreeName;
	private StringProperty addedStatementParent;
	private IntegerProperty addedStatementRank;
	private ObjectProperty<Category> addedCategory;
	private StringProperty OKPopUpMessage;
	private StringProperty SavePopUpMessage;
	private StringProperty popUpTitle;
	private IntegerProperty popupBReturn;
	private Stage stage;
	

	// Boolean properties for change listeners (THESE ARE OBSERVABLE)
	private BooleanProperty recentlySaved;
	private BooleanProperty addCategoryView;
	private BooleanProperty addStatementView;
	private BooleanProperty newPlan;
	private BooleanProperty retrievePlan;
	private BooleanProperty addUserView;
	private BooleanProperty redrawTree;
	private BooleanProperty popUpClosed;
	private BooleanProperty commentsView;

	public TransitionModel()
	{
		commentsView = new SimpleBooleanProperty(false);
		recentlySaved = new SimpleBooleanProperty(false);
		addedStatementParent = new SimpleStringProperty("");
		OKPopUpMessage = new SimpleStringProperty("");
		SavePopUpMessage = new SimpleStringProperty("");
		popUpTitle = new SimpleStringProperty("");
		redrawTree = new SimpleBooleanProperty(false);
		selectedTreeName = new SimpleStringProperty("");
		addedStatementRank = new SimpleIntegerProperty();
		addedCategory = new SimpleObjectProperty<Category>();
		addCategoryView = new SimpleBooleanProperty(false);
		addStatementView = new SimpleBooleanProperty(false);
		newPlan = new SimpleBooleanProperty(false);
		retrievePlan = new SimpleBooleanProperty(false);
		addUserView = new SimpleBooleanProperty(false);
		popUpClosed = new SimpleBooleanProperty(false);
		popupBReturn = new SimpleIntegerProperty(0);
	}
	
	
	////////***GETTERS/SETTERS***////////
	
	public BooleanProperty commentsViewProperty()
	{
		return commentsView;
	}

	public boolean getCommentsView()
	{
		return this.commentsView.getValue();
	}
	
	public void setCommentsView(boolean commentsView)
	{
		this.commentsView.setValue(commentsView);
	}


	public String getSavePopUpMessage()
	{
		return SavePopUpMessage.getValue();
	}
	
	public StringProperty SavePopUpMessageProperty()
	{
		return SavePopUpMessage;
	}
	
	public Stage getStage()
	{
		return stage;
	}
	
	public void setStage(Stage stage)
	{
		this.stage = stage;
	}

	public Boolean getRedrawTree()
	{
		return redrawTree.getValue();
	}
	
	public BooleanProperty redrawTreeProperty()
	{
		return redrawTree;
	}

	public void setRedrawTree(Boolean redrawTree)
	{
		this.redrawTree.setValue(redrawTree);
	}
	
	public IntegerProperty popupBReturnProperty()
	{
		return popupBReturn;
	}
	
	public int getPopupBReturn()
	{
		return popupBReturn.getValue();
	}

	public void setPopupBReturnProperty(int popupBReturn)
	{
		this.popupBReturn.set(popupBReturn);
	}
	
	public BooleanProperty popUpClosedProperty()
	{
		return popUpClosed;
	}
	
	public Boolean isPopUpClosed()
	{
		return popUpClosed.getValue();
	}


	public void setPopUpClosed(Boolean popUpClosed)
	{
		this.popUpClosed.setValue(popUpClosed);
	}

	public String getSelectedTreeName()
	{
		return selectedTreeName.getValue();
	}
	
	public StringProperty selectedTreeNameProperty()
	{
		return selectedTreeName;
	}

	public String getAddedStatementParent()
	{
		return addedStatementParent.getValue();
	}
	
	public StringProperty addedStatementParentProperty()
	{
		return addedStatementParent;
	}
	
	public int getAddedStatementRank()
	{
		return addedStatementRank.getValue();
	}
	
	public IntegerProperty addedStatementRankProperty()
	{
		return addedStatementRank;
	}

	public Category getAddedCategory()
	{
		return addedCategory.getValue();
	}

	public String getOKPopUpMessage()
	{
		return OKPopUpMessage.getValue();
	}
	
	public StringProperty OKPopUpMessageProperty()
	{
		return OKPopUpMessage;
	}

	
	public BooleanProperty recentlySavedProperty()
	{
		return recentlySaved;
	}

	public Boolean isRecentlySaved()
	{
		return recentlySaved.getValue();
	}

	public BooleanProperty addCategoryViewProperty()
	{
		return addCategoryView;
	}
	
	public Boolean isAddCategoryView()
	{
		return addCategoryView.getValue();
	}

	
	public BooleanProperty addStatementViewProperty()
	{
		return addStatementView;
	}
	
	public Boolean isAddStatementView()
	{
		return addStatementView.getValue();
	}
	
	public Boolean getNewPlan()
	{
		return newPlan.getValue();
	}
	
	public BooleanProperty newPlanProperty()
	{
		return newPlan;
	}
	
	public Boolean isNewPlan()
	{
		return newPlan.getValue();
	}
	
	
	public BooleanProperty retrievePlanProperty()
	{
		return retrievePlan;
	}

	public Boolean isRetrievePlan()
	{
		return retrievePlan.getValue();
	}
	
	
	public BooleanProperty addUserViewProperty()
	{
		return addUserView;
	}
	
	public Boolean isAddUserView()
	{
		return addUserView.getValue();
	}
	
	public String getPopUpTitle()
	{
		return popUpTitle.getValue();
	}

	public StringProperty popUpTitleProperty()
	{
		return popUpTitle;
	}
	
	
	public void setPopUpTitle(String popUpTitle)
	{
		this.popUpTitle.set(popUpTitle);
	}
	
	public void setSelectedTreeName(String selectedTreeName)
	{
		this.selectedTreeName.setValue(selectedTreeName);
	}

	public void setAddedStatementParent(String addedStatementParent)
	{
		this.addedStatementParent.setValue(addedStatementParent);
	}

	public void setAddedStatementRank(int addedStatementRank)
	{
		this.addedStatementRank.setValue(addedStatementRank);
	}
	
	public void setAddedCategory(Category addedCategory)
	{
		this.addedCategory.setValue(addedCategory);
	}

	public void setOKPopUpMessage(String popUpMessage)
	{
		this.OKPopUpMessage.setValue(popUpMessage);
	}

	public void setRecentlySaved(Boolean recentlySaved)
	{
		this.recentlySaved.setValue(recentlySaved);
	}

	public void setAddCategoryView(Boolean addCategoryView)
	{
		this.addCategoryView.setValue(addCategoryView);;
	}

	public void setAddStatementView(Boolean addStatementView)
	{
		this.addStatementView.setValue(addStatementView);
	}

	public void setNewPlan(Boolean newPlan)
	{
		this.newPlan.setValue(newPlan);
	}

	public void setRetrievePlan(Boolean retrievePlan)
	{
		this.retrievePlan.setValue(retrievePlan);
	}

	public void setAddUserView(Boolean addUserView)
	{
		this.addUserView.setValue(addUserView);
	}

	public void setSavePopUpMessage(String string)
	{
		SavePopUpMessage.setValue(string);
		
	}

	public void closeStage()
	{
		stage.close();
	}


}


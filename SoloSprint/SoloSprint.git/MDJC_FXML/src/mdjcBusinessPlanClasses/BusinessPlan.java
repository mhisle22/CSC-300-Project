package mdjcBusinessPlanClasses;
import java.util.*;

import javax.xml.bind.annotation.XmlRootElement;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

import java.io.*;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAccessType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class BusinessPlan implements Serializable{
	
	private static final long serialVersionUID = 5650597021644898758L;
	ArrayList<TreeBuilder> Histroy; // list of root nodes that contain copies of previous plans
	PlanDesign design;
	TreeBuilder tree;
	boolean isEditable = true;
	String year = "";
	String id = "";
	String name = "";
	String quarter = "";
	
	transient private BooleanProperty recentlyChanged;
	transient private String userChangee;
	
	
	public BusinessPlan(String id, String name, String year, boolean isEditable)
	{
		recentlyChanged = new SimpleBooleanProperty(false);
		this.Histroy = new ArrayList<TreeBuilder>();
		this.tree = new TreeBuilder();
		this.design = new PlanDesign();
		this.year = year;
		this.id = id;
		this.name = name;
		this.isEditable = isEditable;
	}
	
	public BusinessPlan()
	{
		recentlyChanged = new SimpleBooleanProperty(false);
		this.Histroy = new ArrayList<TreeBuilder>();
		this.tree = new TreeBuilder();
		this.design = new PlanDesign();
	}
   
	public TreeBuilder getTree()
	{
		return tree;
	}
	
   //must be called before adding anything to the tree
   public void setCategoryList()
   {
      this.tree.setCategoriesList(this.design.getCategoryList());
   }
	
   //this is how we save business plans to an array list
	public void addToHistory() 
	{
      Histroy.add(tree);
	}

	public void setPlan(PlanDesign plan) 
	{
      this.design = plan;
	}
	
   //this dynamically adds statements to our statement tree 
	public boolean addStatement(String data, String categoryName, Statement parent, String name)
   {
      int categoryMin;
      int categoryMax;
      int rank = 0;
      int parentRank = 0;
      Statement statement = new Statement();
      Category category = new Category();
      boolean success = true;
     //this gets the min, max, and category object of the statement's category
      for(int i = 0; i<this.design.getCategoryList().size(); i++)
      {
         if(parent.getType() != null && this.design.getCategoryList().get(i).getName().equals(parent.getType().name))
         {
        	 parentRank = this.design.getCategoryList().get(i).getRank();
         }
         if(this.design.getCategoryList().get(i).name.equals(categoryName))
         {
            categoryMin = this.design.getCategoryList().get(i).getMin();
            categoryMax = this.design.getCategoryList().get(i).getMax();
            category = this.design.getCategoryList().get(i);
            rank = this.design.getCategoryList().get(i).getRank();
         }         
      }
      //number of statements in specified category increases by 1
      category.incrementStatement();
      //only lets you add the statement if the #statements in that category isn't already full
      if((category.max>=category.statementCount) && ((parentRank + 1 == rank)))
      {
         statement = this.tree.addStatement(data, categoryName, parent, name);
         return success;
      }
      else
      {
         //if we don't add the statement, we have to decrement the count 
         category.decrementStatement();
         success = false;
      }
      return success;       
   }
	
   public boolean removeStatement(Statement statement)
   {
	   Category category = new Category();
	   for(int i = 0; i<this.design.getCategoryList().size(); i++)
	      {
	         if(this.design.getCategoryList().get(i).name.equals(statement.getType().getName()))
	         {
	        	 category = this.design.getCategoryList().get(i);
	         }         
	      }
      //only allows us to remove a statement if the number of statements in that category < min 
      if(category.getStatementCount()>category.getMin())
      {
         this.tree.removeStatement(statement);
         category.decrementStatement();
         return true;
      }
      else
      {
         return false;
      }
      
   }
   
   //changes the data in the given statement
   public void editStatement(Statement statement, String data)
   {
      this.tree.editStatement(statement, data);
   }
   
   //adds category to PlanDesign object (PlanInterface object)
   public boolean addCategory(String name, int pos, int min, int max)
   {
      return this.design.addCategory(name, pos, min, max);
   }

   public boolean isEditable()
   {
	   return isEditable;
   }
   
   public void setIsEditable(boolean isEditable)
	{
		this.isEditable = isEditable;
	}

   public String getYear()
   {
	   return year;
   }
   
   public void setYear(String year)
   {
	   this.year = year;
   }
   
   public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}
	
	public PlanDesign getPlan()
	{
		return design;
	}
	
   //Resets doubly-linked statements after being read from XML (JAXB does NOT like doubly-linked objects)
   public void renewStatementRefs() 
	{
	   	if(this.tree != null)
	   	{
	   		this.tree.getRoot().resetParentAfterRead();
	   	}
		for(int i = 0; i < Histroy.size(); i++)
		{
			TreeBuilder currTree = Histroy.get(i);
			currTree.getRoot().resetParentAfterRead();
		}
	}
   
   
 //used for recently changed popups
 	public String getUserChangee()
 	{
 		return this.userChangee;
 	}
 	
 	public void setUserChangee(String userChangee)
 	{
 		this.userChangee = userChangee;
 	}
 	
 	public BooleanProperty recentlyChangedProperty()
 	{
 		return recentlyChanged;
 	}

 	public boolean getRecentlyChanged()
 	{
 		return this.recentlyChanged.getValue();
 	}
 	
 	public void setRecentlyChanged(boolean recentlyChanged)
 	{
 		this.recentlyChanged.setValue(recentlyChanged);
 	}

	public String getQuarter()
	{
		return quarter;
	}

	public void setQuarter(String quarter)
	{
		this.quarter = quarter;
	}
 	
}


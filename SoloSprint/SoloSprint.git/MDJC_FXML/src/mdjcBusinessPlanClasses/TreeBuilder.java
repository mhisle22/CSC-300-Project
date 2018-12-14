package mdjcBusinessPlanClasses;
import java.io.Serializable;

import java.util.*;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlAccessType;

@XmlAccessorType(XmlAccessType.FIELD)
public class TreeBuilder implements Serializable
{
	private static final long serialVersionUID = 3071338868387233771L;
	@XmlElementWrapper(name = "categories")
	@XmlElement(name = "category")
	ArrayList<Category> categories;
   //the root is how Organization accesses the tree
	@XmlElement(name = "root")
	Statement root = new Statement();
   public TreeBuilder()
   {
      this.categories = categories;
      this.root.setData("root");
      this.root.setName("root");
      //Root has no parent (null)
      this.getRoot().setParent(null);
   }
   public Statement addStatement(String data, String categoryName, Statement parent, String name)
   {  
      Statement statement = new Statement();
      //set statement's category
      for(Category c : this.categories)
      {
         if(categoryName.equals(c.name))
         {
            statement.setType(c);
         }
      }
      //set statement's data
      statement.setData(data);
      statement.setName(name);
      if(parent!=null)
      {
    	  boolean exists = false;
    	 for(int i = 0; i < parent.getChildren().size(); i++)
    	 {
    		 if(parent.getChildren().get(i).getName().equals(name))
    		 {
    			 exists = true;
    		 }
    	 }
    	 if(!exists)
    	 {
    		 statement.setParent(parent);
    		 parent.addChild(statement);
    	 }
    	 else
    	 {
    		 statement = null;
    	 }
      }
      
      return statement;
   }
   //calls terminate() function in Statement class
   public void removeStatement(Statement statement)
   {
      statement.terminate();
   }
   public void editStatement(Statement statement, String data)
   {
      statement.setData(data);
   }
   public void setCategoriesList(ArrayList<Category> categories)
   {
      this.categories = categories;
   }
   
   public Statement getRoot()
   {
      return this.root;
   }
   
   public Statement findStatement(ArrayList<Statement> searchedList, String name)
   {
		for(int i = 0; i < searchedList.size(); i++)
		{
			Statement c = findStatement(searchedList.get(i).getChildren(), name); //Call findStatement to search children/grandchildren/etc.
			if (c != null) //If findStatement returns an actual statement, it's what we want! Continue returning it
			{
				return c;
			}
			else if(searchedList.size() == 0)
			{
				return null; //Bottom of tree and component still not found, return to last method call
			}
			else if(searchedList.get(i).getName().equals(name)) //see if this item is our target component
			{
				return searchedList.get(i); //return if target component
			}
		}
		return null; //List is correct type and size > 0, but target component was still not found. Return to last method call to continue searching
	}
   
   public Category findCategory(String name)
   {
	   for(Category c: categories)
	   {
		   if(c.getName().equals(name))
		   {
			   return c;
		   }
	   }
	   return null; //not found. :(
   }
}

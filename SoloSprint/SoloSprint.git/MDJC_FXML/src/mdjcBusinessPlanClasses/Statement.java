package mdjcBusinessPlanClasses;
import java.util.*;

import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAccessType;

@XmlAccessorType(XmlAccessType.FIELD)
public class Statement implements Commentable, java.io.Serializable{

	private static final long serialVersionUID = 8922619098294069959L;
	Category type;
	@XmlTransient
	Statement parent;
	ArrayList<Statement> children;
	String data;
	String name;
	ArrayList<String> comments;
	
	public Statement() 
	{
		this.children = new ArrayList<Statement>();
		this.comments = new ArrayList<String>();
	}
   
	public Category getType() {
		return this.type;
	}
	public Statement getParent() {
		return this.parent;
	}
	
	public ArrayList<Statement> getChildren()
	{
		return this.children;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public String getName()
	{
		return name;
	}
	
	public String getData() {
		return this.data;
	}
	public void setType(Category cat) {
		this.type = cat;
		return;
	}
	public void setParent(Statement par) {
		this.parent = par;
		return;
	}
	public void setData(String data) 
	{
		this.data = data;
	}
	
   //adds statement to current statement's children list
	public void addChild(Statement chi) {
		this.children.add(chi);
		return;
	}
   
   //sets the current statement's data members to null
   //also sets all other statements below the current statement to null 
   public void terminate()
   {
      for(int i = 0; i < parent.getChildren().size(); i++)
      {
    	  if(parent.getChildren().get(i).getName().equals(name))
    	  {
    		  parent.getChildren().remove(i);
    	  }
      }
      
   }
   
   public void resetParentAfterRead()
   {
	   for(int i = 0; i < children.size(); i++)
	   {
		   children.get(i).setParent(this);
	   }
   }
   
   public ArrayList<String> getComments()
   {
	   return comments;
   }

   public void setComments(ArrayList<String> comments)
   {
	   this.comments = comments;
   }

}

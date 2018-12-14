package mdjcBusinessPlanClasses;

import javax.xml.bind.annotation.XmlAccessorType;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;

@XmlAccessorType(XmlAccessType.FIELD)
public class Category implements Commentable, java.io.Serializable {
	
	private static final long serialVersionUID = 5235697394687146369L;
	//minimum and maximum number of statements allowed for the category
	int min; 
	int max;
	String name;
   //the rank indicates which level in the tree that the category falls under
	int rank;
   //statementCount is the number of statements that currently exist under this category
   //this allows us to send error messages when we exceed the max # of statements 
   int statementCount;
   ArrayList<String> comments;
	
	public Category()
	{
		this.statementCount = 0;
		this.comments = new ArrayList<String>();
	}
	
	public int getMin() {
		return this.min;
	}
   public int getStatementCount()
   {
      return this.statementCount;
   }
	public int getMax() {
		return this.max;
	}
	public String getName() {
		return this.name;
	}
	public int getRank() {
		return this.rank;
	}
	public void setMin(int Min) {
		this.min = Min;
		return;
	}
   //increase the statementCount by 1
   public void incrementStatement()
   {
      this.statementCount++; 
   }
   //decrease the statementCount by 1
   public void decrementStatement()
   {
      this.statementCount--; 
   }
	public void setMax(int Max) {
		this.max = Max;
		return;
	}
	public void setName(String Name) {
		this.name = Name;
		return;
	}
	public void setRank(int Rank) {
		this.rank = Rank;
		return;
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

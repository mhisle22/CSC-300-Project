package mdjcBusinessPlanClasses;
import java.io.Serializable;
import java.util.*;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlAccessType;

@XmlAccessorType(XmlAccessType.FIELD)
public class PlanDesign implements Serializable{

	private static final long serialVersionUID = -741167926963449578L;
	@XmlElementWrapper(name = "categories")
	@XmlElement(name = "category")
	ArrayList<Category> categoryList;
   
	public PlanDesign() 
	{
		this.categoryList = new ArrayList<Category>();
	}
   
   //set category name, rank/position, min required, and max allowed
	public boolean addCategory(String name, int pos, int min, int max) 
	{
		boolean exists = false;
		for(int i = 0; i < categoryList.size(); i++)
		{
			if((categoryList.get(i).getRank() == pos) || (categoryList.get(i).getName().equals(name)))
			{
				exists = true;
			}
		}
		if(!exists)
		{
			Category c = new Category();
			c.setName(name);
			c.setMin(min);
			c.setMax(max);
			c.setRank(pos);
			this.categoryList.add(c);
			orderCategories();
			return true;
		}
		else
		{
			return false;
		}
	}
   //orders categories from top ranking (lowest actual integer) to lowest ranking (biggest integer)
	public void orderCategories() {
		int minIndex;
		for(int i=0; i < this.categoryList.size(); i++)
		{
			minIndex = i;
			for(int j = i + 1; j < this.categoryList.size(); j++) 
			{
				if(this.categoryList.get(j).rank < this.categoryList.get(minIndex).rank) 
				{
					minIndex = j;
				}
			}
			this.swap(i,minIndex);
		}
	}
	
   //helper function for the sort algorithm in orderCategories()
	public void swap(int j, int min) {
		Category temp = this.categoryList.get(j);
		this.categoryList.set(j, this.categoryList.get(min));
		this.categoryList.set(min, temp);
	}
	
   public ArrayList<Category> getCategoryList()
   {
      return this.categoryList;
   }

}
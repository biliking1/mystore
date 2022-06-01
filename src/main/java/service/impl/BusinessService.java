package service.impl;

import java.util.List;

import domain.Category;

public interface BusinessService {

	
	void addCategory(Category category);

	
	Category findCategory(String id);


	List<Category> getAllCategory();

}
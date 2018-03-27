package com.zv.bookstore.category.service;

import java.util.List;

import com.zv.bookstore.book.dao.BookDao;
import com.zv.bookstore.category.dao.CategoryDao;
import com.zv.bookstore.category.domain.Category;

public class CategoryService {
	private CategoryDao categoryDao=new CategoryDao();
	private BookDao bookDao=new BookDao(); 

	/**
	 * 查询所有分类
	 * @return
	 */
	public List<Category> findAll() {
		return categoryDao.findAll();
	}

	/**
	 * 添加分类
	 * @param category
	 * @throws CategoryException 
	 */
	public void add(Category category) throws CategoryException {
		/*
		 * 1.校验分类名称是否存在
		 * 	存在：抛出异常
		 * 	不存在：执行添加分类
		 */
		Category _category=categoryDao.findByCname(category.getCname());
		if(_category!=null) throw new CategoryException("分类已经存在");
		
		categoryDao.add(category);
		
	}

	/**
	 * 删除分类
	 * @param cid
	 * @throws CategoryException 
	 */
	public void delete(String cid) throws CategoryException {
		//获取该分类下图书的本数
		int count=bookDao.getCountByCid(cid);
		//分类下有图书
		if(count>0) throw new CategoryException("该分类下还有图书");
		//删除该分类
		categoryDao.delete(cid);
	}

	/**
	 * 通过cid查找分类
	 * @param cid
	 * @return
	 */
	public Category findByCid(String cid) {
		
		return categoryDao.findByCid(cid);
	}

	/**
	 * 修改分类
	 * @param category
	 * @throws CategoryException 
	 */
	public void edit(Category category) throws CategoryException {
		/*
		 * 1.校验分类名称是否存在
		 * 	存在：抛出异常
		 * 	不存在：执行修改分类
		 */
		Category _category=categoryDao.findByCname(category.getCname());
		if(_category!=null) throw new CategoryException("分类名称已经存在");
		
		categoryDao.edit(category);
		
	}

}

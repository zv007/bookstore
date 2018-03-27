package com.zv.bookstore.book.service;

import java.util.List;

import com.zv.bookstore.book.dao.BookDao;
import com.zv.bookstore.book.domain.Book;

public class BookService {
	private BookDao bookDao=new BookDao();
	
	/**
	 * 查询所有图书
	 * @return
	 */
	public List<Book> findAll(){
		return bookDao.findAll();
	}

	/**
	 * 按分类查询
	 * @param cid
	 * @return
	 */
	public List<Book> findByCategory(String cid) {
		return bookDao.findByCategory(cid);
	}

	/**
	 * 加载图书
	 * @param bid
	 * @return
	 */
	public Book load(String bid) {
		return bookDao.load(bid);
	}

	/**
	 * 添加图书
	 * @param book
	 */
	public void add(Book book) {
		bookDao.add(book);
	}
	
	/**
	 * 删除图书
	 */
	public void delete(String bid){
		bookDao.delete(bid);
	}

	public void edit(Book book) {
		bookDao.edit(book);
		
	}

}

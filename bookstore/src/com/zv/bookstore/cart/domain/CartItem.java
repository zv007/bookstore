package com.zv.bookstore.cart.domain;

import java.math.BigDecimal;

import com.zv.bookstore.book.domain.Book;

/**
 * 购物车条目类
 * @author zv
 *
 */
public class CartItem {
	private Book book;
	private int count;
	
	/**
	 * 小计方法
	 * @return
	 * 处理了二进制运算误差问题
	 */
	public double getSubtatal(){//小计方法，但没有对应的成员
		BigDecimal d1=new BigDecimal(book.getPrice()+"");
		BigDecimal d2=new BigDecimal(count+"");
		return d1.multiply(d2).doubleValue();
	}
	
	
	public Book getBook() {
		return book;
	}
	public void setBook(Book book) {
		this.book = book;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	
}

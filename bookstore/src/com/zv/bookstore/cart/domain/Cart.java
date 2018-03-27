package com.zv.bookstore.cart.domain;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 购物车类
 * @author zv
 *
 */
public class Cart {
	private Map<String, CartItem> map=new LinkedHashMap<String, CartItem>();
	
	/**
	 * 计算合计（所有条目的小计之和）
	 * @return
	 */
	public double getTotal(){
		BigDecimal total=new BigDecimal(0+"");
		for(CartItem cartItem : map.values()){
			BigDecimal subtotal=new BigDecimal(cartItem.getSubtatal()+"");
			total=total.add(subtotal);
		}
		return total.doubleValue();
	}
	
	/**
	 * 添加条目到车中
	 * @param cartItem
	 */
	public void add(CartItem cartItem){
		String bid=cartItem.getBook().getBid();
		if(map.containsKey(bid)){//判断原来车中是否存在该条目
			CartItem oldItem=map.get(bid);//返回原条目
			oldItem.setCount(oldItem.getCount() + cartItem.getCount());//原条目新条目数量相加，然后在付给原条目
			map.put(bid,oldItem);
		}else{
			map.put(bid,cartItem);
		}
	}
	
	/**
	 * 清空所用条目
	 */
	public void clear(){
		map.clear();
	}
	
	/**
	 * 删除指定条目
	 * @param bid
	 */
	public void delete(String bid){
		map.remove(bid);
	}
	
	/**
	 * 获取所有条目
	 * @return
	 */
	public Collection<CartItem> getCartItems(){
		
		return map.values();
	}

}

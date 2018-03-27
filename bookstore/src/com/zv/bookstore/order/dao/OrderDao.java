package com.zv.bookstore.order.dao;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import com.zv.bookstore.book.domain.Book;
import com.zv.bookstore.order.domain.Order;
import com.zv.bookstore.order.domain.OrderItem;

import cn.itcast.commons.CommonUtils;
import cn.itcast.jdbc.TxQueryRunner;

public class OrderDao {
	private QueryRunner qr=new TxQueryRunner();
	
	/**
	 * 添加订单
	 * @param order
	 */
	public void addOrder(Order order){
		try{
			String sql="insert into orders values(?,?,?,?,?,?)";
			/*
			 * 处理util的date转换成sql的timestamp
			 */
			Timestamp timestamp=new Timestamp(order.getOrdertime().getTime());
			Object[] params={order.getOid(),timestamp,order.getTotal(),
					order.getState(),order.getOwner().getUid(),
					order.getAddress()};
			qr.update(sql, params);
		}catch(SQLException e){
			throw new RuntimeException(e);
		}	
	}
	
	/**
	 * 插入订单条目
	 * （批处理）
	 * @param orderItemList
	 */
	public void addOrderItemList(List<OrderItem> orderItemList){
		/*
		 * QueryRunner类的batch(String sql,Object[][] params)
		 * 其中params是多个一维数组
		 * 每个一维数组都与sql在一起执行一次，多个一位数据就执行多次
		 */
		
		try{
			String sql="insert into orderitem values(?,?,?,?,?)";
			/*
			 * 把orderItemList转换成两位数组
			 * 	把一个OrderItem对象转换成一个一维数组
			 */
			Object[][] params=new Object[orderItemList.size()][];
			//循环遍历orderItemList，使用每个orderItem对象为params每个一维数组赋值
			for(int i=0; i<orderItemList.size(); i++){
				OrderItem item=orderItemList.get(i);
				params[i]=new Object[]{item.getIid(),item.getCount(),
						item.getSubtotal(),item.getOrder().getOid(),
						item.getBook().getBid()};
			}
			qr.batch(sql, params);//执行批处理
		}catch(SQLException e){
			throw new RuntimeException(e);
		}
	}

	/**
	 * 按uid查询订单
	 * @param uid
	 * @return
	 */
	public List<Order> findByUid(String uid) {
		/*
		 * 1.通过UID查询出当前用户的所有List<Order>
		 * 2.循环遍历每个Order，为其加载它所有的OrderItem
		 */
		try{
			/*
			 * 1.得到当前用户的所有订单
			 */
			String sql="select * from orders where uid=?";
			List<Order> orderList=qr.query(sql, new BeanListHandler<Order>(Order.class),uid);
			/*
			 * 2.循环遍历，加载OrderItem
			 */
			for(Order order : orderList){
				loadOrderItems(order);//为order对象添加它所有订单条目
			}
			/*
			 * 3.返回订单列表
			 */
			return orderList;
		}catch(SQLException e){
			throw new RuntimeException(e);
		}	
	}

	/**
	 * 为order对象添加它所有订单条目
	 * @param order
	 * @throws SQLException 
	 */
	private void loadOrderItems(Order order) throws SQLException {
		/*
		 * 查询两张表：orderItem、book
		 */
		String sql="select * from orderitem i,book b where i.bid=b.bid and oid=?";
		/*
		 * 因为一行结果集对应的不在是一个JavaBean，所不能用beanlisthandler，而是mapListHandler
		 */
		List<Map<String,Object>> mapList=qr.query(sql, new MapListHandler(),order.getOid());
		/*
		 * mapList是多个map，每个map对应一行结果集
		 * {iid=....,count=....,subtotal=..,oid=..,bid=..,bname..,price=..,author=..,image=..,cid=..}
		 * 
		 * 我们希望使用一个map生成两个对象，orderItem、Book，然后在吧book设置给orderItem
		 */
		List<OrderItem> orderItemList=toOrderItemList(mapList);
		order.setOrderItemList(orderItemList);
	}

	/**
	 * 把mapList中每个map转换成两个对象并建立关系
	 * @param mapList
	 * @return
	 */
	private List<OrderItem> toOrderItemList(List<Map<String, Object>> mapList) {
		List<OrderItem> orderItemList=new ArrayList<OrderItem>();
		for(Map<String,Object> map : mapList){
			OrderItem item=toOrderItem(map);
			orderItemList.add(item);
		}
		return orderItemList;
	}

	/**
	 * 把一个map转换成一个orderItem
	 * @param map
	 * @return
	 */
	private OrderItem toOrderItem(Map<String,Object> map) {
		OrderItem orderItem=CommonUtils.toBean(map, OrderItem.class);
		Book book=CommonUtils.toBean(map, Book.class);
		orderItem.setBook(book);
		return orderItem;
	}

	/**
	 * 通过oid加载order
	 * @param oid
	 * @return
	 */
	public Order load(String oid) {
		String sql="select * from orders where oid=?";
		try{
			/*
			 * 1.得到指定订单
			 */
			Order order= qr.query(sql, new BeanHandler<Order>(Order.class),oid);
			/*
			 * 2.为订单加载它所有的条目
			 */
			loadOrderItems(order);
			/*
			 * 3.返回订单
			 */
			return order;
		}catch(SQLException e){
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 通过oid查询订单状态
	 * @param oid
	 * @return
	 */
	public int getStateByOid(String oid){
		try{
			String sql="select state from orders where oid=?";
			Number num=(Number) qr.query(sql, new ScalarHandler(),oid);
			return num.intValue();
		}catch(SQLException e){
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 修改订单状态
	 * @param oid
	 * @param state
	 * @return
	 */
	public void updateState(String oid,int state){
		try{
			String sql="update orders set state=? where oid=?";
			qr.update(sql, state,oid);
		}catch(SQLException e){
			throw new RuntimeException(e);
		}
	}
}

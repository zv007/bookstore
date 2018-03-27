package com.zv.bookstore.category.web.servlet.admin;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zv.bookstore.category.domain.Category;
import com.zv.bookstore.category.service.CategoryException;
import com.zv.bookstore.category.service.CategoryService;

import cn.itcast.commons.CommonUtils;
import cn.itcast.servlet.BaseServlet;

public class AdminCategoryServlet extends BaseServlet {
	private CategoryService categoryService=new CategoryService();
	
	/**
	 * 查询所有分类
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String findAll(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		List<Category> categoryList=categoryService.findAll();
		request.setAttribute("categoryList", categoryList);
		return "f:/adminjsps/admin/category/list.jsp";
	}
	
	/**
	 * 添加分类
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String add(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/*
		 * 1.封装表单数据
		 * 2.补全：cid
		 * 3.调用service方法完成添加工作
		 * 4.调用findAll()
		 */
		Category category=CommonUtils.toBean(request.getParameterMap(), Category.class);
		category.setCid(CommonUtils.uuid());
		if(category.getCname()==null || category.getCname().trim().isEmpty()){
			request.setAttribute("msg", "分类名称不能为空");
			return "f:/adminjsps/admin/category/add.jsp";
		}
		try {
			categoryService.add(category);
		} catch (CategoryException e) {
			request.setAttribute("msg", e.getMessage());
			return "f:/adminjsps/admin/category/add.jsp";
		}
		return findAll(request, response);
	}
	
	/**
	 * 删除分类
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String delete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/*
		 * 1.获取参数：cid
		 * 2.调用service方法，传递cid参数
		 * 	*如果抛出异常，保存异常信息，转发到msg.jsp显示
		 * 3.调用findAll（）
		 */
		String cid=request.getParameter("cid");
		try{
			categoryService.delete(cid);
			return findAll(request, response);
		}catch(CategoryException e){
			request.setAttribute("msg", e.getMessage());
			return "f:/adminjsps/msg.jsp";
		}
	}

	/**
	 * 修改分类前
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String editPre(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/*
		 * 1.获取cid
		 * 2.调用service方法，传递cid得到category
		 * 3.保存到request域中，转发到mod.jsp
		 */
		Category category=categoryService.findByCid(request.getParameter("cid"));
		request.setAttribute("category", category);
		return "f:/adminjsps/admin/category/mod.jsp";
	}

	/**
	 * 修改分类
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String edit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/*
		 * 1.封装表单数据
		 * 2.调用service修改分类
		 * 	
		 * 3.调用findAll的
		 */
		Category category=CommonUtils.toBean(request.getParameterMap(), Category.class);
		
		if(category.getCname()==null || category.getCname().trim().isEmpty()){
			request.setAttribute("msg", "分类名称不能为空");
			return "f:/adminjsps/admin/category/mod.jsp";
		}
		try {
			categoryService.edit(category);
			return findAll(request, response);
		} catch (CategoryException e) {
			request.setAttribute("category", category);
			request.setAttribute("msg", e.getMessage());
			return "f:/adminjsps/admin/category/mod.jsp";
		}
	}


}

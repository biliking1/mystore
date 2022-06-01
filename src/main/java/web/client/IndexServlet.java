package web.client;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import domain.Book;
import domain.Category;
import domain.Reco;
import service.impl.BusinessServiceImpl;
import utils.JdbcUtils;
import utils.WebUtils;

import domain.Page;
import domain.User;
import service.impl.BusinessServiceImpl;

public class IndexServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String method = request.getParameter("method");
		if(method.equalsIgnoreCase("getAll")){
			getAll(request, response);
		}else if(method.equalsIgnoreCase("listBookWithCategory")){
			listBookWithCategory(request, response);
		}
		else if(method.equalsIgnoreCase("query"))
				{
			query(request, response);
				}
	}
	public void query(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		try 
		{
		BusinessServiceImpl service = new BusinessServiceImpl();
	
		List<Category> categories = service.getAllCategory();
		request.setAttribute("categories", categories);
		if(request.getSession().getAttribute("user")==null)return;
		User user = (User)request.getSession().getAttribute("user");
		String name=user.getUsername();
		String pagenum = request.getParameter("pagenum");
		QueryRunner runner = new QueryRunner(JdbcUtils.getDataSource());
		
		String sql="select count(*) from tj,book  where tj.bookid=book.id and tj.username=?;";
		Object pram[]={name};
		//注意观察这里的问题
		Long total = (Long)runner.query(sql, pram, new ScalarHandler());
	   int totalrecord=total.intValue();
		
		
			Page page = null;
			if(pagenum == null){
				page = new Page(1,totalrecord);
			}else{
				page = new Page(Integer.parseInt(pagenum), totalrecord);
			}
			
				
				 sql = "select* from book,tj  where bookid=id and username=? limit ?,?;";
				Object params[] = {name, page.getStartindex(), page.getPagesize()};
			 
			
			page.setList((List<Book>)runner.query(sql, params, new BeanListHandler(Book.class)));
		
	
		request.setAttribute("page", page);
		request.getRequestDispatcher("/client/body.jsp").forward(request, response);
		}
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			request.setAttribute("message", "删除失败");
		}
	}

	private void getAll(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		BusinessServiceImpl service = new BusinessServiceImpl();
		List<Category> categories = service.getAllCategory();
		request.setAttribute("categories", categories);
		String pagenum = request.getParameter("pagenum");
		Page page = service.getBookPageData(pagenum);
		request.setAttribute("page", page);
		
		request.getRequestDispatcher("/client/body.jsp").forward(request, response);
	}
	
	public void listBookWithCategory(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		BusinessServiceImpl service = new BusinessServiceImpl();
		String category_id = request.getParameter("category_id");
		List<Category> categories = service.getAllCategory();
		request.setAttribute("categories", categories);
		String pagenum = request.getParameter("pagenum");
		Page page = service.getBookPageData(pagenum, category_id);
		request.setAttribute("page", page);
		request.getRequestDispatcher("/client/body.jsp").forward(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doGet(request, response);
	}

}

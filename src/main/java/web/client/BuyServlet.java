package web.client;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.dbutils.QueryRunner;
import domain.Book;
import domain.Cart;
import domain.User;
import service.impl.BusinessServiceImpl;
import utils.JdbcUtils;

public class BuyServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try{
			String bookid = request.getParameter("bookid");
			BusinessServiceImpl service = new BusinessServiceImpl();
			Book book = service.findBook(bookid);
			Cart cart = (Cart) request.getSession().getAttribute("cart");
			if(cart == null){
				cart = new Cart();
				request.getSession().setAttribute("cart", cart);
			}
			service.buyBook(cart, book);
			//String bookname = request.getParameter("bookname");
			String bookname=new String(request.getParameter("bookname").getBytes("ISO-8859-1"),"UTF-8");
			User user = (User)request.getSession().getAttribute("user");
			String name=user.getUsername();
			QueryRunner runner = new QueryRunner(JdbcUtils.getDataSource());
			String sql="insert into tj values(?,?);";
			Object pram[]={name,bookid};
			runner.update(sql, pram);
			request.getRequestDispatcher("/client/listcart.jsp").forward(request, response);
		}catch(Exception e){
			e.printStackTrace();
			request.setAttribute("message", "¹ºÂòÊ§°Ü");
			request.getRequestDispatcher("/message.jsp").forward(request, response);
		}
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doGet(request, response);
	}

}

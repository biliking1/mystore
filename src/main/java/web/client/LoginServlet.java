package web.client;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.dbutils.QueryRunner;

import domain.User;
import java.util.Date;
import service.impl.BusinessServiceImpl;
import utils.JdbcUtils;

public class LoginServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		BusinessServiceImpl service = new BusinessServiceImpl();
		User user = service.userLogin(username, password);
		if(user == null){
			request.setAttribute("message", "用户名和密码不对");
			request.getRequestDispatcher("/message.jsp").forward(request, response);
			return;
		}
		request.getSession().setAttribute("user", user);
		request.getRequestDispatcher("/client/head.jsp").forward(request, response);
		String ip=getIpAddr(request);
		Date date = new Date();	
		String time=date.toString();
		String name=user.getUsername();
		
		QueryRunner runner = new QueryRunner(JdbcUtils.getDataSource());
		//String sql = "delete from book where id=?;";
		String sql="insert into ip values(?,?,?);";
		Object params[] = {ip,name,time};
		try {			runner.update(sql, params);
		request.setAttribute("message", "记录成功");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			request.setAttribute("message", "记录失败");
		}
	
	}
	public String getIpAddr(HttpServletRequest request) { 
	  if(request.getHeader("x-forwarded-for")==null)
	   {
		   return request.getRemoteAddr(); 		   
	   }
	 return request.getHeader("x-forwarded-for");
	}
	
	 
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doGet(request, response);
	}

}

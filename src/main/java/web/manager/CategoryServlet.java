package web.manager;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.dbutils.QueryRunner;

import service.impl.BusinessServiceImpl;
import utils.JdbcUtils;
import utils.WebUtils;
import domain.Category;

//��������CRUD����
public class CategoryServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String method = request.getParameter("method");
		if (method.equals("add")) {
			add(request, response);
		} else if (method.equals("delete")) {
			delete(request, response);
		} else if (method.equals("update")) {
			update(request, response);
		} else if (method.equals("find")) {
			find(request, response);
		} else if (method.equals("listall")) {
			listAll(request, response);
		} else {
			request.setAttribute("message", "��֧�ִ������");
			request.getRequestDispatcher("/message.jsp").forward(request,
					response);
		}
	}

	private void listAll(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		BusinessServiceImpl service = new BusinessServiceImpl();
		List<Category> CategoryLis = service.getAllCategory();
		request.setAttribute("categories", CategoryLis);
		request.getRequestDispatcher("/manager/listcategory.jsp").forward(request, response);
	}

	private void find(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub

	}

	private void update(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub

	}

	private void delete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		

      String id=new String(request.getParameter("id").getBytes("ISO-8859-1"),"UTF-8");;
		QueryRunner runner = new QueryRunner(JdbcUtils.getDataSource());
		//name="��";
		String sql = "delete from category where id='" + id +"';";
		//String sql="update category set name='"+name+"' where name='��'";
		Object params[] = {};
		try {			runner.update(sql, params);
		request.setAttribute("message", "ɾ���ɹ�");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			request.setAttribute("message", "ɾ��ʧ��");
		}

	}

	private void add(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			String name = request.getParameter("name");
			String description = request.getParameter("description");

			Category category = new Category();
			category.setName(name);
			category.setDescription(description);
			category.setId(WebUtils.makeID());

			BusinessServiceImpl service = new BusinessServiceImpl();
			service.addCategory(category);
			
			request.setAttribute("message", name+"��ӳɹ�");
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("message", "���ʧ��");
		}
		request.getRequestDispatcher("/message.jsp").forward(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doGet(request, response);
	}

}

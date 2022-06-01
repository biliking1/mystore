package web.client;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import service.impl.BusinessServiceImpl;
import domain.Cart;
import domain.User;
import java.util.Properties;
import java.security.GeneralSecurityException;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import com.sun.mail.util.MailSSLSocketFactory;
public class OrderServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try{
			User user = (User) request.getSession().getAttribute("user");
			if(user == null){
				request.setAttribute("message", "对不起，请先登录");
				request.getRequestDispatcher("/message.jsp").forward(request, response);
				return;
			}
			//收件人邮箱
			 String to = user.getEmail();
			 
		      // 发件人电子邮箱
		      String from = "645028763@qq.com";
		 
		      // 指定发送邮件的主机为 smtp.qq.com
		      String host = "smtp.qq.com";  //QQ 邮件服务器
		 
		      // 获取系统属性
		      Properties properties = System.getProperties();
		 
		      // 设置邮件服务器
		      properties.setProperty("mail.smtp.host", host);
		 
		      properties.put("mail.smtp.auth", "true");
		      MailSSLSocketFactory sf = new MailSSLSocketFactory();
		        sf.setTrustAllHosts(true);
		        properties.put("mail.smtp.ssl.enable", "true");
		        properties.put("mail.smtp.ssl.socketFactory", sf);
		      // 获取默认session对象
		      Session session = Session.getDefaultInstance(properties,new Authenticator(){
		        public PasswordAuthentication getPasswordAuthentication()
		        {
		         return new PasswordAuthentication("645028763@qq.com", "jctkrcxjehthbdcg"); //发件人邮件用户名、授权码
		        }
		       });
		 
		      try{
		         // 创建默认的 MimeMessage 对象
		         MimeMessage message = new MimeMessage(session);
		 
		         // Set From: 头部头字段
		         message.setFrom(new InternetAddress(from));
		 
		         // Set To: 头部头字段
		         message.addRecipient(Message.RecipientType.TO,
		                                  new InternetAddress(to));
		 
		         // Set Subject: 头部头字段
		         message.setSubject("订单生成通知!");
		 
		         // 设置消息体
		         message.setText("您的订单已生成，祝您购物愉快");
		 
		         // 发送消息
		         Transport.send(message);
		         System.out.println("Sent message successfully....from runoob.com");
		      }catch (MessagingException mex) {
		         mex.printStackTrace();
		      }
			Cart cart = (Cart) request.getSession().getAttribute("cart");
			BusinessServiceImpl service = new BusinessServiceImpl();
			service.createOrder(cart, user);
			request.setAttribute("message", "订单已生成，请注意查收邮件");
			request.getSession().removeAttribute("cart");//清空购物车，这是我自己加上去的，因为点购买后，如果不清空购物车，前端点击查看购物车又出现了
			request.getRequestDispatcher("/message.jsp").forward(request, response);
		}catch(Exception e){
			e.printStackTrace();
			request.setAttribute("message", "订单生成失败");
			request.getRequestDispatcher("/message.jsp").forward(request, response);
		}
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doGet(request, response);
	}

}

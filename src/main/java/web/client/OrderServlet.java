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
				request.setAttribute("message", "�Բ������ȵ�¼");
				request.getRequestDispatcher("/message.jsp").forward(request, response);
				return;
			}
			//�ռ�������
			 String to = user.getEmail();
			 
		      // �����˵�������
		      String from = "645028763@qq.com";
		 
		      // ָ�������ʼ�������Ϊ smtp.qq.com
		      String host = "smtp.qq.com";  //QQ �ʼ�������
		 
		      // ��ȡϵͳ����
		      Properties properties = System.getProperties();
		 
		      // �����ʼ�������
		      properties.setProperty("mail.smtp.host", host);
		 
		      properties.put("mail.smtp.auth", "true");
		      MailSSLSocketFactory sf = new MailSSLSocketFactory();
		        sf.setTrustAllHosts(true);
		        properties.put("mail.smtp.ssl.enable", "true");
		        properties.put("mail.smtp.ssl.socketFactory", sf);
		      // ��ȡĬ��session����
		      Session session = Session.getDefaultInstance(properties,new Authenticator(){
		        public PasswordAuthentication getPasswordAuthentication()
		        {
		         return new PasswordAuthentication("645028763@qq.com", "jctkrcxjehthbdcg"); //�������ʼ��û�������Ȩ��
		        }
		       });
		 
		      try{
		         // ����Ĭ�ϵ� MimeMessage ����
		         MimeMessage message = new MimeMessage(session);
		 
		         // Set From: ͷ��ͷ�ֶ�
		         message.setFrom(new InternetAddress(from));
		 
		         // Set To: ͷ��ͷ�ֶ�
		         message.addRecipient(Message.RecipientType.TO,
		                                  new InternetAddress(to));
		 
		         // Set Subject: ͷ��ͷ�ֶ�
		         message.setSubject("��������֪ͨ!");
		 
		         // ������Ϣ��
		         message.setText("���Ķ��������ɣ�ף���������");
		 
		         // ������Ϣ
		         Transport.send(message);
		         System.out.println("Sent message successfully....from runoob.com");
		      }catch (MessagingException mex) {
		         mex.printStackTrace();
		      }
			Cart cart = (Cart) request.getSession().getAttribute("cart");
			BusinessServiceImpl service = new BusinessServiceImpl();
			service.createOrder(cart, user);
			request.setAttribute("message", "���������ɣ���ע������ʼ�");
			request.getSession().removeAttribute("cart");//��չ��ﳵ���������Լ�����ȥ�ģ���Ϊ�㹺����������չ��ﳵ��ǰ�˵���鿴���ﳵ�ֳ�����
			request.getRequestDispatcher("/message.jsp").forward(request, response);
		}catch(Exception e){
			e.printStackTrace();
			request.setAttribute("message", "��������ʧ��");
			request.getRequestDispatcher("/message.jsp").forward(request, response);
		}
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doGet(request, response);
	}

}

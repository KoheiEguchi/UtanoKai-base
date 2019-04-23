package haiku.admin;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import beans.UserBean;
import dao.UserDAO;
import haiku.Common;

/**
 * Servlet implementation class UserList
 */
@WebServlet("/UserList")
public class UserList extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UserList() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//認証無しでの閲覧禁止
		if(Common.loginCheck(request, response) == true) {
			//管理人以外の閲覧禁止
			if(Common.adminCheck(request, response) == true) {
				UserDAO dao = new UserDAO();
				//全会員を取得
				ArrayList<UserBean> userList = dao.allUser();
				request.setAttribute("userList", userList);
				
				RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/admin/userList.jsp");
				dispatcher.forward(request,response);
			}
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//強制退会してきた場合
		if(request.getAttribute("delete") != null) {
			request.setAttribute("msg", "退会させました。");
			doGet(request, response);
		//強制退会に行っていない場合
		}else {
			request.setCharacterEncoding("UTF-8");
			
			String name = request.getParameter("name");
			
			//入力された俳号に当てはまる会員を取得
			UserDAO dao = new UserDAO();
			ArrayList<UserBean> searchUser = dao.searchUser(name);
			//一人も見つからなかった場合
			if(searchUser.size() == 0) {
				request.setAttribute("msg", "当てはまる俳号の方は見つかりませんでした。");
			//見つかった場合
			}else {
				request.setAttribute("userList", searchUser);
			}
			
			RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/admin/userList.jsp");
			dispatcher.forward(request,response);
		}
	}

}
package io.codesalad.controller;

import java.io.IOException;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import io.codesalad.model.DatabaseManager;
import io.codesalad.model.Problem;
import io.codesalad.model.ProblemProcessor;
import io.codesalad.model.User;

/**
 * Servlet implementation class NewProblem
 */
@WebServlet("/NewProblem")
public class NewProblem extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public NewProblem() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
		String testCases = "";
		DatabaseManager newDb = new DatabaseManager();
		String pid = "";
		String plevel = "";
		try {
			ResultSet rs = newDb.getDBConnection().executeQuery("SELECT max(ProbId) as id  FROM CodeSalad.Problems");
			while (rs.next()) {
				pid = rs.getString("id");
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		int id = Integer.parseInt(pid);
		id++;
		pid=Integer.toString(id);
		//System.out.println(pid);

		

		for (int i = 1; i <= 14; i = i + 2) {
			if(request.getParameter(Integer.toString(i))==null)
			{
				break;
			}
			else{
			testCases += request.getParameter(Integer.toString(i)) + "|"
					+ request.getParameter(Integer.toString(i + 1))+"\n";
			}
		}

		String code = request.getParameter("code");
		String pname = request.getParameter("Pname");
		plevel = request.getParameter("level");
		User user = null;
		HttpSession session = request.getSession(false);
		user = (User) session.getAttribute("user");

		ProblemProcessor newProcessor = new ProblemProcessor();

		newProcessor.saveProblem(code, pid, testCases);

		DatabaseManager newDbjob = new DatabaseManager();
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		String time = sdf.format(cal.getTime());
		
		String query = "insert into CodeSalad.Problems (`Pname`,`CreatedBy`,`CreatedOn`,`MaxTime`,`MaxMemory`,`Difficulty`) values ('" + pname + "' ,  '" +user.uname+ " ' '" + "' , '" + time
				+ "' , ' ' , ' ' , '" + plevel + "' )";
		
		try {
			newDbjob.getDBConnection().execute(query);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		request.setAttribute("pid", pid);
		RequestDispatcher rd = getServletContext().getRequestDispatcher("/CodeSalad/ViewProblem");
		rd.forward(request, response);
		
		
	}

}

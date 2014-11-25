package org.soen387.app;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dsrg.soenea.application.servlet.DispatcherServlet;
import org.dsrg.soenea.application.servlet.Servlet;
import org.dsrg.soenea.domain.mapper.DomainObjectNotFoundException;
import org.dsrg.soenea.service.MySQLConnectionFactory;
import org.dsrg.soenea.service.registry.Registry;
import org.dsrg.soenea.service.threadLocal.DbRegistry;
import org.soen387.domain.model.challenge.mapper.ChallengeMapper;
import org.soen387.domain.model.checkerboard.mapper.CheckerBoardMapper;
import org.soen387.domain.model.player.Player;
import org.soen387.domain.model.player.mapper.PlayerMapper;
import org.soen387.domain.model.user.mapper.UserMapper;

/**
 * Servlet implementation class PageController
 */
@WebServlet("/PageController")
public abstract class AbstractPageController extends Servlet {
	private static final long serialVersionUID = 1L;
    private static boolean DBSetup = false;
    /**
     * @see DispatcherServlet#DispatcherServlet()
     */
    public AbstractPageController() {
        super();
    }
    
    @Override
    public void init() throws ServletException {
    	AbstractPageController.setupDb();
    };

    public static synchronized void setupDb() {
    	if(!DBSetup) {
    		prepareDbRegistry();
    	}
    }
    
	public static void prepareDbRegistry() {
		MySQLConnectionFactory f = new MySQLConnectionFactory(null, null, null, null);
		try {
			f.defaultInitialization();
		} catch (SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		DbRegistry.setConFactory(f);
		String tablePrefix;
		try {
			tablePrefix = Registry.getProperty("mySqlTablePrefix");
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			tablePrefix = "";
		}
		if(tablePrefix == null) {
			tablePrefix = "";
		}
		DbRegistry.setTablePrefix(tablePrefix);
	}
	
	public static void setupRequest(HttpServletRequest request) {
		try { //Make sure we've started a transaction
			DbRegistry.getDbConnection().setAutoCommit(false);
		} catch (SQLException e) {
			// eat the sqlexception, but throw a stacktrace to console
			e.printStackTrace();
		}
		try {
			Long pid = (Long)request.getSession(true).getAttribute("playerid");
			System.out.println("You are playing with player: " + pid);
			if(pid != null) {
				request.setAttribute("CurrentPlayer", PlayerMapper.find(pid));
			}
		} catch (DomainObjectNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void teardownRequest() {
		try {
			UserMapper.IM.remove();
			PlayerMapper.IM.remove();
			CheckerBoardMapper.IM.remove();
			ChallengeMapper.IM.remove();
			DbRegistry.closeDbConnectionIfNeeded();
		} catch (SQLException e) {
			// eat the sqlexception, but throw a stacktrace to console
			e.printStackTrace();
		}
	}
	
	public Player getCurrentPlayer(HttpServletRequest request) {
		return (Player)request.getAttribute("CurrentPlayer");
	}
	
	public void forwardError(HttpServletRequest request,
			HttpServletResponse response, String error) throws ServletException, IOException {
		request.setAttribute("errorMessage", error);
		request.getRequestDispatcher("/WEB-INF/jsp/xml/failure.jsp").forward(request, response);
	}


}

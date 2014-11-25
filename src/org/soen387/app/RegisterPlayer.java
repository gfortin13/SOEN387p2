package org.soen387.app;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dsrg.soenea.service.threadLocal.DbRegistry;
import org.soen387.domain.model.player.Player;
import org.soen387.domain.model.player.mapper.PlayerMapper;
import org.soen387.domain.model.player.tdg.PlayerTDG;
import org.soen387.domain.model.user.User;
import org.soen387.domain.model.user.mapper.UserMapper;
import org.soen387.domain.model.user.tdg.UserTDG;

/**
 * Servlet implementation class ListGames
 */
@WebServlet("/RegisterPlayer")
public class RegisterPlayer extends AbstractPageController implements Servlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see AbstractPageController#AbstractPageController()
     */
    public RegisterPlayer() {
        super();
        // TODO Auto-generated constructor stub
    }

	@Override
	protected void processRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		setupRequest(request);
		
		try {
			String username = request.getParameter("username");
			String password = request.getParameter("password");
			String firstName = request.getParameter("firstName");
			String lastName = request.getParameter("lastName");
			String email = request.getParameter("email");
			
			User u = new User(UserTDG.getMaxId(), 1, username, password);
			Player p = new Player(PlayerTDG.getMaxId(), 1, firstName, lastName, email, u);
			
			UserMapper.insert(u);
			PlayerMapper.insert(p);
			request.setAttribute("player", p);
			
			//Commit
			DbRegistry.getDbConnection().commit();
			
			request.getRequestDispatcher("/WEB-INF/jsp/xml/player.jsp").forward(request, response);
		} catch (SQLException e) {
			forwardError(request, response, e.getMessage());
			e.printStackTrace();
		} finally {
			teardownRequest();	
		}
		
		
		
	}


}

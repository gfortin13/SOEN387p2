package org.soen387.app;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dsrg.soenea.domain.MapperException;
import org.soen387.domain.model.player.Player;
import org.soen387.domain.model.player.mapper.PlayerMapper;
import org.soen387.domain.model.user.User;
import org.soen387.domain.model.user.mapper.UserMapper;

/**
 * Servlet implementation class ListGames
 */
@WebServlet("/LogIn")
public class LogIn extends AbstractPageController implements Servlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see AbstractPageController#AbstractPageController()
     */
    public LogIn() {
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
			User u = UserMapper.find(username, password);
			Player p = PlayerMapper.find(u);
			request.setAttribute("player", p);

			//Security Check!
			request.getSession(true).invalidate();
			
			//Store PlayerId in sesssion
			request.getSession(true).setAttribute("playerid", p.getId());

			request.getRequestDispatcher("/WEB-INF/jsp/xml/player.jsp").forward(request, response);
		} catch (MapperException | SQLException e) {
			forwardError(request, response, e.getMessage());
			e.printStackTrace();
		} finally {
			teardownRequest();	
		}
		
		
		
	}


}

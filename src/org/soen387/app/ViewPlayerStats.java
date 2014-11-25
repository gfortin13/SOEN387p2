package org.soen387.app;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dsrg.soenea.domain.MapperException;
import org.soen387.domain.model.checkerboard.ICheckerBoard;
import org.soen387.domain.model.checkerboard.mapper.CheckerBoardMapper;
import org.soen387.domain.model.player.Player;
import org.soen387.domain.model.player.mapper.PlayerMapper;

/**
 * Servlet implementation class ListGames
 */
@WebServlet("/ViewPlayerStats")
public class ViewPlayerStats extends AbstractPageController implements Servlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see AbstractPageController#AbstractPageController()
     */
    public ViewPlayerStats() {
        super();
        // TODO Auto-generated constructor stub
    }

	@Override
	protected void processRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		setupRequest(request);
		
		try {
			//Get the player
			Player p = PlayerMapper.find(Long.parseLong(request.getParameter("id")));
			
			//Get all games where they are challenger or challengee
			List<ICheckerBoard> games = CheckerBoardMapper.find(p);

			
			request.setAttribute("player", p);
			request.setAttribute("games", games);
			request.getRequestDispatcher("/WEB-INF/jsp/xml/playergames.jsp").forward(request, response);
		} catch (MapperException | NumberFormatException | SQLException e) {
			forwardError(request, response, e.getMessage());
			e.printStackTrace();
		} finally {
			teardownRequest();	
		}
		
		
		
	}


}

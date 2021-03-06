package org.soen387.app;

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dsrg.soenea.domain.MapperException;
import org.soen387.domain.model.player.mapper.PlayerMapper;

/**
 * Servlet implementation class ListGames
 */
@WebServlet("/ListPlayers")
public class ListPlayers extends AbstractPageController implements Servlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see AbstractPageController#AbstractPageController()
     */
    public ListPlayers() {
        super();
        // TODO Auto-generated constructor stub
    }

	@Override
	protected void processRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		setupRequest(request);
		
		try {
			request.setAttribute("players", PlayerMapper.findAll());
			
			request.getRequestDispatcher("/WEB-INF/jsp/xml/players.jsp").forward(request, response);
		} catch (MapperException e) {
			forwardError(request, response, e.getMessage());
			e.printStackTrace();
		} finally {
			teardownRequest();	
		}
		
		
		
	}


}

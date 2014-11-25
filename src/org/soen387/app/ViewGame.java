package org.soen387.app;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dsrg.soenea.domain.MapperException;
import org.soen387.domain.model.checkerboard.CheckerBoard;
import org.soen387.domain.model.checkerboard.mapper.CheckerBoardMapper;

/**
 * Servlet implementation class ListGames
 */
@WebServlet("/ViewGame")
public class ViewGame extends AbstractPageController implements Servlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see AbstractPageController#AbstractPageController()
     */
    public ViewGame() {
        super();
        // TODO Auto-generated constructor stub
    }

	@Override
	protected void processRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		setupRequest(request);
		
		try {
			
			CheckerBoard cb = CheckerBoardMapper.find(Long.parseLong(request.getParameter("id")));
			
			request.setAttribute("checkerboard", cb);
			request.getRequestDispatcher("/WEB-INF/jsp/xml/checkerboard.jsp").forward(request, response);
		} catch (MapperException | NumberFormatException | SQLException e) {
			forwardError(request, response, e.getMessage());
			e.printStackTrace();
		} finally {
			teardownRequest();	
		}
		
		
		
	}


}

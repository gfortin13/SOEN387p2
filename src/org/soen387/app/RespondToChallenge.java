package org.soen387.app;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dsrg.soenea.domain.MapperException;
import org.dsrg.soenea.service.threadLocal.DbRegistry;
import org.soen387.domain.model.challenge.Challenge;
import org.soen387.domain.model.challenge.ChallengeStatus;
import org.soen387.domain.model.challenge.mapper.ChallengeMapper;
import org.soen387.domain.model.checkerboard.CheckerBoard;
import org.soen387.domain.model.checkerboard.GameStatus;
import org.soen387.domain.model.checkerboard.mapper.CheckerBoardMapper;
import org.soen387.domain.model.checkerboard.tdg.CheckerBoardTDG;
import org.soen387.domain.model.player.Player;

/**
 * Servlet implementation class ListGames
 */
@WebServlet("/RespondToChallenge")
public class RespondToChallenge extends AbstractPageController implements Servlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see AbstractPageController#AbstractPageController()
     */
    public RespondToChallenge() {
        super();
        // TODO Auto-generated constructor stub
    }

	@Override
	protected void processRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		setupRequest(request);
		
		try {
			//Get the CurrentPlayer from attributes
			Player current = getCurrentPlayer(request);
			if(current==null) {
				forwardError(request, response, "No Player is currently logged in.");
				return;
			}
			Challenge challenge = ChallengeMapper.find(Long.parseLong(request.getParameter("challenge")));
					

			int version = Integer.parseInt(request.getParameter("version"));
			challenge.setVersion(version);
			
			if(!challenge.getChallengee().equals(current)) {
				forwardError(request, response, "You may not respond to challenges that you did not receive.");
				return;
			}

			if(!challenge.getStatus().equals(ChallengeStatus.Open)) {
				forwardError(request, response, "This challenge has already been responded to.");
				return;
			}
			
			
			ChallengeStatus status = ChallengeStatus.values()[ Integer.parseInt(request.getParameter("status"))];
			challenge.setStatus(status);
			
			if(status.equals(ChallengeStatus.Accepted)) {
				CheckerBoard cb = new CheckerBoard(CheckerBoardTDG.getMaxId(), 1, GameStatus.Ongoing, challenge.getChallengee(), challenge.getChallenger(), challenge.getChallengee());
				CheckerBoardMapper.insert(cb);
				//Wouldn't it work better of we deleted on accept? That's why I have this
				//in an if/else, to highlight that concern.
				//ChallengeMapper.delete(challenge);
				ChallengeMapper.update(challenge);
			} else {			
				ChallengeMapper.update(challenge);
			}
			
			//Commit
			DbRegistry.getDbConnection().commit();
			
			request.setAttribute("challenge", challenge);
			request.getRequestDispatcher("/WEB-INF/jsp/xml/challenge.jsp").forward(request, response);
		} catch (MapperException | NumberFormatException | SQLException e) {
			try {
				DbRegistry.getDbConnection().rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			forwardError(request, response, e.getMessage());
			e.printStackTrace();
		} finally {
			teardownRequest();	
		}
		
		
		
	}


}

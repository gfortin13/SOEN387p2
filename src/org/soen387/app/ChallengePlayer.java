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
import org.dsrg.soenea.service.threadLocal.DbRegistry;
import org.soen387.domain.model.challenge.Challenge;
import org.soen387.domain.model.challenge.ChallengeStatus;
import org.soen387.domain.model.challenge.IChallenge;
import org.soen387.domain.model.challenge.mapper.ChallengeMapper;
import org.soen387.domain.model.challenge.tdg.ChallengeTDG;
import org.soen387.domain.model.player.IPlayer;
import org.soen387.domain.model.player.Player;
import org.soen387.domain.model.player.mapper.PlayerMapper;

/**
 * Servlet implementation class ListGames
 */
@WebServlet("/ChallengePlayer")
public class ChallengePlayer extends AbstractPageController implements Servlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see AbstractPageController#AbstractPageController()
     */
    public ChallengePlayer() {
        super();
        // TODO Auto-generated constructor stub
    }

	@Override
	protected void processRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		setupRequest(request);
		
		try {
			//Get the CurrentPlayer from attributes
			Player challenger = getCurrentPlayer(request);
			if(challenger==null) {
				forwardError(request, response, "No Player is currently logged in.");
				return;
			}
			
			//Get the Challenged Player
			Player challengee = PlayerMapper.find(Long.parseLong(request.getParameter("player")));
			
			if(challenger.equals(challengee)) {
				forwardError(request, response, "You may not challenge yourself.");
				return;
			}
			
			//Alternately, make a more complex sql, but I hate making more SELECT statements, so I
			//went this route... would fix if this caused performance problems, but is cleaner otherwise
			List<IChallenge> challengerGames = ChallengeMapper.find(challenger);
			challengerGames.retainAll( ChallengeMapper.find(challengee));
			for(IChallenge c: challengerGames) {
				if(c.getStatus().equals(ChallengeStatus.Open)) {
					forwardError(request, response, "You already have an open challenge with this Player.");
					return;
				}
			}
			
			//Create a new Challenge Object
			Challenge c = new Challenge(ChallengeTDG.getMaxId(), 1, challenger, challengee, ChallengeStatus.Open);
			request.setAttribute("challenge", c);
			
			//Call the Challenge Mapper to Insert it
			ChallengeMapper.insert(c);
			
			//Commit
			DbRegistry.getDbConnection().commit();
			
			request.getRequestDispatcher("/WEB-INF/jsp/xml/challenge.jsp").forward(request, response);
		} catch (MapperException | NumberFormatException | SQLException e) {
			forwardError(request, response, e.getMessage());
			e.printStackTrace();
		} finally {
			teardownRequest();	
		}
		
		
		
	}


}

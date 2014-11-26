package org.soen387.test;

import java.sql.SQLException;

import org.soen387.app.AbstractPageController;
import org.soen387.domain.model.challenge.tdg.ChallengeTDG;
import org.soen387.domain.model.checkerboard.tdg.CheckerBoardTDG;
import org.soen387.domain.model.player.tdg.PlayerTDG;
import org.soen387.domain.model.user.tdg.UserTDG;

public class Setup {

	public static void main(String[] args) throws InterruptedException {
		AbstractPageController.setupDb();
		try {
			//Uncomment to wipe and recreate tables
			/*CheckerBoardTDG.dropTable();
			PlayerTDG.dropTable();
			ChallengeTDG.dropTable();
			UserTDG.dropTable();
			*/
			CheckerBoardTDG.createTable();
			PlayerTDG.createTable();
			ChallengeTDG.createTable();
			UserTDG.createTable();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

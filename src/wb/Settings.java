package wb;

import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import java.io.File;

/**
 * Game settings and configuration.
 *
 * @author Jashank Jeremy {@literal <z5017851@cse.unsw.edu.au>}
 * @author Matthew Phillips {@literal <z5062330@cse.unsw.edu.au>}
 */
class Settings {
	private Difficulty gameDifficulty;
	private Mode state;

	private boolean running;
	private boolean gg;
	private double moveIncrement = 0.2;

	private String playerName;
	private ScoreParser scores;

	private String[] savedGames;

	public Settings() {

		FileIO.fillSettings(this);
		this.scores = new ScoreParser();

		this.populateSavedGames("saved");
	}

	private void populateSavedGames(String path) {
		File dir = new File(path);

		Collection<String> files  = new ArrayList<String>();

		if (dir.isDirectory()) {
			File[] listFiles = dir.listFiles();

			for (File file : listFiles) {
				if (file.isFile()) {
					files.add(file.getName());
				}
			}
		}

		this.savedGames = files.toArray(new String[]{});
	}

	public boolean isRunning(){
		return this.running;
	}

	public double getMoveIncrement(){
		return this.moveIncrement;
	}


	public void setMoveIncrement(double moveIncrement){
		this.moveIncrement = moveIncrement;
	}

	public String getPlayerName(){
		return this.playerName;
	}

	public void setPlayerName(String playerName){
		this.playerName = playerName;
	}

	public Mode getState(){
		return this.state;
	}

	public void setRunning(boolean setTo){
		this.running = setTo;
	}

	public void setState(Mode newMode){
		this.state = newMode;
	}

	public void updateScores(int campaignMoves){
		this.scores.updateScores(playerName, campaignMoves);
	}

	public boolean getGameOver(){
		return this.gg;
	}

	public void setGameOver(boolean isFinished){
		this.gg = isFinished;
	}

	public Difficulty getDifficulty(){
		return this.gameDifficulty;
	}

	public void setDifficulty(Difficulty newDifficulty){
		this.gameDifficulty = newDifficulty;
	}
}

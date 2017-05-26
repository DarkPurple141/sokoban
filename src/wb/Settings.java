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
	private double moveIncrement;

	private String playerName;
	private ScoreParser scores;

	private String[] savedGames;

	/**
	 * Initialises a new batch of Settings
	 * Loads the files persisted in the settings file
	 *
	 */
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

	/**
	 *
	 * @return A boolean representing whether or not the game is running
	 */
	public boolean isRunning(){
		return this.running;
	}


	/**
	 *
	 * @param setTo to set whether or not the game is currently running
	 */
	public void setRunning(boolean setTo){
		this.running = setTo;
	}

	/**
	 *
	 * @return The distance that the pieces move each frame
	 */
	public double getMoveIncrement(){
		return this.moveIncrement;
	}

	/**
	 *
	 * @param moveIncrement : The new distance pieces will move each frame
	 */
	public void setMoveIncrement(double moveIncrement){
		this.moveIncrement = moveIncrement;
	}


	/**
	 *
	 * @return The name of the current player
	 */
	public String getPlayerName(){
		return this.playerName;
	}

	/**
	 *
	 * @param playerName : The updated player name to be used from this point on
	 */
	public void setPlayerName(String playerName){
		if (playerName == null){
			this.playerName = "admin";
		}else{
			this.playerName = playerName;
		}
	}


	/**
	 *
	 * @return The current state of the game
	 */
	public Mode getState(){
		return this.state;
	}


	/**
	 * Used to update the current state of the game
	 * @param newMode : The new mode the game will be set to
	 */
	public void setState(Mode newMode){
		this.state = newMode;
	}

	/**
	 *
	 * @return true if the game is over and false otherwise
	 */
	public boolean getGameOver(){
		return this.gg;
	}

	/**
	 *
	 * @param isFinished : Determines whether the current game is over
	 */
	public void setGameOver(boolean isFinished){
		this.gg = isFinished;
	}

	/**
	 *
	 * @param campaignMoves :
	 */
	public void updateScores(int campaignMoves){
		this.scores.updateScores(playerName, campaignMoves);
	}


	/**
	 *
	 * @return The string representation of the high scores
	 */
	public String getScoreTable(){
		return scores.getScoreTable();
	}


	/**
	 *
	 * @return The current difficulty of the game
	 */
	public Difficulty getDifficulty(){
		return this.gameDifficulty;
	}

	/**
	 *
	 * @param newDifficulty : The new difficulty of all subsequent boards
	 */
	public void setDifficulty(Difficulty newDifficulty){
		this.gameDifficulty = newDifficulty;
	}

	/**
	 *
	 * @return An array of all of the save game file names
	 */
	public String[] getSavedGames(){
		return savedGames;
	}
}

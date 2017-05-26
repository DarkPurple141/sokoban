package wb;

public class Settings{

	private Difficulty gameDifficulty;
	private Mode state;
	
	private boolean running;
	private boolean gg;
	private double moveIncrement = 0.2;
	
	private String playerName;
	private ScoreParser scores;

	private String[] savedGames;

	public Settings(){
		this.gameDifficulty = Difficulty.MEDIUM;
		this.state = Mode.NORMAL;
		this.playerName = "admin";
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
}
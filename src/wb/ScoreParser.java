package wb;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * high score file parser and generator
 *
 * @author Alex Hinds {@literal <z3420752@cse.unsw.edu.au>}
 * @author Jashank Jeremy {@literal <z5017851@cse.unsw.edu.au>}
 * @author Ben Lichtman {@literal <z5059760@cse.unsw.edu.au>}
 * @author Matthew Phillips {@literal <z5062330@cse.unsw.edu.au>}
 */
class ScoreParser {
	// FIXME(jashankj): why is this not XML?

	private final static String PATH = "Hall_of_Fame";
	private List<String> names;
	private List<Integer> scores;

	public ScoreParser() {
		Scanner sc = null;
		scores = new ArrayList<Integer>();
		names = new ArrayList<String>();

		try {
			sc = new Scanner(new FileReader(PATH));
			while (sc.hasNext()) {
				parseLine(sc.nextLine());
			}

		} catch (FileNotFoundException e) {
			System.err.println("Score Reader ** No File Found.");
		} finally {
			if (sc != null) sc.close();
		}
	}

	/**
	 * Parse and return the String for the High Scores from
	 * the HallOfFame file for the High Score Button.
	 */
	public String getScoreTable() {
		String toGive = "<html><h1 style=\"text-align:center;\">Top Scores</h1>";
		int index = 0;
		while (index < names.size()) {
			toGive += "<h2>" + Integer.toString(index+1) + ". " + names.get(index) +
				" " + Integer.toString(scores.get(index)) + "</h2>";
			index++;
		}

		return toGive;
	}

	/**
	 * Updates a high score if it's in the top-10.
	 * @param name	The player name
	 * @param score The player score
	 */
	public void updateScores(String name, int score) {
		boolean change = false;
		int index = 0;
		for (int s : scores) {
			if (score < s) {
				scores.add(index,score);
				names.add(index,name);
				change = true;
				break;
			}
			index++;
		}

		if (scores.size() < 10 && !change) {
			scores.add(score);
			names.add(name);
			change = true;
		} else if (scores.size() > 10) {
			scores.remove(10);
			names.remove(10);
			change = true;
		}

		if (change) {
			writeScores();
		}
	}

	/**
	 * Write the Scores to disk.
	 */
	private void writeScores() {
		try(FileWriter fw = new FileWriter(PATH, false);
			BufferedWriter bw = new BufferedWriter(fw);
			PrintWriter out = new PrintWriter(bw))
		{
			int index = 0;
			while (index < names.size()) {
				out.printf("%s %d\n", names.get(index), scores.get(index));
				index++;
			}
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Parse lines from the HS file.
	 * @param line	A line in HS file.
	 */
	private void parseLine(String line) {
		String data[] = line.split(" ");
		names.add(data[0]);
		scores.add(Integer.parseInt(data[1]));
	}

}

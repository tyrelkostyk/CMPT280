import com.opencsv.CSVReader;
import lib280.base.Pair280;
import lib280.hashtable.KeyedChainedHashTable280;
import lib280.tree.OrderedSimpleTree280;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

// This project uses a JAR called opencsv which is a library for reading and
// writing CSV (comma-separated value) files.
// 
// You don't need to do this for this project, because it's already done, but
// if you want to use opencsv in other projects on your own, here's the process:
//
// 1. Download opencsv-3.1.jar from http://sourceforge.net/projects/opencsv/
// 2. Drag opencsv-3.1.jar into your project.
// 3. Right-click on the project in the package explorer, select "Properties" (at bottom of popup menu)
// 4. Choose the "Libraries" tab
// 5. Click "Add JARs"
// 6. Select the opencsv-3.1.jar from within your project from the list.
// 7. At the top of your .java file add the following imports:
//        import java.io.FileReader;
//        import com.opencsv.CSVReader;
//
// Reference documentation for opencsv is here:  
// http://opencsv.sourceforge.net/apidocs/overview-summary.html


public class QuestLog extends KeyedChainedHashTable280<String, QuestLogEntry> {

	public QuestLog() {
		super();
	}
	
	/**
	 * Obtain an array of the keys (quest names) from the quest log.  There is 
	 * no expectation of any particular ordering of the keys.
	 * 
	 * @return The array of keys (quest names) from the quest log.
	 */
	public String[] keys() {
		String[] keys = new String[count];
		int keyIndex = 0;

		// for every index in the hashArray, check for data items
		for (int i = 0; i < this.hashArray.length; i++) {
			// if this index holds a valid non-empty linked list, store all the keys into the keys array
			if ( this.hashArray[i] != null && !this.hashArray[i].isEmpty() ) {
				// start at the beginning of the linked list
				this.hashArray[i].goFirst();
				// save every key in the linked list
				while (this.hashArray[i].itemExists()) {
					// prevent array out of bounds exception (should never happen)
					if (keyIndex >= count) return keys;
					keys[keyIndex++] = this.hashArray[i].item().questName;
					this.hashArray[i].goForth();
				}
			}
		}

		return keys;
	}


	/**
	 * Format the quest log as a string which displays the quests in the log in 
	 * alphabetical order by name.
	 * 
	 * @return A nicely formatted quest log.
	 */
	public String toString() {
		// Create, fill, and sort an array of all of the keys in the Quest Log
		String[] questNames = this.keys();
		Arrays.sort(questNames);

		String fullQuestLog = "";

		// iterate through every quest
		for ( String questName : questNames ) {
			// obtain the quest from the quest log
			QuestLogEntry quest = this.obtain(questName);

			// obtain the quest details
			String questArea = quest.getQuestArea();
			int questMinLvl = quest.getRecommendedMinLevel();
			int questMaxLvl = quest.getRecommendedMaxLevel();

			// append the details to the full Quest Log
			fullQuestLog += "\n" + questName + " : " + questArea + ", Level Range: " + questMinLvl + "-" + questMaxLvl;
		}

		// return the full quest log
		fullQuestLog += "\n";
		return fullQuestLog;
	}


	/**
	 * Obtain the quest with name k, while simultaneously returning the number of
	 * items examined while searching for the quest.
	 * @param k Name of the quest to obtain.
	 * @return A pair in which the first item is the QuestLogEntry for the quest named k, and the
	 *         second item is the number of items examined during the search for the quest named k.
	 *         Note: if no quest named k is found, then the first item of the pair should be null.
	 */
	public Pair280<QuestLogEntry, Integer> obtainWithCount(String k) {
		Pair280 pair = new Pair280(null, 0);
		int itemsChecked = 0;

		/* search code copied from KeyedChainedHashTable280 */
		int itemHashLocation = this.hashPos(k);
		if (searchesContinue && itemListLocation != null) {
			goForth();
		} else {
			if (hashArray[itemHashLocation] == null)
				hashArray[itemHashLocation] = newChain();
			itemListLocation = hashArray[itemHashLocation].iterator();
		}

		itemsChecked++;
		while (!itemListLocation.after() && k.compareTo(itemListLocation.item().key()) != 0 ) {
			itemListLocation.goForth();
			itemsChecked++;
		}

		// update the pair and return it
		if (itemExists())
			pair.setFirstItem(item());
		pair.setSecondItem(itemsChecked);
		return pair;
	}
	
	
	public static void main(String args[])  {
		// Make a new Quest Log
		QuestLog hashQuestLog = new QuestLog();
		
		// Make a new ordered binary lib280.tree.
		OrderedSimpleTree280<QuestLogEntry> treeQuestLog =
				new OrderedSimpleTree280<QuestLogEntry>();
		
		
		// Read the quest data from a CSV (comma-separated value) file.
		// To change the file read in, edit the argument to the FileReader constructor.
		CSVReader inFile;
		try {
			//input filename on the next line - path must be relative to the working directory reported above.
			inFile = new CSVReader(new FileReader("../../../git-repos/CMPT280/a5/QuestLog-Template/quests100000.csv"));
		} catch (FileNotFoundException e) {
			System.out.println("Error: File not found.");
			return;
		}
		
		String[] nextQuest;
		try {
			// Read a row of data from the CSV file
			while ((nextQuest = inFile.readNext()) != null) {
				// If the read succeeded, nextQuest is an array of strings containing the data from
				// each field in a row of the CSV file.  The first field is the quest name,
				// the second field is the quest region, and the next two are the recommended
				// minimum and maximum level, which we convert to integers before passing them to the
				// constructor of a QuestLogEntry object.
				QuestLogEntry newEntry = new QuestLogEntry(nextQuest[0], nextQuest[1], 
						Integer.parseInt(nextQuest[2]), Integer.parseInt(nextQuest[3]));
				// Insert the new quest log entry into the quest log.
				hashQuestLog.insert(newEntry);
				treeQuestLog.insert(newEntry);
			}
		} catch (IOException e) {
			System.out.println("Something bad happened while reading the quest information.");
			e.printStackTrace();
		}
		
//		// Print out the hashed quest log's quests in alphabetical order.
//		System.out.println(hashQuestLog);
//
//		// Print out the lib280.tree quest log's quests in alphabetical order.
//	    System.out.println(treeQuestLog.toStringInorder());

		/* Testing Hashed QuestLog */
		Pair280 results = new Pair280(null, 0);
		results.setFirstItem(null);
		results.setSecondItem(0);
		int questCount = 0;
		int searchesTotal = 0;
		double searchesAverage = 0.0;

		for (String key : hashQuestLog.keys()) {
			results = hashQuestLog.obtainWithCount(key);
			if (results.firstItem() != null) {
				questCount++;
				searchesTotal += (int)results.secondItem();
			}
		}

		searchesAverage = ((double)searchesTotal / (double)questCount);
		System.out.println("Hashed Quest Log: Avg items examined per query, with " + hashQuestLog.count() + " entries: " + searchesAverage);


		/* Testing lib280.tree quest log */
		results.setFirstItem(null);
		results.setSecondItem(0);
		questCount = 0;
		searchesTotal = 0;

		treeQuestLog.rootItem();
		for (String key : hashQuestLog.keys()) {
			results = hashQuestLog.obtainWithCount(key);
			if (results.firstItem() != null) {
				questCount++;
				searchesTotal += treeQuestLog.searchCount((QuestLogEntry) results.firstItem());
			}
		}

		searchesAverage = ((double)searchesTotal / (double)questCount);
		System.out.println("Tree Quest Log: Avg items examined per query, with " + hashQuestLog.count() + " entries: " + searchesAverage);

	}
	
	
}

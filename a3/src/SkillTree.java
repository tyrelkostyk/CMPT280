import lib280.tree.ArrayedBinaryTreeWithCursors280;
import lib280.tree.BasicMAryTree280;

public class SkillTree extends BasicMAryTree280<Skill> {

	/**
	 * Create lib280.tree with the specified root node and
	 * specified maximum arity of nodes.  
	 * @timing O(1) 
	 * @param x item to set as the root node
	 * @param m number of children allowed for future nodes 
	 */
	public SkillTree(Skill x, int m)
	{
		super(x,m);
	}

	/**
	 * A convenience method that avoids typecasts.
	 * Obtains a subtree of the root.
	 * 
	 * @param i Index of the desired subtree of the root.
	 * @return the i-th subtree of the root.
	 */
	public SkillTree rootSubTree(int i) {
		return (SkillTree)super.rootSubtree(i);
	}


	public static void main(String[] args) {
		// root skill
		Skill superpowers = new Skill("Superpowers", "Access to all other super-powers", 5);
		SkillTree superPowersTree = new SkillTree(superpowers, 4);

		// first (non-root) level skill
		Skill power = new Skill("Power", "Strength and control over the outside world", 8);
		SkillTree powerTree = new SkillTree(power, 2);
		// second (non-root) level skill
		Skill mindControl = new Skill("Mind Control", "The ability to control the thoughts and actions of another", 15);
		SkillTree mindControlTree = new SkillTree(mindControl, 2);

		// first (non-root) level skill
		Skill love = new Skill("Love", "Manipulation and charisma", 8);
		SkillTree loveTree = new SkillTree(love, 2);
		// second (non-root) level skill
		Skill charm = new Skill("Charm", "The ability to charm someone, so they will lose desire to oppose you", 12);
		SkillTree charmTree = new SkillTree(charm, 2);

		// first (non-root) level skill
		Skill wisdom = new Skill("Wisdom", "Intelligence and psychic ability", 8);
		SkillTree wisdomTree = new SkillTree(wisdom, 2);
		// second (non-root) level skills
		Skill telekinesis = new Skill("Telekinesis", "The ability to move (inanimate) objects with one's mind", 15);
		SkillTree telekinesisTree = new SkillTree(telekinesis, 2);
		Skill mindReading = new Skill("Mind Reading", "The ability to read the thoughts of another person", 15);
		SkillTree mindReadingTree = new SkillTree(mindReading, 2);

		// first (non-root) level skill
		Skill freedom = new Skill("Freedom", "Detachment from Earthly constraints", 10);
		SkillTree freedomTree = new SkillTree(freedom, 2);
		// second (non-root) level skills
		Skill invisibility = new Skill("Invisibility", "The ability to turn completely invisible", 15);
		SkillTree invisibilityTree = new SkillTree(invisibility, 2);
		Skill flight = new Skill("Flight", "The ability to hover and fly with no propellant", 20);
		SkillTree flightTree = new SkillTree(flight, 2);

		// set first level of skill tree
		superPowersTree.setRootSubtree(powerTree, 1);
		superPowersTree.setRootSubtree(loveTree, 2);
		superPowersTree.setRootSubtree(wisdomTree, 3);
		superPowersTree.setRootSubtree(freedomTree, 4);

		// set second level of skill tree
		powerTree.setRootSubtree(mindControlTree, 1);
		loveTree.setRootSubtree(charmTree, 1);
		wisdomTree.setRootSubtree(telekinesisTree, 1);
		wisdomTree.setRootSubtree(mindReadingTree, 2);
		freedomTree.setRootSubtree(flightTree, 1);
		freedomTree.setRootSubtree(invisibilityTree, 2);

		// print the skill tree
		System.out.print(superPowersTree.toStringByLevel());
	}
}

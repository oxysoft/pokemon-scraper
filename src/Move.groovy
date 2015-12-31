/**
 * Created by oxysoft on 28-12-2015.
 */
class Move {
	int id
	PokeType type
	MoveCategory category
	ContestType contest
	String name, description

	// Varies for each generation
	Map<Integer, Integer> powers = [:], pps = [:], accs = [:]


	@Override
	public String toString() {
		return "Move{$id, $name, $type, $category, $description, $contest, $pps, $powers, $accs}";
	}
}

/**
 * Created by oxysoft on 28-12-2015.
 */
class Data {
	static Map<Integer, Pokemon> pokemons = [:]
	static Map<Integer, Ability> abilities = [:]
	static Map<Integer, Map<String, Move>> moves = [:]

	static Ability abilityByName(String s) {
		abilities.find { it.value.name == s }.value
	}

	static Move moveByName(int gen, String s) {
		moves[gen][s]
	}

	static Move moveByName(String s) {
		moveByName(6, s)
	}
}

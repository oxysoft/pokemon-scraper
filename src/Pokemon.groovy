/**
 * Created by oxysoft on 28-12-2015.
 */
class Pokemon {
	String name, specie, nameJap
	int id
	double height, weight // meters, lbs
//	List<Integer> regionalIds = [-1, -1, -1, -1, -1, -1] //kanto, johto, hoenn, sinnoh, unova, kalos
	List<String> localIdStrings = []
	int baseExp, catchRate, hatchSteps, baseHappiness
	List<EggGroup> eggGroups
	int eggCycles
	List<Double> genderRates = [0.0, 0.0]
	Stats stats, statsmin, statsmax, evs
	List<Learnset> learnsets = [null, null, null, null, null, null]
	List<Ability> abilities = [null, null, null]
	List<PokeType> types = []

	Pokemon() {
		stats = new Stats()
		statsmin = new Stats()
		statsmax = new Stats()
		evs = new Stats()
	}

}

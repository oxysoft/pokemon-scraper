/**
 * Created by oxysoft on 30-12-2015.
 */
enum EggGroup {
	Monster,
	Water1,
	Bug,
	Flying,
	Field,
	Fairy,
	Grass,
	HumanLike,
	Water3,
	Mineral,
	Amorphous,
	Water2,
	Ditto,
	Dragon,
	Undiscovered,

	public static EggGroup byName(String name) {
		values().find { it.name() == name }
	}
}
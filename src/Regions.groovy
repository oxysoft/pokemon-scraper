/**
 * Created by oxysoft on 28-12-2015.
 */
enum Region {
	Kanto(0),
	Johto(1),
	Hoenn(2),
	Sinnoh(3),
	Unova(4),
	Kalos(5);

	int id;

	private Region(int id) {
		this.id = id
	}

	public static Region byId(int id) {
		values().find { it.id == id }
	}

	public static Region byName(String name) {
		values().find { it.name().equalsIgnoreCase(name) }
	}

}
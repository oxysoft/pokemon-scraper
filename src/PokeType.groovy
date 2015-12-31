/**
 * Created by oxysoft on 28-12-2015.
 */
enum PokeType {
	Normal(),
	Fire(),
	Fighting(),
	Water(),
	Flying(),
	Grass(),
	Poison(),
	Electric(),
	Ground(),
	Psychic(),
	Rock(),
	Ice(),
	Bug(),
	Dragon(),
	Ghost(),
	Dark(),
	Steel(),
	Fairy(),
	Wtf(); // question mark type i guess?

	private PokeType() {
	}

	public static PokeType byName(String name) {
		def ret = values().find { name.equalsIgnoreCase(it.name()) }

		if (!ret)
			Wtf

		ret
	}
}
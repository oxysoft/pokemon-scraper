/**
 * Created by oxysoft on 31-12-2015.
 */
enum MoveCategory {
	Physics(0),
	Special(1),
	Status(2)

	public final int id

	private MoveCategory(int id) {
		this.id = id
	}

	static MoveCategory byName(String s) {
		values().find { it.name() == s }
	}
}
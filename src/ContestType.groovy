/**
 * Created by oxysoft on 31-12-2015.
 */
enum ContestType {
	Cool(0),
	Beauty(1),
	Cute(2),
	Clever(3),
	Tough(4)

	public final int id

	private ContestType(int id) {
		this.id = id
	}

	static ContestType byName(String s) {
		values().find { it.name() == s }
	}
}
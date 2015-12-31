/**
 * Created by oxysoft on 28-12-2015.
 */
class Stats {
	int hp, atk, dfs, spatk, spdfs, spd

	Stats() {
		this.hp = 0;
		this.atk = 0;
		this.dfs = 0;
		this.spatk = 0;
		this.spdfs = 0;
		this.spd = 0;
	}

	Stats(int hp, int atk, int dfs, int spatk, int spdfs, int spd) {
		this.hp = hp;
		this.atk = atk;
		this.dfs = dfs;
		this.spatk = spatk;
		this.spdfs = spdfs;
		this.spd = spd;
	}

	void add(String name, int v) {
		name = name.toLowerCase()

		if (!name.contains("special") && !name.contains("sp.")) {
			if (name == "hp")
				hp += v
			else if (name == "attack" || name == "atk")
				atk += v
			else if (name == "defense" || name == "def")
				dfs += v
			else if (name == "speed" || name == "spd")
				spd += v
		} else {
			name = name.split(" ")[-1]
			if (name == "attack" || name == "atk")
				spatk += v
			else if (name == "defense" || name == "def")
				spdfs += v
		}
	}

	public int getTotal() {
		hp + atk + dfs + spatk + spdfs + spd
	}

	@Override
	public String toString() {
		return "Stats[$hp, $atk, $dfs, $spatk, $spdfs, $spd; total: $total]";
	}
}

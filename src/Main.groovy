import org.jsoup.HttpStatusException
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements

/**
 * Created by oxysoft on 28-12-2015.
 */
public class Main {
	public static String URL_LIST = "http://pokemondb.net/pokedex/national"
	public static String URL_ABILITIES = "http://bulbapedia.bulbagarden.net/wiki/Ability"
	public static String URL_MOVES = "http://bulbapedia.bulbagarden.net/wiki/List_of_moves"
	public static String URL_MOVES_DESC = "http://pokemondb.net/move/all"

	public static void main(String[] args) {
//		scrapAbilities()
//		scrapPokemons()
		scrapMoves()
	}

	static void scrapAbilities() {
		Document doc = Jsoup.connect(URL_ABILITIES).get()
		Elements cols = doc.select(".sortable > tbody > tr > td")

		Ability cur

		cols.eachWithIndex { Element col, int i ->
			String cc = col.text()

			switch (i % 7) {
				case 0:
					if (cur != null)
						Data.abilities[cur.id] = cur

					cur = new Ability()

					try {
						cur.id = Integer.parseInt(cc)
					} catch (ignored) {
						cur = null
					}
					break
				case 1:
					cur?.name = cc
					break
				case 2:
					cur?.desc = cc
					break
				case 3:
					cur?.gen = Roman.toInt(cc)
					break
				case 4:
				case 5:
				case 6:
					// we don't care about these informations tbh kinda useless
					break
			}
		}

		Data.abilities = Data.abilities.sort { it.key }
	}

	static void scrapMoves() {
		Document doc = Jsoup.connect(URL_MOVES).get()

		Elements cells = doc.select("table")[0].select("td")[1..-1]

		Move move = null

		for (int i = 0; i < cells.size(); i++) {
			Element td = cells[i]
			String cell = cells[i].text().trim()

			switch (i % 9) {
				case 0:
					move = new Move()
					move.id = cell.toInteger()
					break
				case 1:
					move.name = cell.replace("*", "")
					break
				case 2:
					move.type = PokeType.byName(cell)
					break
				case 3:
					move.category = MoveCategory.byName(cell)
					break
				case 4:
					move.contest = ContestType.byName(cell)
					break
				case 5:
					parseVariableGenProperty(td, move.pps)
					break
				case 6:
					if (td.text() != "*")
						parseVariableGenProperty(td, move.powers)
					break
				case 7:
					parseVariableGenProperty(td, move.accs)
					break
				case 8:
					// the generation it was added in, don't care!
					Data.moves[move.name] = move
					break
			}
		}

		// descriptions that we grab from here
		doc = Jsoup.connect(URL_MOVES_DESC).get()
		cells = doc.select("td")
		final int nCols = 9

		for (int i = 0; i < Data.moves.size(); i++) {
			String name = cells[i * nCols].text()
			String desc = cells[i * nCols + 7].text()

			Data.moves[name].description = desc
			println Data.moves[name]
		}

		null
	}

	static def scrapPokemonLinks() {
		Document doc = Jsoup.connect(URL_LIST).get()
		def links = []

		doc.select(".ent-name").each { Element it ->
			String link = "http://pokemondb.net" + it.attr("href")

			if (links.contains(link))
				return

			links << link
		}

		links
	}

	static void scrapPokemons() {
		List<String> links = scrapPokemonLinks()

		int ii = 0
		links.each {
			boolean done = false
			int i = 0

			if (ii++ == 1)
				System.exit(0)

			while (!done) {
				try {
					Document doc = Jsoup.connect(it).get()
					scrapPokemon(doc)
					done = true
				} catch(HttpStatusException e) {
					// PokemonDB doesn't let us go super duper fast, we get 403 sometime
					println "Error: $e"
					Thread.sleep(500)
				}
			}
		}
	}

	static double parseHeight(String text) {
		Double.parseDouble text[
			text.indexOf('(') + 1
				..
			text.indexOf(')') - 2
		]
	}

	static double parseWeight(String text) {
		Double.parseDouble text[
		    0
				..
		    text.indexOf(' ') - 1
		]
	}

	static void parseVariableGenProperty(Element elem, Map<Integer, Number> props) {
		int val = 0
		String cell = elem.text().replace("%", "")

		if (cell.contains("—")) {
			val = -1
		} else if (cell.contains("*")) {
			elem.getElementsByTag("span")[0].attr("title").split(", ").each {
				try {
					List<Integer> vals = parseValueGenRange(it.replace("%", ""))

					vals[1 .. -1].each {
						props[it] = vals[0]
					}
				} catch(ignored) {
				}
			}

			val = cell.replace("*", "").toInteger()
		} else {
			val = cell.toInteger()
		}

		props[6] = val
	}

	static List<Integer> parseValueGenRange(String text) {
		def tokens = text.split(" ")
		int v = tokens[0].toDouble().toInteger()
		def gens = tokens[-1].split("-").collect { Roman.toInt(it) }

		if (gens.size() > 1)
			gens = [gens[0] .. gens[1]]

		[v, gens].flatten() as List<Integer>
	}

	static List<Double> parseGenderRates(String text) {
		text.split(", ").collect {
			Double.parseDouble it.split(" ")[0][0..-2]
		}
	}

	static Pokemon scrapPokemon(Document doc) {
		Pokemon p = new Pokemon()
		int i = 0

		Elements td = doc.select("td")

		p.name = doc.select("h1").text()
		p.id = Integer.parseInt(td[0].text())
		println "Parsing #${p.id}, ${p.name} ..."

		p.specie = td[2].text()
		p.height = parseHeight td[3].text()
		p.weight = parseWeight td[4].text()
		p.nameJap = td[7].text()
		p.catchRate = Integer.parseInt td[9].text().split(" ")[0]
		p.baseHappiness = Integer.parseInt td[10].text().split(" ")[0]
		p.baseExp = Integer.parseInt td[11].text()
//		p.baseExp = Integer.parseInt td[12].text() // growth rate
		p.eggGroups = td[13].text().split(", ").collect { EggGroup.byName(it) }
		p.genderRates = parseGenderRates td[14].text()
		p.eggCycles = Integer.parseInt td[15].text().split(" ")[0]

		// types
		i = 0
		td[1].select("a").each {
			p.types[i] = PokeType.byName(it.text())
			i++
		}

		// abilities
		i = 0
		td[5].select("a").each {
			if (it.parent().tagName() == "small") {
				p.abilities[2] = Data.abilityByName(it.text()) // hidden ability is always in third slot
			} else {
				p.abilities[i] = Data.abilityByName(it.text())
			}

			i++
		}

		// regional ids
		td[6].text().tokenize(')').each {
			p.localIdStrings << it.trim() + ")"
		}

		// Evs
		td[8].text().split(', ').each {
			def toks = it.split(" ")
			int v = Integer.parseInt toks[0]
			String stat = toks[1 .. -1].join(" ")
			p.evs.add(stat, v)
		}

		// base stats
		def strs = [
			"hp",
			"attack",
			"defense",
			"Sp. atk",
			"Sp. def",
			"speed",
		]

//		for (i = 16; i < 75; i++) {
//			println td[i]
//		}

		for (int x = 0; x < 6; x++) {
			String strStat = strs[x]

			for (int y = 1; y <= 4; y++) {
				if (y == 2)
					continue

				int id = 16 + x*4+y;
//				println([td[id].text(), id, x, y])
				int val = Integer.parseInt(td[id].text())

				if (y == 1) {
					p.stats.add(strStat, val)
				} else if (y == 3) {
					p.statsmin.add(strStat, val)
				} else if (y == 4) {
					p.statsmax.add(strStat, val)
				}
			}
		}

		// learnsets
		doc.select(":nth-child(14) .gen").each {
			String href = it.attr("href")

//			p.learnsets[(href[-1] as int) - 1] =
			scrapLearnsets(href)
		}

		p
	}

	static List<Learnset> scrapLearnsets(String href) {
		Document doc = Jsoup.connect("http://pokemondb.net$href").get()

		int c = doc.getElementsByClass("svtabs-tab").size()
		Elements panels = doc.select(".svtabs-panel")

		def types = [
		    "level up",
		    "hm",
		    "tm",
		    "egg",
		    "tutor",
		]

		panels.each {
			Learnset ls

			it.children()[0].children().each {
				int type = -1

				it.select("h2,table").each {
					if (it.tagName() == "h2") {
						String t = it.text().toLowerCase()
						type = types.indexOf(types.find { t.contains it })
					} else if (it.tagName() == "table") {
						int i = 0

						int level = -1
						String move = ""

						it.select("td").each {
							String cell = it.text()

							switch (type) {
								case -1:
									println "Warning: unhandled type!"
									break
								case 0:
									switch (i % 6) {
										case 0:
											level = cell.toInteger()
											break
										case 1:
											move = cell

											println "Learns $move at lv. $level!"
											break
									}
									break
								case 1:
									break
								case 2:
									break
								case 3:
									break
								case 4:
									break
							}

							i++
						}
					}
				}
			}
		}

		null
	}
}

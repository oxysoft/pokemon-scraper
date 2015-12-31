/**
 * Created by oxysoft on 28-12-2015.
 */
class Roman {
	static Map<Integer, String> nums = [
		1 : "I",
		2 : "II",
		3 : "III",
		4 : "IV",
		5 : "V",
		6 : "VI"
	]

	static int toInt(String roman) {
		nums.find { it.value == roman }.key
	}

	static String fromInt(int i) {
		nums[i]
	}
}

package OutCompare;


public class OutCompare {
	public static boolean compare(String origOutStr, String seededOutStr){
		String[] origOutList = origOutStr.split("\n");
		String[] seededOutList = seededOutStr.split("\n");
		for(int i=0; i<origOutList.length; i++){
			String regex = "\\(\\w+\\.java\\:\\d+\\)";
			
			String orig = origOutList[i];
			orig = orig.replaceAll(regex, "");
			orig = orig.trim();
			
			String seeded = seededOutList[i];
			seeded = seeded.replaceAll(regex, "");
			seeded = seeded.trim();
			
			if(!orig.equals(seeded)){
				return false;
			}
		}
		return true;
	}
}


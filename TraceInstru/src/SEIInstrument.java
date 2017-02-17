import java.io.*;
/* The counter class 
 */
public class SEIInstrument
{
	public static String basicDir = "E:\\IJSEKEExperiment\\";
	public static String traceOut = "seededTraceOut.txt";
	public static synchronized void recordStm(int lineNumber, String className){
		try{
			File traceFile = new File(basicDir + traceOut);
			BufferedWriter traceBW = new BufferedWriter(new FileWriter(traceFile, true));
			traceBW.write(className + "_" + lineNumber + "\n");
			traceBW.close();
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}
//	/* the counter array list to store executing profile*/
//	private static ArrayList<String> traceList = new ArrayList<String>();
//	private static HashMap<String, String> traceMap = new HashMap<String, String>();
//
//	public static synchronized void increase(int lineNum, String className){
//		String keyStr = className + ":" + lineNum;
//		
//		traceList.add(keyStr);
//		
//		if(traceMap.containsKey(keyStr)){
//			int intNum = Integer.parseInt(traceMap.get(keyStr));
//			intNum = intNum + 1;
//			String strNum = Integer.toString(intNum);
//			traceMap.put(keyStr, strNum);
//		}
//		else{
//			String strNum = Integer.toString(1);
//			traceMap.put(keyStr, strNum);
//		}
//	}
//	
//	/**
//	 * reports the array list to "stmtCountResult.txt".
//	 */
//	public static synchronized void report(){
//		try{
//			File file = new File("E:\\stmtCountList.txt");
//			BufferedWriter bw = new BufferedWriter(new FileWriter(file));
//			for(int i=0; i<traceList.size(); i++){
//				String str = traceList.get(i);
//				bw.write(str + "\n");
//			}
//			bw.close();
//		    file = new File("E:\\stmtCountMap.txt");
//			bw = new BufferedWriter(new FileWriter(file));
//			Iterator<Entry<String, String>> iter = traceMap.entrySet().iterator();
//			while(iter.hasNext()){
//				Entry<String, String> entry = iter.next();
//				String keyStr = entry.getKey();
//				String valueStr = entry.getValue();
//				bw.write(keyStr + "&" + valueStr + "\n");
//			}
//			bw.close();
//		}
//		catch(IOException e){
//			e.printStackTrace();
//		}		 
//	}
}

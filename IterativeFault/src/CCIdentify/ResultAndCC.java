package CCIdentify;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;

import Configure.Configure;
import OutCompare.OutCompare;

public class ResultAndCC {
	public static String basicDir = "E:\\IJSEKEExperiment\\";
	public static String seededTraceOut = "seededTraceOut.txt";
	public static String origOut = "origOut.txt";
	public static String seededOut = "seededOut.txt";
	public static String exeOrder = "exeOrder.txt";
	public static String result = "result.txt";
	// The faulty Statements
	public static String faultStmt = null;
	
	public static void run(){
		faultStmt = Configure.getFaultStmt();
		preprocess();
		identify();
	}
	
	public static void preprocess(){
		File resultFile = new File(basicDir + result);
		if(resultFile.exists()){
			resultFile.delete();
		}
	}
	
	public static void identify(){
		//Faulty Statements
		String[] faultStmts = faultStmt.split("#");
		
		try{
			File resultFile = new File(basicDir + result);
			BufferedWriter resultBW = new BufferedWriter(new FileWriter(resultFile));
			
			File exeOrderFile = new File(basicDir + exeOrder);
			File origOutFile = new File(basicDir + origOut);
			File seededOutFile = new File(basicDir + seededOut);
			File seededTraceOutFile = new File(basicDir + seededTraceOut);
			
			BufferedReader origOutBR = new BufferedReader(new FileReader(origOutFile));
			BufferedReader seededOutBR = new BufferedReader(new FileReader(seededOutFile));
			BufferedReader exeOrderBR = new BufferedReader(new FileReader(exeOrderFile));
			BufferedReader seededTraceOutBR = new BufferedReader(new FileReader(seededTraceOutFile));
	
			while(true){
				String eachInput = exeOrderBR.readLine();
				if(eachInput != null){
					resultBW.write(eachInput + "#" + "used");
					// comparing whether the outputs are same
					StringBuilder origOutStr = new StringBuilder();
					StringBuilder seededOutStr = new StringBuilder();
					while(true){
						String eachOrigOut = origOutBR.readLine();
						if(eachOrigOut != null && !eachOrigOut.contains("*****")){
							origOutStr.append(eachOrigOut);
						}
						else{
							break;
						}
					}
					while(true){
						String eachSeededOut = seededOutBR.readLine();
						if(eachSeededOut != null && !eachSeededOut.contains("*****")){
							seededOutStr.append(eachSeededOut);
						}
						else{
							break;
						}
					}
					//System.out.println(eachInput+"#orig#"+origOutStr.toString()+"\n"+eachInput+"#seeded#"+seededOutStr.toString());
					boolean PFIndex = OutCompare.compare(origOutStr.toString(), seededOutStr.toString());
					if(PFIndex == true){
						resultBW.write("#" + "passed");
					}
					else{
						resultBW.write("#" + "failed");
					}
					//identify whether the test is CC
					HashSet<String> traceSet = new HashSet<String>();
					while(true){
						String eachTrace = seededTraceOutBR.readLine();
						if(eachTrace != null && !eachTrace.contains("*****")){
							traceSet.add(eachTrace);
						}
						else{
							break;
						}
					}
					boolean CCIndex = false;
					for(int i=0; i<faultStmts.length; i++){
						if(traceSet.contains(faultStmts[i])){
							CCIndex = true;
							break;
						}
					}
					if(CCIndex == true && PFIndex == true){
						resultBW.write("#" + "CC" + "\n");
					}
					else{
						resultBW.write("#" + "NoCC" + "\n");
					}
				}
				else{
					break;
				}
			}
			origOutBR.close();
			seededOutBR.close();
			exeOrderBR.close();
			seededTraceOutBR.close();
			resultBW.close();
		}
		catch(IOException e){
			e.printStackTrace();
		}
		
	}
	
}

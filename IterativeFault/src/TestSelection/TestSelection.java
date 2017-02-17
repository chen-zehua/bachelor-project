package TestSelection;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class TestSelection {
	public static String basicDir = "E:\\IJSEKEExperiment\\";
	public static String result = "result.txt";
	public static String clusterResult = "clusterResult.txt";
	
	public static void run(){
		preprocess();
		testSelective();
	}
	
	public static void preprocess(){
		File resultFile = new File(basicDir + result);
		if(resultFile.exists()){
			resultFile.delete();
		}
	}
	
	public static void testSelective(){
		ArrayList<String> allInputs = new ArrayList<String>(); 
		//ArrayList<String> allInputs1 = new ArrayList<String>();
		HashMap<String,Integer> classCounter = new HashMap<String,Integer>();
		try{
			File clusterResultFile = new File(basicDir + clusterResult);
			BufferedReader clusterResultBR = new BufferedReader(new FileReader(clusterResultFile));
			while(true){
				String eachInput = clusterResultBR.readLine();
				if(eachInput != null){
					allInputs.add(eachInput);
					String[] inputItem = eachInput.split("#");
					if(!classCounter.containsKey(inputItem[4])&&inputItem[2].equals("failed"))
						classCounter.put(inputItem[4],1);					
					else if(inputItem[2].equals("failed")){
						int temp=classCounter.get(inputItem[4]);
						classCounter.remove(inputItem[4]);
						classCounter.put(inputItem[4], temp+1);
					}
				}
				else{
					break;
				}
			}
			clusterResultBR.close();
		}
		catch(IOException e){
			e.printStackTrace();
		}
		String ccClass=null;
		int c1=0;
		int c0=0;
		if(classCounter.containsKey("1.0"))
			c1=classCounter.get("1.0");
		if(classCounter.containsKey("0.0"))
			c0=classCounter.get("0.0");		
		if(c1>c0)
			ccClass="1.0";
		else {
			ccClass="0.0";
		}
		System.out.println("#"+ccClass+"#");
		
		try{
			File resultFile = new File(basicDir + result);
			BufferedWriter resultBW = new BufferedWriter(new FileWriter(resultFile));
			for(int i=0; i<allInputs.size(); i++){
				String eachInput = allInputs.get(i);
				String[] inputItem = eachInput.split("#");
				if(inputItem[4].equals(ccClass)&&inputItem[2].equals("passed")){
					inputItem[1] = "unused";
				}
				resultBW.write(inputItem[0] + "#" + inputItem[1] + "#" + inputItem[2] + "#" + inputItem[3] + "#" + inputItem[4] + "\n");
			}
			resultBW.close();
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}
	
}

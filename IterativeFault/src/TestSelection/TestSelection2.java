package TestSelection;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class TestSelection2 {
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
		try{
			File clusterResultFile = new File(basicDir + clusterResult);
			BufferedReader clusterResultBR = new BufferedReader(new FileReader(clusterResultFile));
			while(true){
				String eachInput = clusterResultBR.readLine();
				if(eachInput != null){
					allInputs.add(eachInput);
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
		
		HashMap<String, String> classifierPF = new HashMap<String, String>();
		for(int i=0; i<allInputs.size(); i++){
			String eachInput = allInputs.get(i);
			String[] inputItem = eachInput.split("#");
			if(classifierPF.containsKey(inputItem[4])){
				if(inputItem[2].equals("failed")){
					classifierPF.put(inputItem[4], inputItem[2]);
				}
			}
			else{
				classifierPF.put(inputItem[4], inputItem[2]);
			}
		}
		
		try{
			File resultFile = new File(basicDir + result);
			BufferedWriter resultBW = new BufferedWriter(new FileWriter(resultFile));
			for(int i=0; i<allInputs.size(); i++){
				String eachInput = allInputs.get(i);
				String[] inputItem = eachInput.split("#");
				if(classifierPF.containsKey(inputItem[4])){
					String classType = classifierPF.get(inputItem[4]);
					if(classType.equals("failed")){
						if(inputItem[2].equals("passed")){
							inputItem[1] = "unused";
						}
					}
				}
				else{
					System.out.println("Classify Error!");
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

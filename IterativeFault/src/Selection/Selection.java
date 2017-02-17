package Selection;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Selection {
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
		
		try{
			File resultFile = new File(basicDir + result);
			BufferedWriter resultBW = new BufferedWriter(new FileWriter(resultFile));
			for(int i=0; i<allInputs.size(); i++){
				String eachInput = allInputs.get(i);
				String[] inputItem = eachInput.split("#");
				if(inputItem[3].equals("CC")){
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

package FaultLocalization;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import Configure.Configure;


public class FaultLocalization {
	
	public static String basicDir = "E:\\IJSEKEExperiment\\";
	public static String result = "result.txt";
	public static String seededTraceOut = "seededTraceOut.txt";
	public static String sus = "suspicious.txt";

	public static HashMap<String, Integer> passedFrequency = null;
	public static HashMap<String, Integer> failedFrequency = null;
	public static HashMap<String, Double> suspiciousMap = null;
	public static String faultStmt = null;
	public static HashSet<String> allElement =null;
	
	public static int totalPassed = 0;
	public static int totalFailed = 0;
	public static double entropy = 0.0;
	
	public static double run(){

		preprocess();
		elementSpectrum();
		Tarantula();
		printSuspicious();
//		entropyCal();
		return entropy;
	}
	
	public static void preprocess(){
		faultStmt = Configure.getFaultStmt();
		passedFrequency = new HashMap<String, Integer>();
		failedFrequency = new HashMap<String, Integer>();
		suspiciousMap = new HashMap<String, Double>();
		allElement= new HashSet<String>();
		
		totalPassed = 0;
		totalFailed = 0;
		entropy = 0.0;
		
		File susFile = new File(basicDir + sus);
		if(susFile.exists()){
			susFile.delete();
		}
	}
	
	public static void elementSpectrum(){
		try{
			File exeOrderFile = new File(basicDir + result);
			File seededTraceOutFile = new File(basicDir + seededTraceOut);
			BufferedReader exeOrderBR = new BufferedReader(new FileReader(exeOrderFile));
			BufferedReader seededTraceOutBR = new BufferedReader(new FileReader(seededTraceOutFile));
			while(true){
				String eachInput = exeOrderBR.readLine();
				if(eachInput != null){
					String[] eachElement = eachInput.split("#");
					String PFStr = eachElement[2];
					HashSet<String> elementFrequency = new HashSet<String>();
					while(true){
						String eachTrace = seededTraceOutBR.readLine();
						if(eachTrace != null && !eachTrace.startsWith("*****")){
							elementFrequency.add(eachTrace);
						}
						else{
							break;
						}
					}
					
					//determine whether the test case is used
					if(eachElement[1].equals("used")){
						if(PFStr.contains("passed")){
							totalPassed = totalPassed + 1;
							addFrequency(true, elementFrequency);
						}
						else if(PFStr.contains("failed")){
							totalFailed = totalFailed + 1;
							addFrequency(false, elementFrequency);
						}
					}
				}
				else{
					break;
				}
			}
			exeOrderBR.close();
			seededTraceOutBR.close();
		}
		catch(IOException e){
			e.printStackTrace();
		}
		Set<String> failedElement=failedFrequency.keySet();
		Set<String> passedElement=passedFrequency.keySet();
		allElement.addAll(failedElement);
		allElement.addAll(passedElement);
	}
	
	private static void addFrequency(boolean passed, HashSet<String> elementFrequency) {
		if(passed == true){
			Iterator<String> iter = elementFrequency.iterator();
			while(iter.hasNext()){
				String eachElement = iter.next();
				if(passedFrequency.containsKey(eachElement)){
					int num = passedFrequency.get(eachElement) + 1;
					passedFrequency.put(eachElement, num);
				}
				else{
					passedFrequency.put(eachElement, 1);
				}
			}
		}
		else{
			Iterator<String> iter = elementFrequency.iterator();
			while(iter.hasNext()){
				String eachElement = iter.next();
				if(failedFrequency.containsKey(eachElement)){
					int num = failedFrequency.get(eachElement) + 1;
					failedFrequency.put(eachElement, num);
				}
				else{
					failedFrequency.put(eachElement, 1);
				}
			}
		}
	}
	
	public static void Ochiai(){
		Iterator<Entry<String, Integer>> iter = failedFrequency.entrySet().iterator();
		while(iter.hasNext()){
			Entry<String, Integer> failed = iter.next();
			String failedKey = failed.getKey();
			int failedValue = failed.getValue();
			int passedValue = 0;
			if(passedFrequency.containsKey(failedKey)){
				passedValue = passedFrequency.get(failedKey);
			}
			
			double denominator = Math.sqrt(totalFailed * (failedValue + passedValue));
			double suspicious = failedValue / denominator;
			
			suspiciousMap.put(failedKey, suspicious);
		}
	}
	
	public static void Tarantula(){
		Iterator<String> iter = allElement.iterator();
		while(iter.hasNext()){
			String Key = iter.next();
			int failedValue = 0;			
			int passedValue = 0;
			if(failedFrequency.containsKey(Key)){
				failedValue = failedFrequency.get(Key);
			}
			if(passedFrequency.containsKey(Key)){
				passedValue = passedFrequency.get(Key);
			}
			double suspicious = 1;
			if(totalPassed!=0){				
			double Fe = (double)failedValue / (double)totalFailed;
			double Pe = (double)passedValue / (double)totalPassed;			
			//System.out.println(totalPassed);
			suspicious = Fe / (Fe + Pe);
			}
			suspiciousMap.put(Key, suspicious);
		}
	}
	
	public static void printSuspicious(){
		LinkedList<Map.Entry<String, Double>> entries = sortMap(suspiciousMap); 
		printTScore(entries);
		try{
			File susFile = new File(basicDir + sus);
			BufferedWriter susBW = new BufferedWriter(new FileWriter(susFile));
			for(int i=0; i<entries.size(); i++){
				susBW.write(entries.get(i).getKey() + ":" + entries.get(i).getValue() + "\n");
			}
			susBW.close();
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}

	private static LinkedList<Entry<String, Double>> sortMap(HashMap<String, Double> susMap){
	     List<Map.Entry<String, Double>> entries = new LinkedList<Map.Entry<String, Double>>(susMap.entrySet()); 
	     for(int i=0; i<entries.size()-1; i++){
	    	 for(int j=i+1; j<entries.size(); j++){
	    		 Double value_i = entries.get(i).getValue();
	    		 Double value_j = entries.get(j).getValue();
	    		 if(value_i < value_j){
	    			 Entry<String, Double> temp = entries.get(j);
	    			 entries.set(j, entries.get(i));
	    			 entries.set(i, temp);
	    		 }
	    	 }
	     }
	     return (LinkedList<Entry<String, Double>>)entries;
	}
	
//	public static void printTScore(LinkedList<Map.Entry<String, Double>> entries){			
//
//		int allElementSize = allElement.size();
//		double faultStmtSus=-1;
//		int eExamined=0;
//		
//		String[] faultStmts = faultStmt.split("#");
//		for(int i=0; i<faultStmts.length; i++){
////			System.out.println(eExamined);
//			for(int j=0; j<entries.size(); j++){
//				String eachKey = entries.get(j).getKey();
//				if(eachKey.equals(faultStmts[i])){
//					faultStmtSus=entries.get(j).getValue();
//					eExamined=j;
//					//double tscore = (double)(j+1) / (double)allElementSize;								
////					System.out.println(j);
//					//System.out.println(faultStmts[i] + ": " + (j+1) + ": " + tscore);
//				}
//				if(entries.get(j).getValue()==faultStmtSus){
//					eExamined++;
//				}				
//			}			
//			double tscore = (double)eExamined/ (double)allElementSize;
////			System.out.println(eExamined+"#"+allElementSize+"#"+tscore+"#"+faultStmts[i]);
//			System.out.println(tscore);
//			eExamined=0;
//			faultStmtSus=-1;
//		}
//	}
	
	public static void printTScore(LinkedList<Map.Entry<String, Double>> entries){			

		int allElementSize = allElement.size();
		int eExamined=0;
		int rank=0;
		double sus=2;
		int l=0;
		double faultStmtSus=-1;
		
		String[] faultStmts = faultStmt.split("#");
		for(int i=0; i<faultStmts.length; i++){
//			System.out.println(eExamined);
			for(int j=0; j<entries.size(); j++){
				String eachKey = entries.get(j).getKey();
				if(!(entries.get(j).getValue()==sus)){
					eExamined=j;
				}
				sus=entries.get(j).getValue();
				if(eachKey.equals(faultStmts[i])){
					System.out.println(sus);
					break;
				}
			}
			
			for(int j=0; j<entries.size(); j++){
				String eachKey = entries.get(j).getKey();
				if(eachKey.equals(faultStmts[i])){
					faultStmtSus=entries.get(j).getValue();
					l=j;
					//double tscore = (double)(j+1) / (double)allElementSize;								
	//				System.out.println(j);
					//System.out.println(faultStmts[i] + ": " + (j+1) + ": " + tscore);
				}
				if(entries.get(j).getValue()==faultStmtSus){
					l++;
				}				
			}

			if(sus==0){
				eExamined=allElementSize;
				l=allElementSize;
			}
			rank=(l+eExamined)/2;
			double tscore = (double)rank/ (double)allElementSize;
			System.out.println(eExamined+"#"+l+"#"+rank);
			eExamined=0;
			faultStmtSus=-1;
		}
	}
	
	public static void entropyCal(){
		ArrayList<Double> probabilities = new ArrayList<Double>();
		double totalProbabilities = 0.0;
		Iterator<Entry<String, Double>> iter = suspiciousMap.entrySet().iterator();
		while(iter.hasNext()){
			Entry<String, Double> eachElement = iter.next();
			double value = eachElement.getValue();
			probabilities.add(value);
			totalProbabilities = totalProbabilities + value;
		}
		
		for(int i=0; i<probabilities.size(); i++){
			entropy = entropy - ((probabilities.get(i) / totalProbabilities) * Math.log(probabilities.get(i) / totalProbabilities));
		}
		System.out.println(entropy);
	}
}
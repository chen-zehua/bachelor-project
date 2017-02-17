package Cluster;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

public class DataPreprocess {
	
	public static String basicDir = "E:\\IJSEKEExperiment\\";
	public static String seededTraceOut = "seededTraceOut.txt";
	public static String suspicious = "suspicious.txt";
	public static String cluster = "cluster.arff";
	
	public static ArrayList<String> elementList = null;
	public static ArrayList<Double> suspiciousList = null;
	
	public static void run(){
		preprocess();
		dataProcess();
	}
	
	public static void preprocess(){
		elementList = new ArrayList<String>();
		suspiciousList = new ArrayList<Double>();
		
		File clusterFile = new File(basicDir + cluster);
		if(clusterFile.exists()){
			clusterFile.delete();
		}
	}
	
	public static void dataProcess(){
		try{
			// store the element and its suspicious
			File suspiciousFile = new File(basicDir + suspicious);
			BufferedReader suspiciousBR = new BufferedReader(new FileReader(suspiciousFile));
			while(true){
				String eachSuspicious = suspiciousBR.readLine();
				if(eachSuspicious != null){
					String[] suspiciousItem = eachSuspicious.split(":");
					String element = suspiciousItem[0].trim();
					elementList.add(element);
					String sus = suspiciousItem[1].trim();
					double susDouble = Double.parseDouble(sus);
					suspiciousList.add(susDouble);
				}
				else{
					break;
				}
			}
			suspiciousBR.close();
			
			//create the .arff file
			File clusterFile = new File(basicDir + cluster);
			BufferedWriter clusterBW = new BufferedWriter(new FileWriter(clusterFile));
			clusterBW.write("%ARFF file for the execution profile data with some numeric features\n");
			clusterBW.write("%\n");
			clusterBW.write("@relation 'execution profile'\n");
			
			for(int i=0; i<elementList.size(); i++){
				String element = elementList.get(i);
				clusterBW.write("@attribute " + element + " real\n");
			}
			
			clusterBW.write("\n@data\n%\n% the instances\n%\n");
			
			File seededTraceOutFile = new File(basicDir + seededTraceOut);
			BufferedReader seededTraceOutBR = new BufferedReader(new FileReader(seededTraceOutFile));
			HashSet<String> traceSet = new HashSet<String>();
			while(true){
				String element = seededTraceOutBR.readLine();
				if(element != null){
					if(!element.contains("*****")){
						traceSet.add(element);
					}
					else{
						for(int i=0; i<elementList.size(); i++){							
							String elementCandidate = elementList.get(i);
							double sus = 0.0;
							if(traceSet.contains(elementCandidate)){
								sus = suspiciousList.get(i);
							}
							clusterBW.write(sus + " ");
						}
						clusterBW.write("\n");
						traceSet.clear();
					}
				}
				else{
					break;
				}
			}
			seededTraceOutBR.close();
			clusterBW.close();
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}						
}

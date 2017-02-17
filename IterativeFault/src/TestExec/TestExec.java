package TestExec;

import java.lang.String;
import java.io.BufferedWriter;
import java.io.File;
import java.io.BufferedReader; 
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.Runtime;
import java.io.IOException;
import java.lang.Process;
import java.io.InputStreamReader; 

import Configure.Configure;
import OutCompare.OutCompare;

public class TestExec 
{
	// The name to execute the program
	public static String binName = null;
	
	public static String basicDir = "E:\\IJSEKEExperiment\\";
	public static String origDir = "orig";
	public static String seededDir = "seeded";
	public static String origTraceOut = "origTraceOut.txt";
	public static String seededTraceOut = "seededTraceOut.txt";
	public static String origOut = "origOut.txt";
	public static String seededOut = "seededOut.txt";
	public static String exeOrder = "exeOrder.txt";
	
	public static int numRun = 0;
	
	public static void run(){
		binName = Configure.getBinName();
		preprocess();
		mainProcessDir();
	}

	public static void preprocess(){
		File origTraceOutFile = new File(basicDir + origTraceOut);
		if(origTraceOutFile.exists()){
			origTraceOutFile.delete();
		}
		File seededTraceOutFile = new File(basicDir + seededTraceOut);
		if(seededTraceOutFile.exists()){
			seededTraceOutFile.delete();
		}
		File origOutFile = new File(basicDir + origOut);
		if(origOutFile.exists()){
			origOutFile.delete();
		}
		File seededOutFile = new File(basicDir + seededOut);
		if(seededOutFile.exists()){
			seededOutFile.delete();		
		}
		File exeOrderFile = new File(basicDir + exeOrder);
		if(exeOrderFile.exists()){
			exeOrderFile.delete();
		}
	}
	
	public static void javaRun(String inputs, boolean origIndex){
		try{
			String clPath = null;
			String filePath = null;
			if(origIndex == true){
				clPath = basicDir + origDir;
				filePath = basicDir + origOut;
			}
			else{
				clPath = basicDir + seededDir;
				filePath = basicDir + seededOut;
			}
			String command = "java -classpath " + clPath + " " + binName + " " + inputs;
			//System.out.println(++numRun + ": " + command);
			Process process = Runtime.getRuntime().exec(command);
			BufferedReader error = new BufferedReader(new InputStreamReader(process.getErrorStream(), "gb2312"));
			StringBuilder errorInfo = new StringBuilder();
			BufferedReader output = new BufferedReader(new InputStreamReader(process.getInputStream(), "gb2312"));
			StringBuilder outputInfo = new StringBuilder();
			while(true){
				String info = error.readLine();
				if(info != null){
					errorInfo.append(info + "\n");
				}
				else{
					break;
				}
			}
			error.close();
			
			while(true){
				String info = output.readLine();
				if(info != null){
					outputInfo.append(info + "\n");
				}
				else{
					break;
				}
			}
			
			if(errorInfo.length() != 0){
				File outFile = new File(filePath);
				BufferedWriter outBW = new BufferedWriter(new FileWriter(outFile, true));
				outBW.write(errorInfo.toString() + "\n");
				outBW.write("*****" + numRun +  "#" + inputs + "\n");
				outBW.close();
			}			
			else if(outputInfo.length() != 0){
				File outFile = new File(filePath);
				BufferedWriter outBW = new BufferedWriter(new FileWriter(outFile, true));
				outBW.write(outputInfo.toString() + "\n");
				outBW.write("*****" + numRun +  "#" + inputs + "\n");
				outBW.close();
			}
			else{
				System.out.println("No Error and Output!");
			}
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}
	
	/*
	 * Execute the original program and seeded program with multiple input files 
	 */
	public static void mainProcessDir(){
		File currentDir = new File (".");
		FileFilter filter = new FileFilter(".xml");
		String[] inputFileList = currentDir.list(filter);
//		for(String eString : inputFileList)
//			System.out.println(eString);
		try{
			File origTraceFile = new File(basicDir + origTraceOut);
			File seededTraceFile = new File(basicDir + seededTraceOut);
			File exeOrderFile = new File(basicDir + exeOrder);
			for(String eachInput : inputFileList){
				BufferedWriter exeOrderBW = new BufferedWriter(new FileWriter(exeOrderFile, true));
				exeOrderBW.write(eachInput + "\n");
				exeOrderBW.close();
				numRun++;
				// Execute the original program 
				javaRun(eachInput, true);
				
				BufferedWriter origTraceBW = new BufferedWriter(new FileWriter(origTraceFile, true));
				origTraceBW.write("*****" + numRun + "#" + eachInput + "\n");
				origTraceBW.close();
				
				// Execute the seeded program 
				javaRun(eachInput, false);
				
				BufferedWriter seededTraceBW = new BufferedWriter(new FileWriter(seededTraceFile, true));
				seededTraceBW.write("*****" + numRun +  "#" + eachInput + "\n");
				seededTraceBW.close();
			}
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}
	
	/*
	 * Execute the original program and seeded program with one input file 
	 */
	public static void mainProcessFile(){

	}
	
}

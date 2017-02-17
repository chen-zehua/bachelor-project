package Cluster;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import weka.clusterers.ClusterEvaluation;
import weka.clusterers.SimpleKMeans;
import weka.core.Instances;
import weka.core.converters.ArffLoader;


public class Cluster{
	
	public static String basicDir = "E:\\IJSEKEExperiment\\";
	public static String result = "result.txt";
	public static String cluster = "cluster.arff";
	
	public static String clusterResult = "clusterResult.txt";
	
	
	public static void run(int numCluster){
		preprocess();
		simpleKMeans(numCluster);
	}
	
	public static void preprocess(){
		File clusterResultFile = new File(basicDir + clusterResult);
		if(clusterResultFile.exists()){
			clusterResultFile.delete();
		}
	}
	
	public static void simpleKMeans(int numCluster){
        
        try{
        	File clusterFile = new File(basicDir + cluster);
          	
        	ArffLoader loader = new ArffLoader();
        	loader.setFile(clusterFile);
        	Instances ins = loader.getDataSet();
        	SimpleKMeans KM = new SimpleKMeans();
        	KM.setNumClusters(numCluster);
        	KM.buildClusterer(ins);
        	KM.setPreserveInstancesOrder(true);
        	
        	ClusterEvaluation CE = new ClusterEvaluation(); 
        	CE.setClusterer(KM);
        	CE.evaluateClusterer(new Instances(ins));
        	
        	double[] classifier = CE.getClusterAssignments();
        	
          	File clusterResultFile = new File(basicDir + clusterResult);
          	BufferedWriter clusterResultBW = new BufferedWriter(new FileWriter(clusterResultFile));
//          	clusterResultBW.write(CE.clusterResultsToString());
          	
          	File resultFile = new File(basicDir + result);
          	BufferedReader resultBR = new BufferedReader(new FileReader(resultFile));
          	for(int i=0; i<classifier.length; i++){
          		String eachInput = resultBR.readLine();
          		if(eachInput != null){
          			clusterResultBW.write(eachInput + "#" + classifier[i] + "\n");
          		}
          		else{
          			break;
          		}
          	}
        	resultBR.close();
        	clusterResultBW.close();
        	
        }
        catch(Exception e){
        	e.printStackTrace();
        }   
	}
}

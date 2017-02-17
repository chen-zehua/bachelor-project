import java.util.HashMap;

import CCIdentify.ResultAndCC;
import Cluster.Cluster;
import Cluster.DataPreprocess;
import Configure.Configure;
import FaultLocalization.FaultLocalization;
import TestExec.TestExec;
import TestExec.TestExec2;
import TestSelection.TestSelection;
import TestSelection.TestSelection2;
import Selection.Selection;


public class MainProcess {

	public static void main(String[] args) {
		mainProcess();
	}
	
	public static void mainProcess(){
//		// get the run trace
//		TestExec.run();
//		TestExec2.run();
//		// determine the pass or the fail, and identify the CC
		ResultAndCC.run();
		
		//double lastEntropy = Double.MAX_VALUE;
		//int numCluster = Configure.getNumCluster();
		/*while(true){
			// fault localization
			double currentEntropy = FaultLocalization.run();
			if(currentEntropy < lastEntropy && numCluster > 1){
				//create the .arff file
				DataPreprocess.run();
				// clustering
				Cluster.run(2);
				// test case selection
				TestSelection.run();
			}
			else{
				break;
			}
			lastEntropy = currentEntropy;
			numCluster = 1;
		}*/
		double currentEntropy = FaultLocalization.run();
		DataPreprocess.run();
		Cluster.run(10);
//		TestSelection.run();
		TestSelection2.run();
//		Selection.run();
		double currentEntropy1 = FaultLocalization.run();
		//System.out.print(currentEntropy);

	}
	

}

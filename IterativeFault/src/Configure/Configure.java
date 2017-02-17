package Configure;

public class Configure {
	
	public static String binName = "DumpXML";
//	public static String binName = "tcas";
	
//	public static String faultStmt = "net.n3.nanoxml.NonValidator_122";
//	public static String faultStmt = "net.n3.nanoxml.NonValidator_273";
//	public static String faultStmt = "net.n3.nanoxml.NonValidator_392";
//	public static String faultStmt = "net.n3.nanoxml.NonValidator_482";
//	public static String faultStmt = "net.n3.nanoxml.StdXMLParser_366";
//	public static String faultStmt = "net.n3.nanoxml.StdXMLParser_456";
//	public static String faultStmt = "net.n3.nanoxml.XMLElement_875";
//	public static String faultStmt = "net.n3.nanoxml.ContentReader_132";
//	public static String faultStmt = "net.n3.nanoxml.StdXMLParser_368";
	public static String faultStmt = "net.n3.nanoxml.XMLException_146";	
	
//	public static String faultStmt = "tcas_84";
//	public static String faultStmt = "tcas_45";
//	public static String faultStmt = "tcas_47";
//	public static String faultStmt = "tcas_64";
//	public static String faultStmt = "tcas_88";
//	public static String faultStmt = "tcas_98";
//	public static String faultStmt = "tcas_118";
//	public static String faultStmt = "tcas_118#tcas_123";
//	public static String faultStmt = "tcas_132";
//	public static String faultStmt = "tcas_134";
	
	public static int numCluster = 40;
	
	
	public static String getBinName() {
		return binName;
	}
	public static void setBinName(String binName) {
		Configure.binName = binName;
	}
	public static String getFaultStmt() {
		return faultStmt;
	}
	public static void setFaultStmt(String faultStmt) {
		Configure.faultStmt = faultStmt;
	}
	public static int getNumCluster() {
		return numCluster;
	}
	public static void setNumCluster(int numCluster) {
		Configure.numCluster = numCluster;
	}
		
}

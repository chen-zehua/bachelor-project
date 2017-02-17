/* Usage: java MainDriver appClass
 */

/* import necessary soot packages */
import soot.*;

public class MainDriver {
  public static void main(String[] args) {

    /* check the arguments */
    /*if (args.length == 0) {
      System.err.println("Usage: java MainDriver [options] classname");
      System.exit(0);
    }*/
    
    /* add a phase to transformer pack by call Pack.add */
    Pack jtp = PackManager.v().getPack("jtp");
    jtp.add(new Transform("jtp.instrumenter", new StmtCountInstrumenter()));

    /* Give control to Soot to process all options, 
     * InvokeStaticInstrumenter.internalTransform will get called.
     */
//    String srcDir = "E:\\IJSEKEExperiment\\origSrc";
//    String resultDir = "E:\\IJSEKEExperiment\\orig";
    String srcDir = "E:\\IJSEKEExperiment\\seededSrc";
    String resultDir = "E:\\IJSEKEExperiment\\seeded";
    String[] inputs = {"-keep-line-number", "-cp", srcDir, "-pp", "-process-dir", srcDir, "-d", resultDir};
    soot.Main.main(inputs);
  }
}



/*
 * InvokeStaticInstrumenter inserts count instructions before
 * INVOKESTATIC bytecode in a program. The instrumented program will
 * report how many static invocations happen in a run.
 * 
 * Goal:
 *   Insert counter instruction before static invocation instruction.
 *   Report counters before program's normal exit point.
 *
 * Approach:
 *   1. Create a counter class which has a counter field, and 
 *      a reporting method.
 *   2. Take each method body, go through each instruction, and
 *      insert count instructions before INVOKESTATIC.
 *   3. Make a call of reporting method of the counter class.
 *
 * Things to learn from this example:
 *   1. How to use Soot to examine a Java class.
 *   2. How to insert profiling instructions in a class.
 */

/* InvokeStaticInstrumenter extends the abstract class BodyTransformer,
 * and implements <pre>internalTransform</pre> method.
 */
import soot.*;
import soot.jimple.*;
import soot.util.*;

import java.util.*;

public class StmtCountInstrumenter extends BodyTransformer {

	/*
	 * internalTransform goes through a method body and inserts counter
	 * instructions before an INVOKESTATIC instruction
	 */
	protected void internalTransform(Body body, String phase, Map options) {

		// body's method
		SootMethod method = body.getMethod();
		String methodName = method.toString();
		System.out.println(methodName + "...............");
		if (methodName.contains("SEIInstrument")) {
			return;
		}
		
		System.out.println("instrumenting method : " + method.getSignature());

		String className = methodName.substring(1, methodName.indexOf(":"));

		// get body's unit as a chain
		Chain units = body.getUnits();

		// get a snapshot iterator of the unit since we are going to
		// mutate the chain when iterating over it.
		Iterator stmtIt = units.snapshotIterator();
		
		SootClass instrumentClass = Scene.v().getSootClass("SEIInstrument");

		SootMethod recordCounter = instrumentClass.getMethod("void recordStm(int,java.lang.String)");

		int lastNum = 0;

		// typical while loop for iterating over each statement
		while (stmtIt.hasNext()) {

			// cast back to a statement.
			Stmt stmt = (Stmt) stmtIt.next();
			// identity Statement
			if (stmt instanceof IdentityStmt || stmt instanceof GotoStmt) {
				continue;
			}
			
			// get the line number of the statement
			int lineNum = stmt.getJavaSourceStartLineNumber();

			if (lineNum != lastNum) {

				InvokeExpr incExpr = Jimple.v().newStaticInvokeExpr(
						recordCounter.makeRef(), IntConstant.v(lineNum),
						StringConstant.v(className));
				Stmt incStmt = Jimple.v().newInvokeStmt(incExpr);
				units.insertBefore(incStmt, stmt);
				lastNum = lineNum;
			}
		}
		System.out.println("end");
	}
}

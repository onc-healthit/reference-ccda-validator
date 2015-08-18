import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.apache.commons.io.IOUtils;
import org.sitenv.referenceccda.validator.RefCCDAValidationResult;
import org.sitenv.referenceccda.validator.ReferenceCCDAValidator;

import junit.framework.TestCase;

public class ReferenceCCDAValidatorTest extends TestCase {

	public void testCCDAR2(){
		
		String valObjective = "TOC";
		String refFileName = "Ref_File1.pdf";
		
		
		String ccdaFile = "";
		try {
			InputStream in = getClass().getResourceAsStream("Sample.xml");
			ccdaFile = IOUtils.toString(in);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ArrayList<RefCCDAValidationResult> res = 
				ReferenceCCDAValidator.validateCCDAWithReferenceFileName(valObjective, refFileName, ccdaFile);
		
		System.out.println(" No of Messages = " + res.size());
	}
}

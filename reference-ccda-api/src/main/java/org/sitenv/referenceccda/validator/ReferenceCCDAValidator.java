package org.sitenv.referenceccda.validator;

import org.openhealthtools.mdht.uml.cda.consol.ConsolPackage;
import org.openhealthtools.mdht.uml.cda.ClinicalDocument;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

import org.apache.commons.io.IOUtils;
import org.eclipse.emf.common.util.Diagnostic;
import org.openhealthtools.mdht.uml.cda.util.CDADiagnostic;
import org.openhealthtools.mdht.uml.cda.util.CDAUtil;
import org.openhealthtools.mdht.uml.cda.util.ValidationResult;

public class ReferenceCCDAValidator {
	
	public static ArrayList<RefCCDAValidationResult> validateCCDAWithReferenceFileName(
			String validationObjective,
			String referenceFileName,
			String ccdaFile)
	{
		// create a validation result object to collect diagnostics produced during validation
		ValidationResult result = new ValidationResult();
		ConsolPackage.eINSTANCE.eClass();
		
		try {
			
			//Identify the objectives and check for the document type
			
			// Check if all files are C-CDA compliant
			InputStream in = IOUtils.toInputStream(ccdaFile, "UTF-8");
			ClinicalDocument clinicalDocument = CDAUtil.load(in, result);
			
			// If there are no schema errors, check for Vocabulary errors
			
			// If there are no vocab errors, then check for reference CCDA errors
					
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ArrayList<RefCCDAValidationResult> results = new ArrayList<RefCCDAValidationResult>();
		
		for (Diagnostic diagnostic : result.getErrorDiagnostics()) {
		   
			CDADiagnostic diag = new CDADiagnostic(diagnostic);
			results.add(new RefCCDAValidationResult(
							diag.getMessage(), diag.getPath(), RefCCDAValidationResult.CCDA_IG_CONFORMANCE_ERROR));
			
		  }

		for (Diagnostic diagnostic : result.getWarningDiagnostics()) {
		
			CDADiagnostic diag = new CDADiagnostic(diagnostic);
			results.add(new RefCCDAValidationResult(
							diag.getMessage(), diag.getPath(), RefCCDAValidationResult.CCDA_IG_CONFORMANCE_WARN));
			
		  }
		
		for (Diagnostic diagnostic : result.getInfoDiagnostics()) {
			
			CDADiagnostic diag = new CDADiagnostic(diagnostic);
			results.add(new RefCCDAValidationResult(
							diag.getMessage(), diag.getPath(), RefCCDAValidationResult.CCDA_IG_CONFORMANCE_INFO));
			
		  }
		
		return results;
	}

}

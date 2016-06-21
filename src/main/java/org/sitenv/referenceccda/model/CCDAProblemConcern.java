package org.sitenv.referenceccda.model;

import java.util.ArrayList;

public class CCDAProblemConcern {

	private ArrayList<CCDAII>     		templateId;
	private CCDACode         	   		concernCode;
	private CCDADataElement  			statusCode;
	private CCDAEffTime      			effTime;
	private ArrayList<CCDAProblemObs>  	problems;
	
	public CCDAProblemConcern()
	{
		problems = new ArrayList<CCDAProblemObs>();
	}
	
}

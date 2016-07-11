package org.sitenv.referenceccda.model;

import org.apache.log4j.Logger;

import java.util.ArrayList;

public class CCDABirthSexObs {

	private static Logger log = Logger.getLogger(CCDASmokingStatus.class.getName());

	private ArrayList<CCDAII>					templateIds;
	private CCDACode                            birthSexObsCode;
	private CCDACode							sexCode;
	private CCDAEffTime							observationTime;

	public CCDABirthSexObs() {
		templateIds = new ArrayList<CCDAII>();
	}

	public void log() {

		if(birthSexObsCode != null)
			log.info(" Birth Sex Obs Code = " + birthSexObsCode.getCode());
		
		if(sexCode != null)
			log.info(" Birth Sex Code = " + sexCode.getCode());

		for(int j = 0; j < templateIds.size(); j++) {
			log.info(" Tempalte Id [" + j + "] = " + templateIds.get(j).getRootValue());
			log.info(" Tempalte Id Ext [" + j + "] = " + templateIds.get(j).getExtValue());
		}

		if(observationTime != null)
			observationTime.log();
	}

	public ArrayList<CCDAII> getTemplateIds() {
		return templateIds;
	}

	public void setTemplateIds(ArrayList<CCDAII> ids) {
		
		if(ids != null)
			this.templateIds = ids;
	}

	public CCDACode getBirthSexObsCode() {
		return birthSexObsCode;
	}

	public void setBirthSexObsCode(CCDACode birthSexObsCode) {
		this.birthSexObsCode = birthSexObsCode;
	}

	public CCDACode getSexCode() {
		return sexCode;
	}

	public void setSexCode(CCDACode sexCode) {
		this.sexCode = sexCode;
	}

	public CCDAEffTime getObservationTime() {
		return observationTime;
	}

	public void setObservationTime(CCDAEffTime observationTime) {
		this.observationTime = observationTime;
	}
}

package org.sitenv.referenceccda.model;

import org.apache.log4j.Logger;

public class CCDAPreferredLanguage {
	
		private static Logger log = Logger.getLogger(CCDAPatient.class.getName());
	
		private CCDACode languageCode ;
		private CCDACode modeCode;
		private CCDADataElement preferenceInd;
		
		public CCDACode getLanguageCode() {
			return languageCode;
		}
		public void setLanguageCode(CCDACode languageCode) {
			this.languageCode = languageCode;
		}
		public CCDACode getModeCode() {
			return modeCode;
		}
		public void setModeCode(CCDACode modeCode) {
			this.modeCode = modeCode;
		}
		public CCDADataElement getPreferenceInd() {
			return preferenceInd;
		}
		public void setPreferenceInd(CCDADataElement preferenceInd) {
			this.preferenceInd = preferenceInd;
		}
		
		public void log() {
			
			log.info("Language Code = " + (languageCode==null ? "No Data" : languageCode.getCode()));
			log.info("Mode Code = " + (modeCode==null ? "No Data" : modeCode.getCode()));
			log.info("Preferece Ind = " + (preferenceInd==null ? "No Data" : preferenceInd.getValue()));
		}
		
		@Override
		public boolean equals(Object obj) {

			if (obj == this) { 
				return true; 
			} 
			if (obj == null || obj.getClass() != this.getClass()) { 
				return false; 
			} 
			CCDAPreferredLanguage obj2 = (CCDAPreferredLanguage) obj; 
			return  (this.languageCode == obj2.getLanguageCode() || (this.languageCode != null && this.languageCode.equals(obj2.getLanguageCode()))) && 
					(this.modeCode == obj2.getModeCode() || (this.modeCode != null && this.modeCode .equals(obj2.getModeCode())))&&				
					(this.preferenceInd == obj2.getPreferenceInd() || (this.preferenceInd != null && this.preferenceInd .equals(obj2.getPreferenceInd())));
			
		}
}

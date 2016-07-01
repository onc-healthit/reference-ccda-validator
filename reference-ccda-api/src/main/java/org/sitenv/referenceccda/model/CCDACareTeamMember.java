package org.sitenv.referenceccda.model;

import java.util.ArrayList;

import org.apache.log4j.Logger;

public class CCDACareTeamMember {
	
	private static Logger log = Logger.getLogger(CCDACareTeamMember.class.getName());

	private ArrayList<CCDAParticipant> members;
	
	public void log() {
		
		for (int i = 0; i < members.size(); i++) {
			members.get(i).log();
		}
	}
	
	public CCDACareTeamMember()
	{
		members = new ArrayList<CCDAParticipant>();
	}

	public ArrayList<CCDAParticipant> getMembers() {
		return members;
	}

	public void setMembers(ArrayList<CCDAParticipant> ms) {
		
		if(ms != null)
			this.members = ms;
	}
}

package org.sitenv.referenceccda.model;

import java.util.ArrayList;

public class CCDACareTeamMember {

	private ArrayList<CCDAParticipant> members;
	
	public CCDACareTeamMember(){}

	public ArrayList<CCDAParticipant> getMembers() {
		return members;
	}

	public void setMembers(ArrayList<CCDAParticipant> members) {
		this.members = members;
	}
}

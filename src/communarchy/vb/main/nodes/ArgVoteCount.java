package communarchy.vb.main.nodes;

import javax.servlet.http.HttpServletRequest;

import com.google.template.soy.data.SoyMapData;

import communarchy.facts.PMSession;
import communarchy.facts.implementations.Argument;
import communarchy.facts.interfaces.IUser;
import communarchy.vb.AbstractTemplateWrapper;
import communarchy.vb.IResourceTemplateWrapper;
import communarchy.vb.utils.displayformatting.numbers.NumberFormatter;

public class ArgVoteCount extends AbstractTemplateWrapper implements IResourceTemplateWrapper<Argument> {

	private static ArgVoteCount INSTANCE;
	private ArgVoteCount() {}
	
	public static final String P_VOTE_COUNT = "voteCount";
	
	public static ArgVoteCount get() {
		if(INSTANCE == null) {
			INSTANCE = new ArgVoteCount();
		}
		
		return INSTANCE;
	}
	
	@Override
	public String getTemplate() {
		return "./templates/html/main/nodes/ArgVoteCount.soy";
	}

	@Override
	public SoyMapData getParams(PMSession pmSession, IUser user,
			HttpServletRequest request, Argument scopedResource) {
		
		SoyMapData pMap = new SoyMapData();
		pMap.put(P_VOTE_COUNT, NumberFormatter.VoteNumber(scopedResource.getVotes()));
		return pMap;
	}
}
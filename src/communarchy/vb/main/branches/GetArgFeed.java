package communarchy.vb.main.branches;

import javax.servlet.http.HttpServletRequest;

import com.google.template.soy.data.SoyListData;
import com.google.template.soy.data.SoyMapData;

import communarchy.facts.PMSession;
import communarchy.facts.implementations.Argument;
import communarchy.facts.interfaces.IUser;
import communarchy.facts.mappers.QueryMapper;
import communarchy.facts.queries.list.GetRankedArguments;
import communarchy.facts.queries.list.GetRecentArguments;
import communarchy.facts.queries.list.IPagedQuery;
import communarchy.facts.results.PageSet;
import communarchy.vb.AbstractTemplateWrapper;
import communarchy.vb.IResourceTemplateWrapper;
import communarchy.vb.main.nodes.ArgFeedElement;

public class GetArgFeed extends AbstractTemplateWrapper implements IResourceTemplateWrapper<String> {

	private static GetArgFeed INSTANCE;
	private GetArgFeed() {}
	
	public static GetArgFeed get() {
		if(INSTANCE == null) {
			INSTANCE = new GetArgFeed();
			INSTANCE.possiblePaths.add(ArgFeedElement.get());
		}
		
		return INSTANCE;
	}

	@Override
	public String getTemplate() {
		return "./templates/html/main/branches/GetArgFeed.soy";
	}
	
	public static final String P_ARGS = "args";

	@Override
	public SoyMapData getParams(PMSession pmSession, IUser user,
			HttpServletRequest request, String tab) {
		
		SoyMapData pMap = new SoyMapData();
		
		SoyListData argList = new SoyListData();
		IPagedQuery<Argument> query = null;
		if(tab.equals("Ranked")) {
			query = new GetRankedArguments(null);
		} else if(tab.equals("Recent")) {
			query = new GetRecentArguments(null);
		}
		
		PageSet<Argument> pageSet = pmSession.getMapper(QueryMapper.class).runPagedListQuery(query);
		
		if(pageSet != null) {
			for(Argument arg : pageSet.getPages(pmSession)) {
				argList.add(ArgFeedElement.get().getParams(pmSession, user, request, arg));
			}
		}
		
		pMap.put(P_ARGS, argList);
		return pMap;
	}
}
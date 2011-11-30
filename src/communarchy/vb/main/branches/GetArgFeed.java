package communarchy.vb.main.branches;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.datanucleus.store.appengine.query.JDOCursorHelper;

import com.google.template.soy.data.SoyListData;
import com.google.template.soy.data.SoyMapData;

import communarchy.facts.PMSession;
import communarchy.facts.implementations.Argument;
import communarchy.facts.interfaces.IUser;
import communarchy.facts.mappers.QueryMapper;
import communarchy.facts.queries.list.ArgFeedQuery;
import communarchy.facts.results.PageSet;
import communarchy.vb.AbstractTemplateWrapper;
import communarchy.vb.IParamBuilder;

public class GetArgFeed extends AbstractTemplateWrapper implements IParamBuilder {

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
			HttpServletRequest request) {
		
		SoyMapData pMap = new SoyMapData();
		
		SoyListData argList = new SoyListData();
		ArgFeedQuery query = new ArgFeedQuery(null);
		List<Argument> results = pmSession.getMapper(QueryMapper.class).runListQuery(query);
		PageSet<Argument> pageSet = new PageSet<Argument>(results, null, JDOCursorHelper.getCursor(results).toWebSafeString());
		
		for(Argument arg : pageSet.getPages()) {
			argList.add(ArgFeedElement.get().getParams(pmSession, user, request, arg));
		}
		
		pMap.put(P_ARGS, argList);
		return pMap;
	}
}
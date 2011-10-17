package communarchy.facts.mappers.interfaces;

import java.util.List;

import com.google.appengine.api.datastore.Key;

import communarchy.exceptions.CommunarchyPersistenceException;
import communarchy.facts.actions.interfaces.IFlag;
import communarchy.facts.actions.interfaces.IVote;
import communarchy.facts.interfaces.IPoint;
import communarchy.facts.interfaces.IPointOfView;

public interface IPovMapper extends IPostMapper<IPointOfView>, IPostHasParentMapper<IPoint, IPointOfView> {
	public List<IPointOfView> selectAllChildrenByStance(Key pointId, Integer stance);
	public List<IVote> selectAllVotes(Key pointId, Key userId);
	public Integer getPovVoteCount(Key povId);
	public IFlag selectFlag(IFlag flag);
	public void insertVote(Key povId, Key userId);
	public void insertFlagIgnored(Key povId, Key userId, int flagType);
	public void reclaimVote(Key povId, Key userId) throws CommunarchyPersistenceException;
	public IVote selectVote(Key povId, Key userId);
}

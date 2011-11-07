package communarchy.facts.mappers.interfaces;

import java.util.List;

import com.google.appengine.api.datastore.Key;

import communarchy.exceptions.CommunarchyPersistenceException;
import communarchy.facts.implementations.Point;
import communarchy.facts.interfaces.IArgument;
import communarchy.facts.interfaces.IPoint;
import communarchy.facts.interfaces.IPointOfView;
import communarchy.facts.interfaces.IUserStance;

public interface IPointMapper extends IPostMapper<Point>, IPostHasChildrenMapper<IPointOfView>, IPostHasParentMapper<IArgument, IPoint> {
	public IUserStance selectStance(Key pointKey, Key userKey);
	public Integer getPointCountByStance(Key pointKey, Integer stance);
	public Integer getVoteCount(Key pointKey, Key userKey);
	public Integer getMaxVoteCount(Key pointKey, int stance);
	public Integer getPointAgreeCount(Key pointId);
	public Integer getPointNeutralCount(Key pointId);
	public Integer getPointDisagreeCount(Key pointId);
	public Key getTopPoint(Key argId);
	public void updateStance(IUserStance stance, Integer oldStance) throws CommunarchyPersistenceException;
	public void reclaimAllVotes(IUserStance stance) throws CommunarchyPersistenceException;
	public void insertNewStance(IUserStance stance) throws CommunarchyPersistenceException;
	public List<IPointOfView> getPovsByStance(Key pointKey, int stance);
}
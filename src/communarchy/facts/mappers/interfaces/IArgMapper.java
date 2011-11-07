package communarchy.facts.mappers.interfaces;

import communarchy.facts.implementations.Argument;
import communarchy.facts.interfaces.IPoint;

public interface IArgMapper extends IPostMapper<Argument>, IPostHasChildrenMapper<IPoint> {}
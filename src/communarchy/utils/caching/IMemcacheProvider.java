package communarchy.utils.caching;

public interface IMemcacheProvider<T> {
	public <U> U getValue(String key);
	public void putValue(String scopeKey, String mapKey, Object mapValue);
	public void clearCache(T scope);
}

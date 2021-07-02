package io.aext.core.base.service;

import java.util.Optional;

/**
 * @author Rojar Smith
 *
 * @date 2021-07-02
 */
public interface DataCacheService extends BaseService<DataCacheService> {
	Optional<String> readString(String key);

	void update(String key, String value, int seconds);

	void delete(String key);
}

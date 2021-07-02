package io.aext.core.base.service;

import java.util.Optional;

import io.aext.core.base.model.vo.ECBaseVO;

/**
 * @author rojar
 *
 * @date 2021-07-02
 */
public interface EmailContentBuilder {
	<T extends ECBaseVO> Optional<String> generateMailContent(T content);
}

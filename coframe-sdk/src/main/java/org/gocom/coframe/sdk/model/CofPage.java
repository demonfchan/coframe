/*******************************************************************************
 *
 * Copyright (c) 2001-2019 Primeton Technologies, Ltd.
 * All rights reserved.
 *
 * Created on Jun 27, 2019
 *******************************************************************************/
package org.gocom.coframe.sdk.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * @author qinsc (mailto:qinsc@primeton.com)
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CofPage<T> extends PageImpl<T> {
	private static final long serialVersionUID = -6271295484749679672L;

	@JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public CofPage(@JsonProperty("content") List<T> content,
                            @JsonProperty("number") int number,
                            @JsonProperty("size") int size,
                            @JsonProperty("totalElements") Long totalElements,
                            @JsonProperty("pageable") JsonNode pageable,
                            @JsonProperty("last") boolean last,
                            @JsonProperty("totalPages") int totalPages,
                            @JsonProperty("sort") JsonNode sort,
                            @JsonProperty("first") boolean first,
                            @JsonProperty("numberOfElements") int numberOfElements) {

        super(content, PageRequest.of(number, size), totalElements);
    }

    public CofPage(List<T> content, Pageable pageable, long total) {
        super(content, pageable, total);
    }

    public CofPage(List<T> content) {
        super(content);
    }

    public CofPage() {
        super(new ArrayList<>());
    }
}

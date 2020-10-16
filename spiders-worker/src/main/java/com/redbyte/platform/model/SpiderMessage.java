package com.redbyte.platform.model;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author wangwq
 */
@Data
public class SpiderMessage implements Serializable {
    private static final long serialVersionUID = -1978589533458241397L;

    private String requestUrl;

    private String html;
}

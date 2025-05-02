package com.yugao.dto.post;

import lombok.Data;

@Data
public class BoardPostsPageDTO {

    private Integer limit;

    private Integer orderMode;

    private Integer index;

    public int getSafeLimit() {
        return limit == null ? 10 : limit;
    }

    public int getSafeOrderMode() {
        return orderMode == null ? 0 : orderMode;
    }

    public int getSafeIndex() {
        return index == null ? 1 : index;
    }
}

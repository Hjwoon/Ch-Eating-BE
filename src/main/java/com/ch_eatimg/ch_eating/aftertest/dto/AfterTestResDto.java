package com.ch_eatimg.ch_eating.aftertest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AfterTestResDto{
    private Long afterId;
    private String userId;
    private String afterResult;
    private String afterWin;
}

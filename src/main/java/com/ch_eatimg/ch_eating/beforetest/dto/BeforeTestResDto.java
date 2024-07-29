package com.ch_eatimg.ch_eating.beforetest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BeforeTestResDto {
    private Long beforeId;
    private String userId;
    private String beforeResult;
    private String beforeWin;
}

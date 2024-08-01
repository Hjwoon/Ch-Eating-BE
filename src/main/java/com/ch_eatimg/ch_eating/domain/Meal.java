package com.ch_eatimg.ch_eating.domain;

import com.ch_eatimg.ch_eating.util.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "MEAL")
public class Meal extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MEAL_ID")
    private Long mealId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private User user;

    @Column(name = "MEAL_BRAND")
    private String mealBrand; // 식사 브랜드명

    @Column(name = "MEAL_TYPE")
    private String mealType; // 식사량 종류

    @Column(name = "MEAL_NAME")
    private String mealName; // 식사량 메뉴명

    @Column(name = "MEAL_AMOUNT")
    private String mealAmount; // 식사량

    @Column(name = "MEAL_DETAIL")
    private String mealDetail; // 세부사항

    /// 식사량 수정 메소드
    public void changeMeal(String mealType, String mealBrand, String mealName, String mealAmount, String mealDetail) {
        this.mealType = mealType;
        this.mealBrand = mealBrand;
        this.mealName = mealName;
        this.mealAmount = mealAmount;
        this.mealDetail = mealDetail;
    }
}

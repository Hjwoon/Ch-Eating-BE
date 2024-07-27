package com.ch_eatimg.ch_eating.meal.service;

import com.ch_eatimg.ch_eating.domain.Meal;
import com.ch_eatimg.ch_eating.domain.User;
import com.ch_eatimg.ch_eating.meal.dto.MealCreateRequestDto;
import com.ch_eatimg.ch_eating.meal.dto.MealCreateResponseDto;
import com.ch_eatimg.ch_eating.meal.dto.MealDeleteRequestDto;
import com.ch_eatimg.ch_eating.meal.dto.MealUpdateRequestDto;
import com.ch_eatimg.ch_eating.meal.repository.MealRepository;
import com.ch_eatimg.ch_eating.user.repository.UserRepository;
import com.ch_eatimg.ch_eating.util.response.CustomApiResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MealServiceImpl implements MealService {
    private final MealRepository mealRepository;
    private final UserRepository userRepository;

    public MealServiceImpl(MealRepository mealRepository, UserRepository userRepository) {
        this.mealRepository = mealRepository;
        this.userRepository = userRepository;
    }

    // 식사량 등록
    @Override
    @Transactional
    public ResponseEntity<CustomApiResponse<?>> createMeals(MealCreateRequestDto requestDto) {
        // 생성 유저가 db에 존재하는가?
        Optional<User> findUser = userRepository.findByUserId(requestDto.getUserId());

        // 존재하지 않는다면 오류 리턴하는 로직 추가
        if (findUser.isEmpty()) {
            return ResponseEntity.status(404)
                    .body(CustomApiResponse.createFailWithout(404, "존재하지 않는 사용자입니다."));
        }

        // 존재한다면 Meal DB에 저장
        Meal newMeal = Meal.builder()
                .user(findUser.get())
                .mealType(requestDto.getMealType())
                .mealName(requestDto.getMealName())
                .mealBrand(requestDto.getMealBrand())
                .mealAmount(requestDto.getMealAmount())
                .mealDetail(requestDto.getMealDetail())
                .build();

        mealRepository.save(newMeal);

        // 응답으로 넘겨줄 dto
        MealCreateResponseDto responseDto = MealCreateResponseDto.builder()
                .mealId(newMeal.getMealId())
                .createAt(newMeal.getCreateAt())
                .build();

        return ResponseEntity.status(201)
                .body(CustomApiResponse.createSuccess(201, responseDto, "식사량이 성공적으로 등록되었습니다."));
    }

    /*@Override // 식사량 수정
    public ResponseEntity<CustomApiResponse<?>> updateMeal(String userId, Long mealId, MealUpdateRequestDto.Req req) {
        return null;
    }*/

    /*@Override // 식사량 삭제
    public ResponseEntity<CustomApiResponse<?>> deleteMeal(MealDeleteRequestDto dto) {
        return null;
    }*/
}

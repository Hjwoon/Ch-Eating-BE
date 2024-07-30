package com.ch_eatimg.ch_eating.meal.service;

import com.ch_eatimg.ch_eating.domain.Meal;
import com.ch_eatimg.ch_eating.domain.User;
import com.ch_eatimg.ch_eating.meal.dto.*;
import com.ch_eatimg.ch_eating.meal.repository.MealRepository;
import com.ch_eatimg.ch_eating.user.repository.UserRepository;
import com.ch_eatimg.ch_eating.util.response.CustomApiResponse;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    // 입력 데이터 유효성 검사 메서드
    private boolean isValidMealData(MealDto meal) {
        return meal.getMealType() != null && !meal.getMealType().isEmpty() &&
                meal.getMealName() != null && !meal.getMealName().isEmpty() &&
                meal.getMealAmount() != null && !meal.getMealAmount().isEmpty();
    }

    // 식사량 등록
    @Override @Transactional
    public ResponseEntity<CustomApiResponse<?>> createMeals(MealCreateRequestDto requestDto) {
        try {
            // 생성 유저가 db에 존재하는가?
            Optional<User> findUser = userRepository.findByUserId(requestDto.getUserId());

            // 존재하지 않는다면 오류 리턴하는 로직 추가
            if (findUser.isEmpty()) {
                return ResponseEntity.status(404)
                        .body(CustomApiResponse.createFailWithout(404, "존재하지 않는 사용자입니다."));
            }

            // 입력된 각 식사 데이터 유효성 검사
            for (MealDto meal : requestDto.getMeals()) {
                if (!isValidMealData(meal)) {
                    return ResponseEntity.status(400)
                            .body(CustomApiResponse.createFailWithout(400, "입력된 데이터가 유효하지 않습니다."));
                }
            }

            // 유효한 데이터라면 Meal DB에 저장하고 응답 DTO 생성
            List<MealCreateResponseDto> responseDtos = new ArrayList<>();
            for (MealDto meal : requestDto.getMeals()) {
                Meal newMeal = Meal.builder()
                        .user(findUser.get())
                        .mealType(meal.getMealType())
                        .mealName(meal.getMealName())
                        .mealBrand(meal.getMealBrand())
                        .mealAmount(meal.getMealAmount())
                        .mealDetail(meal.getMealDetail())
                        .build();

                mealRepository.save(newMeal);

                MealCreateResponseDto responseDto = MealCreateResponseDto.builder()
                        .mealId(newMeal.getMealId())
                        .createAt(newMeal.getCreateAt())
                        .build();

                responseDtos.add(responseDto);
            }

            return ResponseEntity.status(201)
                    .body(CustomApiResponse.createSuccess(201, responseDtos, "식사량이 성공적으로 등록되었습니다."));
        } catch (Exception e) {
            // 500 내부 서버 오류 처리
            return ResponseEntity.status(500)
                    .body(CustomApiResponse.createFailWithout(500, "서버 오류가 발생했습니다."));
        }
    }

    // 식사량 전체 조회
    @Override @Transactional
    public ResponseEntity<CustomApiResponse<?>> getMeals() {
        List<Meal> meals = mealRepository.findAll();

        List<MealListDto.MealResponse> mealResponses = new ArrayList<>();
        for(Meal meal : meals) {
            Long mealId = meal.getMealId();
            mealResponses.add(MealListDto.MealResponse.builder()
                    .mealId(mealId)
                    .mealName(meal.getMealName())
                    .mealType(meal.getMealType())
                    .mealAmount(meal.getMealAmount())
                    .mealBrand(meal.getMealBrand())
                    .mealDetail(meal.getMealDetail())
                    .build());
        }

        return ResponseEntity.status(200)
                .body(CustomApiResponse.createSuccess(200, mealResponses, "식사량 전체 조회에 성공하였습니다."));
    }

    // 식사량 수정
    /*@Override @Transactional
    public ResponseEntity<CustomApiResponse<?>> modifyMeal(Long mealId, MealModifyRequestDto.Req requestDto) {

        Optional<Meal> findMeal = mealRepository.findById(mealId);

        // 수정할 식사량이 존재하지 않으면 오류 리턴
        if (findMeal.isEmpty()) {
            return ResponseEntity.status(404)
                    .body(CustomApiResponse.createFailWithout(404, "존재하지 않는 식사량입니다."));
        }

        Meal meal = findMeal.get();

        // 식사량 업데이트
        meal.changeMeal(requestDto.getMealName(), requestDto.getMealType(), requestDto.getMealBrand(), requestDto.getMealAmount(), requestDto.getMealDetail());
        mealRepository.flush();

        // 응답 DTO 생성
        MealModifyResponseDto responseDto = MealModifyResponseDto.builder()
                .mealId(meal.getMealId())
                .userId(meal.getUser().getUserId())
                .mealType(meal.getMealType())
                .mealBrand(meal.getMealBrand())
                .mealName(meal.getMealName())
                .mealAmount(meal.getMealAmount())
                .mealDetail(meal.getMealDetail())
                .createAt(meal.getCreateAt())
                .updateAt(meal.getUpdateAt())
                .build();

        return ResponseEntity.status(200)
                .body(CustomApiResponse.createSuccess(200, responseDto, "식사량 기록이 성공적으로 수정되었습니다."));
    }
*/
    // 식사량 삭제
    /*@Override @Transactional
    public ResponseEntity<CustomApiResponse<?>> deleteMeal(MealDeleteRequestDto dto) {

        Optional<Meal> findMeal = mealRepository.findByMealId(Long.valueOf(dto.getMealId()));

        // 삭서령울 석재헐 권한이 있는 유저인지 먼저 확인
        // 현재 유저와 식사량 작성자가 같은지
        String mealUserId = findMeal.get().getUser().getUserId();

        if (!(mealUserId.equals(dto.getUserId()))) { // 다르면
            return ResponseEntity.status(400)
                    .body(CustomApiResponse.createFailWithout(400, "식사량 작성자가 아닙니다."));

        }

        // 식사량이 존재하는지
        if (findMeal.isEmpty()) {
            return ResponseEntity.status(404)
                    .body(CustomApiResponse.createFailWithout(404, "식사량이 존재하지 않습니다."));
        }

        // 식사량 삭제
        mealRepository.delete(findMeal.get());

        return ResponseEntity.status(201)
                .body(CustomApiResponse.createSuccess(201, findMeal.get().getMealId(), "식사량이 삭제되었습니다."));
    }*/
}

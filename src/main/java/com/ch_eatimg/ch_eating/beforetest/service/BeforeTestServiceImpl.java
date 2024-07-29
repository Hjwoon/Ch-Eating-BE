package com.ch_eatimg.ch_eating.beforetest.service;

import com.ch_eatimg.ch_eating.beforetest.dto.BeforeTestReqDto;
import com.ch_eatimg.ch_eating.beforetest.dto.BeforeTestResDto;
import com.ch_eatimg.ch_eating.beforetest.repository.BeforeTestRepository;
import com.ch_eatimg.ch_eating.domain.BeforeTest;
import com.ch_eatimg.ch_eating.domain.User;
import com.ch_eatimg.ch_eating.security.JwtTokenProvider;
import com.ch_eatimg.ch_eating.user.repository.UserRepository;
import com.ch_eatimg.ch_eating.util.response.CustomApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BeforeTestServiceImpl implements BeforeTestService{

    private final BeforeTestRepository beforeTestRepository;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public CustomApiResponse<BeforeTestResDto> createBeforeTest(HttpServletRequest request, BeforeTestReqDto dto) {
        String userId = jwtTokenProvider.getUsername(jwtTokenProvider.resolveToken(request));
        User user = userRepository.findByUserId(userId).orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        BeforeTest beforeTest = BeforeTest.builder()
                .userId(user)
                .beforeResult(dto.getBeforeResult())
                .beforeWin(null)
                .build();

        beforeTestRepository.save(beforeTest);

        BeforeTestResDto responseDto = BeforeTestResDto.builder()
                .beforeId(beforeTest.getBeforeId())
                .userId(userId)
                .beforeResult(beforeTest.getBeforeResult())
                .beforeWin(beforeTest.getBeforeWin())
                .build();

        return CustomApiResponse.createSuccess(
                HttpStatus.CREATED.value(),
                responseDto,
                "식사 전 테스트 결과 등록 성공"
        );
    }

    @Override
    public CustomApiResponse<List<BeforeTestResDto>> getBeforeTestsByUser(HttpServletRequest request) {
        String userId = jwtTokenProvider.getUsername(jwtTokenProvider.resolveToken(request));
        User user = userRepository.findByUserId(userId).orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        List<BeforeTestResDto> beforeTests = beforeTestRepository.findByUserId(user).stream()
                .map(beforeTest -> BeforeTestResDto.builder()
                        .beforeId(beforeTest.getBeforeId())
                        .userId(userId)
                        .beforeResult(beforeTest.getBeforeResult())
                        .beforeWin(beforeTest.getBeforeWin())
                        .build())
                .collect(Collectors.toList());

        return CustomApiResponse.createSuccess(
                HttpStatus.OK.value(),
                beforeTests,
                "식사 전 테스트 결과 조회 성공"
        );
    }
}

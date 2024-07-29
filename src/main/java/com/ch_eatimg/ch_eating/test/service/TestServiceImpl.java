package com.ch_eatimg.ch_eating.test.service;

import com.ch_eatimg.ch_eating.domain.Test;
import com.ch_eatimg.ch_eating.test.dto.TestReqDto;
import com.ch_eatimg.ch_eating.test.dto.TestResDto;
import com.ch_eatimg.ch_eating.test.repository.TestRepository;
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
public class TestServiceImpl implements TestService {

    private final TestRepository testRepository;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public CustomApiResponse<TestResDto> createTest(HttpServletRequest request, TestReqDto dto) {
        String userId = jwtTokenProvider.getUsername(jwtTokenProvider.resolveToken(request));
        User user = userRepository.findByUserId(userId).orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        //Test 엔티티 생성
        Test test = Test.builder()
                .userId(user)
                .testName(dto.getTestName())
                .testResult(dto.getTestResult())
                .testWin(null) // 기본값으로 null 설정
                .build();

        testRepository.save(test);

        // 응답 DTO 생성
        TestResDto responseDto = TestResDto.builder()
                .testId(test.getTestId())
                .userId(userId)
                .testName(test.getTestName())
                .testResult(test.getTestResult())
                .testWin(test.getTestWin())
                .build();

        return CustomApiResponse.createSuccess(
                HttpStatus.CREATED.value(),
                responseDto,
                "테스트 결과 등록 성공"
        );
    }

    @Override
    public CustomApiResponse<List<TestResDto>> getTestsByUser(HttpServletRequest request) {
        String userId = jwtTokenProvider.getUsername(jwtTokenProvider.resolveToken(request));
        User user = userRepository.findByUserId(userId).orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        List<TestResDto> afterTests = testRepository.findByUserId(user).stream()
                .map(afterTest -> TestResDto.builder()
                        .testId(afterTest.getTestId())
                        .userId(userId)
                        .testName(afterTest.getTestName())
                        .testResult(afterTest.getTestResult())
                        .testWin(afterTest.getTestWin())
                        .build())
                .collect(Collectors.toList());

        return CustomApiResponse.createSuccess(
                HttpStatus.OK.value(),
                afterTests,
                "테스트 결과 조회 성공"
        );
    }
}

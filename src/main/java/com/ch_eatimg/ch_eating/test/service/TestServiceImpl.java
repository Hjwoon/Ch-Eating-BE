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
        try {
            String userId = jwtTokenProvider.getUsername(jwtTokenProvider.resolveToken(request));
            User user = userRepository.findByUserId(userId)
                    .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

            Test test = Test.builder()
                    .userId(user)
                    .testName(dto.getTestName())
                    .testResult(dto.getTestResult())
                    .testWin(null)
                    .build();

            test.validateFields();

            testRepository.save(test);

            TestResDto responseDto = TestResDto.builder()
                    .userId(userId)
                    .testId(test.getTestId())
                    .testName(test.getTestName())
                    .testResult(test.getTestResult())
                    .testWin(test.getTestWin())
                    .build();

            return CustomApiResponse.createSuccess(
                    HttpStatus.CREATED.value(),
                    responseDto,
                    "테스트 결과 등록 성공"
            );
        } catch (IllegalArgumentException e) {
            return CustomApiResponse.createFailWithout(
                    HttpStatus.BAD_REQUEST.value(),
                    "유효하지 않은 데이터입니다: " + e.getMessage()
            );
        } catch (Exception e) {
            return CustomApiResponse.createFailWithout(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "서버 오류가 발생했습니다: " + e.getMessage()
            );
        }
    }

    @Override
    public CustomApiResponse<List<TestResDto>> getTestsByUser(HttpServletRequest request) {
        try {
            String userId = jwtTokenProvider.getUsername(jwtTokenProvider.resolveToken(request));
            User user = userRepository.findByUserId(userId)
                    .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

            List<TestResDto> tests = testRepository.findByUserId(user).stream()
                    .map(test -> TestResDto.builder()
                            .userId(userId)
                            .testId(test.getTestId())
                            .testName(test.getTestName())
                            .testResult(test.getTestResult())
                            .testWin(test.getTestWin())
                            .build())
                    .collect(Collectors.toList());

            return CustomApiResponse.createSuccess(
                    HttpStatus.OK.value(),
                    tests,
                    "테스트 결과 조회 성공"
            );
        } catch (Exception e) {
            return CustomApiResponse.createFailWithout(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "서버 오류가 발생했습니다: " + e.getMessage()
            );
        }
    }
}

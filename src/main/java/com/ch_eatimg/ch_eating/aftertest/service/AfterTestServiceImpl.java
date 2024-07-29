package com.ch_eatimg.ch_eating.aftertest.service;

import com.ch_eatimg.ch_eating.aftertest.dto.AfterTestReqDto;
import com.ch_eatimg.ch_eating.aftertest.dto.AfterTestResDto;
import com.ch_eatimg.ch_eating.aftertest.repository.AfterTestRepository;
import com.ch_eatimg.ch_eating.domain.AfterTest;
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
public class AfterTestServiceImpl implements AfterTestService {

    private final AfterTestRepository afterTestRepository;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public CustomApiResponse<AfterTestResDto> createAfterTest(HttpServletRequest request, AfterTestReqDto dto) {
        String userId = jwtTokenProvider.getUsername(jwtTokenProvider.resolveToken(request));
        User user = userRepository.findByUserId(userId).orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        // AfterTest 엔티티 생성
        AfterTest afterTest = AfterTest.builder()
                .userId(user)
                .afterResult(dto.getAfterResult())
                .afterWin(null) // 기본값으로 null 설정
                .build();

        afterTestRepository.save(afterTest);

        // 응답 DTO 생성
        AfterTestResDto responseDto = AfterTestResDto.builder()
                .afterId(afterTest.getAfterId())
                .userId(userId)
                .afterResult(afterTest.getAfterResult())
                .afterWin(afterTest.getAfterWin())
                .build();

        return CustomApiResponse.createSuccess(
                HttpStatus.CREATED.value(),
                responseDto,
                "식사 후 테스트 결과 등록 성공"
        );
    }

    @Override
    public CustomApiResponse<List<AfterTestResDto>> getAfterTestsByUser(HttpServletRequest request) {
        String userId = jwtTokenProvider.getUsername(jwtTokenProvider.resolveToken(request));
        User user = userRepository.findByUserId(userId).orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        List<AfterTestResDto> afterTests = afterTestRepository.findByUserId(user).stream()
                .map(afterTest -> AfterTestResDto.builder()
                        .afterId(afterTest.getAfterId())
                        .userId(userId)
                        .afterResult(afterTest.getAfterResult())
                        .afterWin(afterTest.getAfterWin())
                        .build())
                .collect(Collectors.toList());

        return CustomApiResponse.createSuccess(
                HttpStatus.OK.value(),
                afterTests,
                "식사 후 테스트 결과 조회 성공"
        );
    }
}

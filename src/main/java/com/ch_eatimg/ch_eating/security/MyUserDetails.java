//package com.ch_eatimg.ch_eating.security;
//
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Service
//@RequiredArgsConstructor
//public class MyUserDetails implements UserDetailsService {
//
//    // 사용자 정보를 가져오기 위해 UserRepository를 주입
//    private final UserRepository userRepository;
//
//    // 로그인 시 사용자 정보를 가져오는 메소드
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//
//        // 사용자 정보를 가져오는 메소드
//        User user = userRepository.findByUserId(username).orElseThrow(RuntimeException::new);
//
//        // 사용자의 권한을 가져와 GrantedAuthority로 변환
//        List<GrantedAuthority> authorities = user.getUserRoles().stream()
//                .map(userRole -> new SimpleGrantedAuthority(userRole.getRole().getRoleName().name()))
//                .collect(Collectors.toList());
//
//        // UserDetails를 구현한 User 객체를 반환
//        return  new org.springframework.security.core.userdetails.User(
//                user.getUserId(), user.getPassword(), authorities
//        );
//    }
//}

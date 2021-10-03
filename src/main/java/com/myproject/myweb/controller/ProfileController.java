package com.myproject.myweb.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class ProfileController {
    private final Environment env;

    @GetMapping("/profile")
    public String profile(){
        List<String> profiles = Arrays.asList(env.getActiveProfiles());
        List<String> realProfiles = Arrays.asList("real", "real1", "real2");
        String defaultProfile = profiles.isEmpty()? "default" : profiles.get(0);
        return profiles.stream()
                .filter(realProfiles::contains)
                .findAny()
                .orElse(defaultProfile);
    }

    // java -jar 실행 시
    // application.properties와 application-${profile}.properties 설정 파일로 명시하고
    // active profile도 지정

    // 공통 모듈 일반적으로 spring.profiles.include로
    // 한 방에는 EnvironmentPostProcessor으로 가능,
    // 또는 Application 파일을 한 단계 상위 위치에 넣으면 base package로 component scan으로도 가능
    // 또는 ...

    // 보안을 위해 application 계층의 모듈에서는 implementation 활용.. 자세한 내용은 게시글에서 확인
}

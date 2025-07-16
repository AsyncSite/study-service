package com.asyncsite.studyservice.config;

import com.asyncsite.studyservice.domain.model.Study;
import com.asyncsite.studyservice.domain.port.out.NotificationPort;
import com.asyncsite.studyservice.domain.port.out.StudyRepository;
import com.asyncsite.studyservice.domain.service.StudyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StudyConfiguration {

    @Bean
    public StudyService studyService(StudyRepository studyRepository, NotificationPort notificationPort) {
        return new StudyService(studyRepository, notificationPort);
    }

    @Bean
    CommandLineRunner initMockData(@Autowired StudyRepository studyRepository) {
        return args -> {
            Study study1 = new Study("Spring Boot 완전 정복", "Spring Boot 프레임워크를 깊이 있게 학습하는 스터디", "user001");
            Study study2 = new Study("알고리즘 문제 해결", "코딩 테스트 대비 알고리즘 문제를 함께 풀어보는 스터디", "user002");
            Study study3 = new Study("React 실전 프로젝트", "React를 이용한 실제 프로젝트 개발 스터디", "user003");

            studyRepository.save(study1);
            studyRepository.save(study2);
            studyRepository.save(study3);

            System.out.println("🎯 Mock 데이터 초기화 완료!");
            System.out.println("   - 총 " + studyRepository.findAll().size() + "개의 스터디가 등록되었습니다.");
        };
    }
}
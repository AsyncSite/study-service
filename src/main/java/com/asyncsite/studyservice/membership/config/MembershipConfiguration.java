package com.asyncsite.studyservice.membership.config;

import com.asyncsite.studyservice.membership.domain.port.out.MemberRepository;
import com.asyncsite.studyservice.membership.domain.service.MembershipDomainService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MembershipConfiguration {
    
    @Bean
    public MembershipDomainService membershipDomainService(MemberRepository memberRepository) {
        return new MembershipDomainService(memberRepository);
    }
}
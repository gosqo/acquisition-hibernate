package org.flyinheron.hibernate;

import org.flyinheron.hibernate.domain.MemberService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class HibernatePractice {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(HibernatePractice.class, args);

        MemberService memberService = context.getBean(MemberService.class);

        memberService.persistEmbeddedMember();
    }
}

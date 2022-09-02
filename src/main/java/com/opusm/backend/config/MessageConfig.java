package com.opusm.backend.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;

@Configuration
public class MessageConfig {

    @Bean
    public MessageSource messageSource() {

        var messageSource = new ResourceBundleMessageSource();  //Java 10 이상부터 var 지원
        // MessageSource messageSource = new ResourceBundleMessageSource();  //Java 10 이전
        messageSource.setBasename("messages");  //메시지 리소스 번들 지정
        messageSource.setDefaultEncoding("UTF-8");  //문자 집합 설정 - 한글 깨짐 방지

        return messageSource;
    }
}

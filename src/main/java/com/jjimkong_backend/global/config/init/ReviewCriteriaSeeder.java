package com.jjimkong_backend.global.config.init;

import com.jjimkong_backend.domain.reviews.entity.ReviewCriteria;
import com.jjimkong_backend.domain.reviews.repository.ReviewCriteriaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 평가 기준(ReviewCriteria) 고정 시드.
 * 앱 시작 시 기준이 하나도 없으면 기본 기준들을 삽입한다(이미 있으면 아무것도 하지 않음).
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class ReviewCriteriaSeeder implements ApplicationRunner {

    private static final List<String> DEFAULT_CRITERIA = List.of("맛", "분위기", "서비스", "가성비", "청결");

    private final ReviewCriteriaRepository reviewCriteriaRepository;

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        if (reviewCriteriaRepository.count() > 0) {
            return;
        }
        DEFAULT_CRITERIA.forEach(name -> reviewCriteriaRepository.save(new ReviewCriteria(name)));
        log.info("ReviewCriteria 기본 기준 시드 완료: {}", DEFAULT_CRITERIA);
    }
}

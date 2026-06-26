package com.jjimkong_backend.api.service.review.impl;

import com.jjimkong_backend.api.service.review.ReviewCriteriaService;
import com.jjimkong_backend.api.service.review.dto.request.ReviewCriteriaSaveRequest;
import com.jjimkong_backend.api.service.review.dto.request.ReviewCriteriaUpdateRequest;
import com.jjimkong_backend.api.service.review.dto.response.ReviewCriteriaResponse;
import com.jjimkong_backend.domain.reviews.entity.ReviewCriteria;
import com.jjimkong_backend.domain.reviews.repository.ReviewCriteriaRepository;
import com.jjimkong_backend.global.response.exception.BadRequestException;
import com.jjimkong_backend.global.response.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewCriteriaServiceImpl implements ReviewCriteriaService {

    private final ReviewCriteriaRepository reviewCriteriaRepository;

    @Override
    public List<ReviewCriteriaResponse> getCriteriaList() {
        return reviewCriteriaRepository.findAll().stream()
                .map(ReviewCriteriaResponse::from)
                .toList();
    }

    @Override
    @Transactional
    public Long saveCriteria(ReviewCriteriaSaveRequest request) {
        return reviewCriteriaRepository.save(request.toEntity()).getId();
    }

    @Override
    @Transactional
    public Long updateCriteria(Long criteriaId, ReviewCriteriaUpdateRequest request) {
        ReviewCriteria criteria = findCriteriaById(criteriaId);
        criteria.update(request.name());
        return criteriaId;
    }

    @Override
    @Transactional
    public void deleteCriteria(Long criteriaId) {
        ReviewCriteria criteria = findCriteriaById(criteriaId);
        criteria.delete();
    }

    private ReviewCriteria findCriteriaById(Long criteriaId) {
        return reviewCriteriaRepository.findById(criteriaId)
                .orElseThrow(() -> new BadRequestException(ExceptionCode.NOT_FOUND_REVIEW_CRITERIA));
    }
}

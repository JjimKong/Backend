package com.jjimkong_backend.api.service.review;

import com.jjimkong_backend.api.service.review.dto.request.ReviewCriteriaSaveRequest;
import com.jjimkong_backend.api.service.review.dto.request.ReviewCriteriaUpdateRequest;
import com.jjimkong_backend.api.service.review.dto.response.ReviewCriteriaResponse;

import java.util.List;

public interface ReviewCriteriaService {

    List<ReviewCriteriaResponse> getCriteriaList();

    Long saveCriteria(ReviewCriteriaSaveRequest request);

    Long updateCriteria(Long criteriaId, ReviewCriteriaUpdateRequest request);

    void deleteCriteria(Long criteriaId);
}

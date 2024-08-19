package vn.jully.website_selling_technology_backend.services.feedback;

import vn.jully.website_selling_technology_backend.dtos.FeedBackDTO;
import vn.jully.website_selling_technology_backend.responses.feedback.FeedBackResponse;

import java.util.List;

public interface IFeedBackService {
    FeedBackResponse insertFeedBack (FeedBackDTO feedBackDTO) throws Exception;

    FeedBackResponse updateFeedBack (Long id, FeedBackDTO feedBackDTO) throws Exception;

    FeedBackResponse getFeedBackById (long id) throws Exception;

    List<FeedBackResponse> getFeedBackByProductId  (long productId);

    List<FeedBackResponse> getFeedBackByUserId (long userId);

    void deleteFeedBackById (long id);
}

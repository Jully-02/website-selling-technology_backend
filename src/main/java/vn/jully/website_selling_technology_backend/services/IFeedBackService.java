package vn.jully.website_selling_technology_backend.services;

import vn.jully.website_selling_technology_backend.dtos.FeedBackDTO;
import vn.jully.website_selling_technology_backend.entities.FeedBack;
import vn.jully.website_selling_technology_backend.exceptions.DataNotFoundException;
import vn.jully.website_selling_technology_backend.responses.FeedBackResponse;

import java.util.List;

public interface IFeedBackService {
    FeedBackResponse insertFeedBack (FeedBackDTO feedBackDTO) throws Exception;

    FeedBackResponse updateFeedBack (Long id, FeedBackDTO feedBackDTO) throws Exception;

    FeedBackResponse getFeedBackById (long id) throws Exception;

    List<FeedBackResponse> getFeedBackByProductId  (long productId);

    List<FeedBackResponse> getFeedBackByUserId (long userId);

    void deleteFeedBackById (long id);
}

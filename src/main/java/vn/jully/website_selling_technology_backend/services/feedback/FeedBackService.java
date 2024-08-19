package vn.jully.website_selling_technology_backend.services.feedback;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import vn.jully.website_selling_technology_backend.dtos.FeedBackDTO;
import vn.jully.website_selling_technology_backend.entities.FeedBack;
import vn.jully.website_selling_technology_backend.entities.Product;
import vn.jully.website_selling_technology_backend.entities.User;
import vn.jully.website_selling_technology_backend.exceptions.DataNotFoundException;
import vn.jully.website_selling_technology_backend.repositories.FeedBackRepository;
import vn.jully.website_selling_technology_backend.repositories.ProductRepository;
import vn.jully.website_selling_technology_backend.repositories.UserRepository;
import vn.jully.website_selling_technology_backend.responses.feedback.FeedBackResponse;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FeedBackService implements IFeedBackService {
    private final FeedBackRepository feedBackRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;

    @Override
    public FeedBackResponse insertFeedBack(FeedBackDTO feedBackDTO) throws Exception {
        User userExisting = userRepository.findById(feedBackDTO.getUserId())
                .orElseThrow(() -> new DataNotFoundException("Cannot find User by ID = " + feedBackDTO.getUserId()));
        Product productExisting = productRepository.findById(feedBackDTO.getProductId())
                .orElseThrow(() -> new DataNotFoundException("Cannot find Product by ID = " + feedBackDTO.getProductId()));

        FeedBack feedBack = modelMapper.map(feedBackDTO, FeedBack.class);
        feedBack.setUser(userExisting);
        feedBack.setProduct(productExisting);

        feedBackRepository.save(feedBack);

        FeedBackResponse feedBackResponse = modelMapper.map(feedBack, FeedBackResponse.class);
        feedBackResponse.setProductId(feedBack.getProduct().getId());
        feedBackResponse.setUserId(feedBack.getUser().getId());
        feedBackResponse.setName(feedBack.getUser().getFullName());
        return feedBackResponse;
    }

    @Override
    public FeedBackResponse updateFeedBack(Long id, FeedBackDTO feedBackDTO) throws Exception {
        FeedBack feedBackExisting = feedBackRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Cannot find Feedback by ID = " + id));

        User userExisting = userRepository.findById(feedBackDTO.getUserId())
                .orElseThrow(() -> new DataNotFoundException("Cannot find User by ID = " + feedBackDTO.getUserId()));

        Product productExisting =productRepository.findById(feedBackDTO.getProductId())
                .orElseThrow(() -> new DataNotFoundException("Cannot find Product by ID = " + feedBackDTO.getProductId()));

        modelMapper.map(feedBackDTO, feedBackExisting);
        feedBackRepository.save(feedBackExisting);

        FeedBackResponse feedBackResponse = modelMapper.map(feedBackExisting, FeedBackResponse.class);
        feedBackResponse.setUserId(feedBackExisting.getUser().getId());
        feedBackResponse.setProductId(feedBackExisting.getProduct().getId());
        feedBackResponse.setName(feedBackExisting.getUser().getFullName());
        return feedBackResponse;
    }

    @Override
    public FeedBackResponse getFeedBackById(long id) throws Exception {
        FeedBack feedBack = feedBackRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Cannot find Feedback by ID = " + id));

        FeedBackResponse feedBackResponse = modelMapper.map(feedBack, FeedBackResponse.class);
        feedBackResponse.setUserId(feedBack.getUser().getId());
        feedBackResponse.setProductId(feedBack.getProduct().getId());
        feedBackResponse.setName(feedBack.getUser().getFullName());
        return feedBackResponse;
    }

    @Override
    public List<FeedBackResponse> getFeedBackByProductId(long productId) {
        List<FeedBack> feedBacks = feedBackRepository.findByProductId(productId);

        return feedBacks.stream()
                .map(feedBack -> {
                    FeedBackResponse feedBackResponse = modelMapper.map(feedBack, FeedBackResponse.class);
                    feedBackResponse.setUserId(feedBack.getUser().getId());
                    feedBackResponse.setProductId(feedBack.getProduct().getId());
                    feedBackResponse.setName(feedBack.getUser().getFullName());
                    return feedBackResponse;
                }).toList();
    }

    @Override
    public List<FeedBackResponse> getFeedBackByUserId(long userId) {
        List<FeedBack> feedBacks = feedBackRepository.findByUserId(userId);

        return feedBacks.stream()
                .map(feedBack -> {
                    FeedBackResponse feedBackResponse = modelMapper.map(feedBack, FeedBackResponse.class);
                    feedBackResponse.setUserId(feedBack.getUser().getId());
                    feedBackResponse.setProductId(feedBack.getProduct().getId());
                    feedBackResponse.setName(feedBack.getUser().getFullName());
                    return feedBackResponse;
                }).toList();
    }

    @Override
    public void deleteFeedBackById(long id) {
        feedBackRepository.deleteById(id);
    }
}

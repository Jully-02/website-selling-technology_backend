package vn.jully.website_selling_technology_backend.services;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import vn.jully.website_selling_technology_backend.dtos.PaymentMethodDTO;
import vn.jully.website_selling_technology_backend.entities.PaymentMethod;
import vn.jully.website_selling_technology_backend.exceptions.DataNotFoundException;
import vn.jully.website_selling_technology_backend.repositories.PaymentMethodRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentMethodService implements IPaymentMethodService{
    private final PaymentMethodRepository paymentMethodRepository;
    private final ModelMapper modelMapper;
    @Override
    public PaymentMethod insertPaymentMethod(PaymentMethodDTO paymentMethodDTO) {
        PaymentMethod paymentMethod = modelMapper.map(paymentMethodDTO, PaymentMethod.class);
        return paymentMethodRepository.save(paymentMethod);
    }

    @Override
    public PaymentMethod updatePaymentMethod(Long id, PaymentMethodDTO paymentMethodDTO) throws DataNotFoundException {
        PaymentMethod existingPaymentMethod = getPaymentMethod(id);
        modelMapper.map(paymentMethodDTO, existingPaymentMethod);
        paymentMethodRepository.save(existingPaymentMethod);
        return existingPaymentMethod;
    }

    @Override
    public PaymentMethod getPaymentMethod(Long id) throws DataNotFoundException {
        return paymentMethodRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Cannot find Payment method with ID = " + id));
    }

    @Override
    public List<PaymentMethod> getAllPaymentMethod() {
         return paymentMethodRepository.findAll();
    }

    @Override
    public void deletePaymentMethod (Long id) {
        paymentMethodRepository.deleteById(id);
    }
}

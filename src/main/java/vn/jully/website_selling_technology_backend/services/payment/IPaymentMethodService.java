package vn.jully.website_selling_technology_backend.services.payment;

import vn.jully.website_selling_technology_backend.dtos.PaymentMethodDTO;
import vn.jully.website_selling_technology_backend.entities.PaymentMethod;
import vn.jully.website_selling_technology_backend.exceptions.DataNotFoundException;

import java.util.List;

public interface IPaymentMethodService {

    PaymentMethod insertPaymentMethod (PaymentMethodDTO paymentMethodDTO);

    PaymentMethod updatePaymentMethod (Long id, PaymentMethodDTO paymentMethodDTO) throws DataNotFoundException;

    PaymentMethod getPaymentMethod (Long id) throws DataNotFoundException;

    List<PaymentMethod> getAllPaymentMethod ();

    void deletePaymentMethod (Long id);
}

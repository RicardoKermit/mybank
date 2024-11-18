package org.example.services;

import org.example.data.CustomerRepository;
import org.example.exceptions.UnexistingCustomerException;
import org.example.models.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomersService {

    @Autowired
    private CustomerRepository customerRepository;

    public List<Customer> getAll() {
        return customerRepository.findAll();
    }

    public Customer get(Integer id) throws UnexistingCustomerException {
        return customerRepository
                .findById(id)
                .orElseThrow(()-> new UnexistingCustomerException());
    }

    public Customer add(Customer customer) {
        return customerRepository.save(customer);
    }

    public Customer update(Customer customer) throws UnexistingCustomerException {
        if (!customerRepository.existsById(customer.getId())) {
            throw new UnexistingCustomerException();
        }
        return customerRepository.save(customer);
    }

    public void delete(Integer id) throws UnexistingCustomerException {
        if (!customerRepository.existsById(id)) {
            throw new UnexistingCustomerException();
        }
        customerRepository.deleteById(id);
    }
}

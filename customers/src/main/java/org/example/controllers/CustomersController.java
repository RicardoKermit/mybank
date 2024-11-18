package org.example.controllers;

import org.example.data.CustomerRepository;
import org.example.dtos.CustomerDTO;
import org.example.exceptions.UnexistingCustomerException;
import org.example.models.Customer;
import org.example.services.CustomersService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/customers")
public class CustomersController {

    @Autowired
    private CustomersService customerService;
    private CustomerRepository customerRepository;

    private ModelMapper modelMapper;

    public CustomersController() {modelMapper = new ModelMapper();}

    @GetMapping("/landing")
    public ResponseEntity<String> landing(){
        return ResponseEntity.status(HttpStatus.OK).body("Customer service is working fine....");
    }

    @GetMapping
    ResponseEntity<List<CustomerDTO>> getAll(){
        List<Customer> customers = customerService.getAll();
        return ResponseEntity.status(HttpStatus.OK).body(
                customers.stream()
                        .map(c->modelMapper.map(c, CustomerDTO.class))
                        .collect(Collectors.toList())
        );
    }

    @GetMapping("/{id}")
    ResponseEntity<?> get(@PathVariable Integer id){
        Customer customer;
        try {
            customer=customerService.get(id);
        } catch (UnexistingCustomerException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

        return ResponseEntity.status(HttpStatus.OK).body(modelMapper.map(customer, CustomerDTO.class));

    }

    @PostMapping
    ResponseEntity<?> add(@RequestBody CustomerDTO customerDTO){
        Customer newCustomer;
        try {
            newCustomer=customerService.add(modelMapper.map(customerDTO, Customer.class));
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

        return new ResponseEntity<>(modelMapper.map(newCustomer, CustomerDTO.class), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    ResponseEntity<?> update(@PathVariable Integer id, @RequestBody CustomerDTO customerDTO){
        Customer updatecustomer;

        if (customerDTO.getId()!=id){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("(RS) - Customer update id does not match");
        }
        try {
            updatecustomer=customerService.update(modelMapper.map(customerDTO,Customer.class));
        } catch (UnexistingCustomerException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

        return new ResponseEntity<>(modelMapper.map(updatecustomer, CustomerDTO.class), HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    ResponseEntity<?> delete(@PathVariable Integer id){
        try {
            customerService.delete(id);
        }catch (UnexistingCustomerException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

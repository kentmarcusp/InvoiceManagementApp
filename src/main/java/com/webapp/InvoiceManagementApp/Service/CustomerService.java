package com.webapp.InvoiceManagementApp.Service;

import com.webapp.InvoiceManagementApp.Model.Customer;
import com.webapp.InvoiceManagementApp.Model.Role;
import com.webapp.InvoiceManagementApp.Repository.CustomerRepository;
import com.webapp.InvoiceManagementApp.Repository.RoleRepository;
import java.sql.Date;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CustomerService implements UserDetailsService {

    private final CustomerRepository customerRepository;
    private final RoleRepository roleRepository;

    public List<Customer> getCustomers() {
        return customerRepository.findAll();
    }

    public Optional<Customer> findCustomerById(long id) {
        return customerRepository.findById(id);

    }
    public Optional<Customer> findUserByEmail(String email) {
        return customerRepository.findCustomerByEmail(email);
    }
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return customerRepository.findCustomerByEmail(email)
            .map(customer -> new User(
                email,
                customer.getPassword(),
                List.of(new SimpleGrantedAuthority(customer.getRole().getName()))
            ))
            .orElseThrow(() -> new UsernameNotFoundException("Email '%s' not found"));
    }

    public Boolean isValidEmail(String email) {
        return email.contains("@");
    }

    public Customer saveCustomer(Customer customer) {;
        var now = Date.from(Instant.now());
        var role = new Role();
        role.setName("ADMIN");
        role.setCreatedAt(now);
        var savedRole = roleRepository.save(role);

        customer.setCreated_at(now);
        customer.setUpdated_at(now);
        customer.setRole(savedRole);
        return customerRepository.save(customer);
    }
}

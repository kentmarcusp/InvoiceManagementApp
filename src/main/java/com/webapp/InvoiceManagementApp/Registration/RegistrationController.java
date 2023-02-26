package com.webapp.InvoiceManagementApp.Registration;

import com.webapp.InvoiceManagementApp.Model.Customer;
import com.webapp.InvoiceManagementApp.Security.JwtTokenUtil;
import com.webapp.InvoiceManagementApp.Service.CustomerService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/registration")
@AllArgsConstructor
public class RegistrationController {

    private CustomerService customerService;
    private BCryptPasswordEncoder passwordEncoder;

    private JwtTokenUtil jwtTokenUtil;

    @PostMapping
    public Customer register(@RequestBody RegistrationRequest request) {
        log.info("Registering - email: {}, password: {}", request.getEmail(), request.getPassword());

        var customer = new Customer();
        customer.setEmail(request.getEmail());
        customer.setPassword(passwordEncoder.encode(request.getPassword()));
        return customerService.saveCustomer(customer);
    }

    @PostMapping(path = "/login")
    public ResponseEntity<String> login(@RequestBody RegistrationRequest request) {
        log.info("Logging in - email: {}, password: {}", request.getEmail(), request.getPassword());

        var user = customerService.loadUserByUsername(request.getEmail());

        if (user == null || !passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            log.warn("Bad credentials");
            return ResponseEntity.badRequest().build();
        }

        var token = jwtTokenUtil.generate(request.getEmail());
        return ResponseEntity.ok(token);
    }

    @GetMapping(path = "/secured")
    public String securedEndpoint() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getPrincipal().toString();
    }
}

package com.webapp.InvoiceManagementApp.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table(name= "customer")
@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class Customer implements UserDetails {
    //maybe use sequences?
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id")
    private long customer_id;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "created_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date created_at;

    @Column(name = "updated_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date updated_at;

    @Column(name = "iban")
    private String iban;

    @Column(name = "bankName")
    private String bankName;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="role_id", referencedColumnName = "role_id", nullable = false)
    private Role role;


    /*  @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY)
        private List<Invoice> invoiceList;

        @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY)
        private List<Invoice> invoiceList;*/
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role.getName());
        return Collections.singletonList(authority);
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return "";
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }



/*
    public static Customer fromAuthRequestDto(AuthRequestDto registerRequestDto, PasswordEncoder passwordEncoder) {
        return new Customer(10,
                registerRequestDto.getEmail(),
                passwordEncoder.encode(registerRequestDto.getPassword()),
                new Date(),
                new Date(),
                null,
                null,
                Role.REGULAR_USER);
    }
*/


}

package com.example.Pfeproject.entity;

import com.example.Pfeproject.dto.UpdateCustomerDto;
import com.example.Pfeproject.dto.UserDto;
import com.example.Pfeproject.enums.UserRole;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Data
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private  String name;
    private String email;
    private String password;
    private UserRole userRole;



    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(userRole.name()));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public void getName(String name) {}

    public UserDto getUserDto() {
        UserDto userDto = new UserDto();
        userDto.setId(id);
        userDto.setName(name);
        userDto.setPassword(password);
        userDto.setEmail(email);
        return userDto;
    }
    public UpdateCustomerDto getUpdateCustomerDto(){
        UpdateCustomerDto updateCustomerDto = new UpdateCustomerDto();
        updateCustomerDto.setId(id);
        updateCustomerDto.setName(name);
        updateCustomerDto.setEmail(email);
        return updateCustomerDto;
    }
}

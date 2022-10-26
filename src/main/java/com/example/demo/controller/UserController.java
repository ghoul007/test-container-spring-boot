package com.example.demo.controller;

import com.example.demo.model.UserEnity;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

  @Autowired
  private UserRepository repository;

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public List<UserEnity> getAllUsers(){
    return repository.findAll();
  }
}

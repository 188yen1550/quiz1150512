package com.example.quiz1150512.Dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.quiz1150512.entity.User;

@Repository
public interface UserDao extends JpaRepository<User, Long>{

}

package com.revature.Project2.repository;

import com.revature.Project2.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<User, Integer> {
    //find a user by their userName
    User findByUserName(String username);
}

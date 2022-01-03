package com.revature.Project2.repository;

import com.revature.Project2.models.Post;
import com.revature.Project2.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepo extends JpaRepository<Post, Integer> {
    List<Post> findAllUserById(Integer id);
    List<Post> findAllPostById(Integer id);
}

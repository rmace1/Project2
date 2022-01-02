package com.revature.Project2.repository;

import com.revature.Project2.models.Post;
import com.revature.Project2.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepo extends JpaRepository<Post, Integer> {
    List<Post> findAllByUserId(Integer id);
    List<Post> findAllByPostId(Integer id);
}

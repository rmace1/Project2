package com.revature.Project2.repository;

import com.revature.Project2.models.Post;
import com.revature.Project2.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepo extends JpaRepository<Post, Integer> {
    @Query(value = "SELECT * FROM posts p WHERE p.author_id = :id AND p.original_post_id IS null", nativeQuery = true)
    List<Post> findAllUserByauthorId(@Param("id") Integer id);

    //https://www.petrikainulainen.net/programming/spring-framework/spring-data-jpa-tutorial-introduction-to-query-methods/
    @Query(value = "SELECT * FROM posts p WHERE p.original_post_id IS null", nativeQuery = true)
    List<Post> findAllOriginalPosts();
}

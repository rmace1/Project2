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
    List<Post> findAllUserByauthorId(Integer id);

    //https://www.petrikainulainen.net/programming/spring-framework/spring-data-jpa-tutorial-introduction-to-query-methods/
    @Query(value = "SELECT * FROM posts p WHERE p.original_post_id = :id OR p.id = :id", nativeQuery = true)
    List<Post> findAllPostByIdOroriginalPostId(@Param("id")Integer id);
}

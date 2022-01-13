package com.revature.Project2.repository;

import com.revature.Project2.models.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepoPaged extends PagingAndSortingRepository<Post, Integer> {
    @Query(value = "SELECT * FROM posts p WHERE p.original_post_id IS null ORDER BY p.id DESC", nativeQuery = true)
    Page<Post> findAllOriginalPostsPaged(Pageable pageable);
}

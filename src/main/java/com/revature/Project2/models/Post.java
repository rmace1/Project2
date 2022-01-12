package com.revature.Project2.models;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.bytebuddy.dynamic.loading.InjectionClassLoader;
import org.springframework.beans.factory.annotation.Value;

import javax.persistence.*;
import java.io.File;
import java.sql.Timestamp;
import java.util.Comparator;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "posts")
public class Post implements Comparable<Post> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String message;

    @Transient
    private Boolean liked = false;

    private String picture;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm az")
    @Column(columnDefinition = "TIMESTAMP default now()")
    private Timestamp timePosted;

    @Column(columnDefinition = "int4 default 0")
    private Integer likes;

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnoreProperties({"likes", "password"})
    private User author;

    @JsonIgnoreProperties({"author", "comments"})
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "original_post_id")
    private Post originalPost;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "originalPost")
    private List<Post> comments;

    public Post(Integer id, String message, User author) {
        this.id = id;
        this.message = message;
        this.author = author;
    }

    @Override
    public int compareTo(Post post){
        if(post.getId() < this.getId()){
            return 1;
        }
        return -1;
    }

    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", message='" + message + '\'' +
                ", liked=" + liked +
                ", picture='" + picture + '\'' +
                ", timePosted=" + timePosted +
                ", likes=" + likes +
                '}';
    }
}

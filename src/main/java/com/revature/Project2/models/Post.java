package com.revature.Project2.models;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.bytebuddy.dynamic.loading.InjectionClassLoader;

import javax.persistence.*;
import java.sql.Timestamp;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @OneToMany(mappedBy = "originalPost")
    private Integer id;

    @Column(nullable = false)
    private String message;

    private String picture;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm az")
    private Timestamp timePosted;

    private Integer likes;

    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private User author;

    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private Post originalPost;







}

package com.demosocket.blog.model;

import lombok.*;
import com.fasterxml.jackson.annotation.*;

import java.util.Date;
import javax.persistence.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"article", "user"})

@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @JsonProperty("message")
    @Column(name = "text_message")
    private String message;

    @JsonProperty("article_id")
    @JsonIdentityReference(alwaysAsId = true)
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @ManyToOne(fetch = FetchType.EAGER, targetEntity = Article.class)
    @JoinColumn(name = "article_id", referencedColumnName = "id")
    private Article article;

    @JsonProperty("author_id")
    @JsonIdentityReference(alwaysAsId = true)
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @ManyToOne(fetch = FetchType.EAGER, targetEntity = User.class)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @JsonProperty("created_at")
    @JsonFormat(pattern="dd.MM.yyyy HH:mm:ss", timezone = "GMT+3")
    @Column(name = "created_at", insertable = false, updatable = false)
    private Date createdAt;
}

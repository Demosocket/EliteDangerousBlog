package com.demosocket.blog.model;

import com.fasterxml.jackson.annotation.*;
import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"comments", "user", "tags"})

@Entity
@Table(name = "articles")
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "title")
    private String title;

    @JsonProperty("text")
    @Column(name = "article")
    private String text;

    @Column(name = "status")
    @Enumerated(value = EnumType.STRING)
    private Status status;

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

    @JsonProperty("updated_at")
    @JsonFormat(pattern="dd.MM.yyyy HH:mm:ss", timezone = "GMT+3")
    @Column(name = "updated_at", insertable = false)
    private Date updatedAt;

    @OneToMany(mappedBy = "article", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Comment> comments;

    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE})
    @JoinTable(name = "article_tag",
            joinColumns = @JoinColumn(name = "article_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private Set<Tag> tags;
}




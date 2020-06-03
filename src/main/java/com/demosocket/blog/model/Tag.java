package com.demosocket.blog.model;

import lombok.*;

import java.util.Set;
import javax.persistence.*;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"articles"})
@Table(name = "tags")
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "tag_name")
    private String name;

    @ManyToMany(mappedBy = "tags")
    private Set<Article> articles;
}

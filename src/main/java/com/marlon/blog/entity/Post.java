package com.marlon.blog.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @Column(unique = true, nullable = false)
    private String title;

    @Lob
    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;
    @Column(nullable = false)
    private String createdOn;
    @Column(nullable = false)
    private String updatedOn;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;
    @ElementCollection
    @CollectionTable(name = "post_tags", joinColumns = @JoinColumn(name = "post_id"))
    @Column(name = "tag")
    private List<String> tags;

    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", title='" + title + "'" +
                ", content='" + content + "'" +
                ", createdOn='" + createdOn + "'" +
                ", updatedOn='" + updatedOn + "'" +
                "}";
    }
}


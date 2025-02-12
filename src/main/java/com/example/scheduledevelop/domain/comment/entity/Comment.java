package com.example.scheduledevelop.domain.comment.entity;

import com.example.scheduledevelop.domain.schedule.entity.Schedule;
import com.example.scheduledevelop.domain.user.entity.User;
import com.example.scheduledevelop.global.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "comments")
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id")
    private Schedule schedule;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User users;

    @Column(nullable = false)
    @Size(max = 200)
    private String contents;

    public Comment(Schedule schedule, User user, String content) {
        this.schedule = schedule;
        this.users = user;
        this.contents = content;
    }

    public void updateContents(String contents){
        this.contents = contents;
    }
}

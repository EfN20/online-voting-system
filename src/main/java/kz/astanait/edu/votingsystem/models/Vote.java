package kz.astanait.edu.votingsystem.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@RequiredArgsConstructor
@Table(name = "votes")
public class Vote {
    @Id
    @SequenceGenerator(
            name = "votes_sequence",
            sequenceName = "votes_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "votes_sequence"
    )
    private Long id;

    @NonNull
    @ManyToOne(
            fetch = FetchType.EAGER,
            cascade = CascadeType.MERGE
    )
    @JoinColumn("user_id")
    private User user;

    @NonNull
    @ManyToOne(
            fetch = FetchType.EAGER,
            cascade = CascadeType.MERGE
    )
    @JoinColumn("option_id")
    private Option option;

    @NonNull
    @ManyToOne(
            fetch = FetchType.EAGER,
            cascade = CascadeType.MERGE
    )
    @JoinColumn(name = "question_id")
    private Question question;
}

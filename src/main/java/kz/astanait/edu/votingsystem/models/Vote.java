package kz.astanait.edu.votingsystem.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

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
    @JoinColumn(name = "user_id")
    private User user;

    @NonNull
    @ManyToOne(
            fetch = FetchType.EAGER,
            cascade = CascadeType.MERGE
    )
    @JoinColumn(name = "option_id")
    private Option option;

    @NonNull
    @ManyToOne(
            fetch = FetchType.EAGER,
            cascade = CascadeType.MERGE
    )
    @JoinColumn(name = "question_id")
    private Question question;
}

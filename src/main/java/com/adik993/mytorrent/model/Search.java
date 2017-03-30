package com.adik993.mytorrent.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@ToString(exclude = {"searchResults"})
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Search {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String query;
    private LocalDateTime timestamp;
    private Boolean success;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "search")
    @JsonIgnore
    private List<SearchResult> searchResults;
}

package com.adik993.mytorrent.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by adrian on 28/03/17.
 */
@Entity
@Getter
@Setter
@ToString(exclude = {"searchResults"})
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

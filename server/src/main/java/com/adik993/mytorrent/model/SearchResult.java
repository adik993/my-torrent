package com.adik993.mytorrent.model;

import com.adik993.tpbclient.model.Category;
import com.adik993.tpbclient.model.Torrent;
import com.adik993.tpbclient.model.TorrentQuality;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@ToString(exclude = {"search"})
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "search_id", nullable = false)
    @JsonIgnore
    private Search search;
    boolean chosen;
    Long torrentId;
    String title;
    Category category;
    String user;
    @Column(length = Short.MAX_VALUE)
    String magnetLink;
    String torrentLink;
    LocalDateTime publishDate;
    TorrentQuality quality;
    long size;
    int seeds;
    int leeches;


    public static SearchResult from(Torrent torrent, Search search) {
        SearchResult searchResult = new SearchResult();
        searchResult.setTorrentId(torrent.getId());
        searchResult.setTitle(torrent.getTitle());
        searchResult.setCategory(torrent.getCategory());
        searchResult.setUser(torrent.getUser());
        searchResult.setMagnetLink(torrent.getMagnetLink());
        searchResult.setTorrentLink(torrent.getTorrentLink());
        searchResult.setPublishDate(torrent.getPublishDate());
        searchResult.setQuality(torrent.getQuality());
        searchResult.setSize(torrent.getSize());
        searchResult.setSeeds(torrent.getSeeds());
        searchResult.setLeeches(torrent.getLeeches());
        searchResult.setSearch(search);
        return searchResult;
    }

    public static List<SearchResult> fromList(List<Torrent> torrentList, Search search) {
        return torrentList.stream().map(torrent -> from(torrent, search))
                .collect(Collectors.toList());
    }
}

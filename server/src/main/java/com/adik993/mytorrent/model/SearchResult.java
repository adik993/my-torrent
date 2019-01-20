package com.adik993.mytorrent.model;

import com.adik993.tpbclient.model.Category;
import com.adik993.tpbclient.model.Torrent;
import com.adik993.tpbclient.model.TorrentQuality;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

import static lombok.AccessLevel.NONE;

@Document
@Getter
@Setter
@ToString
@NoArgsConstructor
public class SearchResult {
    @Setter(NONE)
    private String id = new ObjectId().toString();
    private boolean chosen;
    private Long torrentId;
    private String title;
    private Category category;
    private String user;
    private String magnetLink;
    private String torrentLink;
    private LocalDateTime publishDate;
    private TorrentQuality quality;
    private long size;
    private int seeds;
    private int leeches;


    public SearchResult(Torrent torrent) {
        torrentId = torrent.getId();
        title = torrent.getTitle();
        category = torrent.getCategory();
        user = torrent.getUser();
        magnetLink = torrent.getMagnetLink();
        torrentLink = torrent.getTorrentLink();
        publishDate = torrent.getPublishDate();
        quality = torrent.getQuality();
        size = torrent.getSize();
        seeds = torrent.getSeeds();
        leeches = torrent.getLeeches();
    }
}

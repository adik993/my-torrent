package com.adik993.mytorrent.providers;

import lombok.Data;

import java.util.List;

@Data
public class Page<T> {
    final private int pageSize;
    final private int total;
    final List<T> content;
}

package com.adik993.mytorrent.providers;

import com.adik993.tpbclient.TpbClient;
import com.adik993.tpbclient.exceptions.ParseException;
import com.adik993.tpbclient.model.Category;
import com.adik993.tpbclient.model.OrderBy;
import com.adik993.tpbclient.model.Torrent;
import com.adik993.tpbclient.model.TpbResult;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TpbProvider extends TorrentsProvider {

    private final TpbClient tpbClient;
    private final List<String> categories;

    public TpbProvider(TpbClient tpbClient) {
        this.tpbClient = tpbClient;
        this.categories = Arrays.stream(Category.values())
                .map(Enum::name)
                .collect(Collectors.toList());
    }

    @Override
    public String getName() {
        return tpbClient.getHost().getHost();
    }

    @Override
    public List<String> getCategories() {
        return categories;
    }

    @Override
    public Page<Torrent> search(String query, String category, Integer page, String orderBy) throws IOException, ParseException {
        Category cat = safeEnum(Category.class, category);
        OrderBy order = safeEnum(OrderBy.class, orderBy);
        TpbResult result = tpbClient.search(query, cat, page, order);
        return new Page<>(
                result.getPageInfo().getPageSize(),
                result.getPageInfo().getTotal(),
                result.getTorrents());
    }

    private <T extends Enum<T>> T safeEnum(Class<T> clazz, String name) {
        return name != null ? Enum.valueOf(clazz, name) : null;
    }

    @Override
    public boolean checkIfUp() {
        try {
            tpbClient.search("abc", null, null, null);
            up.set(true);
        } catch (IOException | ParseException e) {
            up.set(false);
        }
        return up.get();
    }
}

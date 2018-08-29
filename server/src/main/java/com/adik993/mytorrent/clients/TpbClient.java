package com.adik993.mytorrent.clients;

import com.adik993.tpbclient.model.Category;
import com.adik993.tpbclient.model.OrderBy;
import com.adik993.tpbclient.model.Torrent;
import io.reactivex.Single;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;

@Slf4j
public class TpbClient extends TorrentClient {

    private final com.adik993.tpbclient.TpbClient tpbClient;
    private final List<String> categories;

    public TpbClient(com.adik993.tpbclient.TpbClient tpbClient) {
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
    public Single<Page<Torrent>> search(String query, String category, Integer page, String orderBy) {
        Category cat = safeEnum(Category.class, category);
        OrderBy order = safeEnum(OrderBy.class, orderBy);
        return tpbClient.search(query, cat, page, order)
                .map(result -> new Page<>(
                        result.getPageInfo().getPageSize(),
                        result.getPageInfo().getTotal(),
                        result.getTorrents()));
    }

    private <T extends Enum<T>> T safeEnum(Class<T> clazz, String name) {
        return ofNullable(name).map(n -> Enum.valueOf(clazz, n)).orElse(null);
    }

    @Override
    public Single<TorrentClient> checkIfUp() {
        return tpbClient.search("abc", null, null, null)
                .doOnSuccess(tpbResult -> log.debug("up check for {} is success and resulted with {}", this, tpbResult.getTorrents().size()))
                .doOnSuccess(tpbResult -> up.set(true))
                .doOnError(tpbClient -> up.set(false))
                .map(tpbResult -> (TorrentClient) this)
                .onErrorResumeNext(throwable -> Single.just(this));
    }
}

package com.adik993.mytorrent.clients.source;

import com.adik993.mytorrent.clients.TorrentClient;
import com.adik993.mytorrent.clients.TpbClient;
import com.adik993.tpbclient.proxy.ProxyList;
import com.adik993.tpbclient.proxy.model.Proxy;
import io.reactivex.Flowable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TpbClientSource implements TorrentClientSource {
    private final ProxyList proxyList;
    private static final Proxy tpborg = new Proxy(com.adik993.tpbclient.TpbClient.DEFAULT_HOST, .0f, true, "US", true);

    @Override
    public Flowable<TorrentClient> provide() {
        return Flowable.concat(Flowable.just(tpborg), proxyList.fetchProxies().onErrorResumeNext(Flowable.empty()))
                .map(com.adik993.tpbclient.TpbClient::fromProxy)
                .map(TpbClient::new);
    }
}

package top.dustone.cilicili.component;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.MultiMap;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientOptions;

public class WebClientHandler {
    private WebClient webClient;
    private String cookies;
    public WebClientHandler(Vertx vertx, WebClientOptions options){
        webClient = WebClient.create(vertx, options);
    }
    public void sendGetRequest(MultiMap headers,MultiMap queryParams,String requestURL, Handler<AsyncResult<HttpResponse<Buffer>>> handler){
        HttpRequest<Buffer> request = webClient.getAbs(requestURL);
        request.ssl(true);
        MultiMap header = request.headers();
        header.addAll(headers);
        header.add("Cookie",cookies);
        MultiMap params = request.queryParams();
        params.addAll(queryParams);
        request.send(handler);
    }

    public WebClient getWebClient() {
        return webClient;
    }

    public void setWebClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public String getCookies() {
        return cookies;
    }

    public void setCookies(String cookies) {
        this.cookies = cookies;
    }
}

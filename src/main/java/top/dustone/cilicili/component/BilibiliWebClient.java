package top.dustone.cilicili.component;

import io.vertx.core.MultiMap;
import io.vertx.core.Vertx;
import io.vertx.core.WorkerExecutor;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.CaseInsensitiveHeaders;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClientOptions;
import top.dustone.cilicili.bean.RuntimeParams;
import top.dustone.cilicili.util.CommonUtil;
import top.dustone.cilicili.util.ConstParam;

public class BilibiliWebClient {
    private WebClientHandler webClientHandler;
    private JinkelaCommander jinkelaCommander;
    private MultiMap commonHeaders;
    private RuntimeParams runtimeParams;
    private boolean locker;
    private Vertx vertx;
    public BilibiliWebClient(Vertx vertx,WebClientOptions options,RuntimeParams runtimeParams){
        this.runtimeParams = runtimeParams;
        this.jinkelaCommander = JinkelaCommander.getInstance();
        this.vertx = vertx;
        init(vertx,options);
        getDynamicType();
    }
    private void init(Vertx vertx,WebClientOptions options){
        webClientHandler = new WebClientHandler(vertx, options);
        webClientHandler.setCookies(runtimeParams.getBaseRequestHeaders().get("Cookie"));
        commonHeaders = new CaseInsensitiveHeaders();
        commonHeaders.addAll(runtimeParams.getBaseRequestHeaders());
    }

    public void test1() {
        MultiMap queryParams = new CaseInsensitiveHeaders();
        queryParams.set("uid","36033035");
        queryParams.set("type",runtimeParams.getDynamicType());
        queryParams.set("from","header");
        webClientHandler.sendGetRequest(commonHeaders,queryParams, ConstParam.DYNAMIC_NEW,
                ar -> {
                    if (ar.succeeded()) {
                        HttpResponse<Buffer> response = ar.result();
                        System.out.println("Received response with status code" + response.statusCode());
                        String restr = CommonUtil.gzipReplyAsString(response.body());
                        System.out.println(restr);
                    } else {
                        System.out.println("Something went wrong " + ar.cause().getMessage());
                    }
                });
    }
    private void getDynamicType() {
        MultiMap queryParams = new CaseInsensitiveHeaders();
        MultiMap headers = new CaseInsensitiveHeaders();
        headers.addAll(commonHeaders);
        headers.set("Accept","*/*");
        headers.set("Sec-Fetch-Mode", "no-cors");
        headers.set("Sec-Fetch-Site","cross-site");
        headers.set("Host","s1.hdslb.com");
        webClientHandler.sendGetRequest(headers,queryParams, ConstParam.DYNAMIC_HEADER_JS,
                ar -> {
                    if (ar.succeeded()) {
                        HttpResponse<Buffer> response = ar.result();
                        String restr = CommonUtil.gzipReplyAsString(response.body());
                        String startStr = "data:{uid:this.uid,type:";
                        int startIndex = restr.indexOf(startStr);
                        int endIndx = restr.indexOf(",",startIndex+startStr.length());
                        runtimeParams.setDynamicType(restr.substring(startIndex+startStr.length(),endIndx));
                        waitForCommand();
                    } else {
                        System.out.println("Something went wrong " + ar.cause().getMessage());
                    }
                });
    }

    public void waitForCommand(){
        vertx.executeBlocking(future -> {
            jinkelaCommander.readCommand();
        }, res -> {});
    }

    public WebClientHandler getWebClientHandler() {
        return webClientHandler;
    }

    public void setWebClientHandler(WebClientHandler webClientHandler) {
        this.webClientHandler = webClientHandler;
    }

    public MultiMap getCommonHeaders() {
        return commonHeaders;
    }

    public void setCommonHeaders(MultiMap commonHeaders) {
        this.commonHeaders = commonHeaders;
    }

    public RuntimeParams getRuntimeParams() {
        return runtimeParams;
    }

    public void setRuntimeParams(RuntimeParams runtimeParams) {
        this.runtimeParams = runtimeParams;
    }

    public boolean isLocker() {
        return locker;
    }

    public void setLocker(boolean locker) {
        this.locker = locker;
    }
}

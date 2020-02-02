package top.dustone.cilicili;

import cn.hutool.setting.dialect.Props;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.MultiMap;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.CaseInsensitiveHeaders;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientOptions;
import top.dustone.cilicili.bean.RuntimeParams;
import top.dustone.cilicili.component.WebClientHandler;
import top.dustone.cilicili.util.CommonUtil;
import top.dustone.cilicili.util.ConstParam;

public class Application extends AbstractVerticle {
    private WebClient webClient;
    private WebClientHandler webClientHandler;
    private MultiMap commonHeaders;
    private RuntimeParams runtimeParams;
    private boolean locker;

    @Override
    public void start() throws Exception {
        init();
        getDynamicType();
    }

    private void init(){
        WebClientOptions options = new WebClientOptions()
                .setUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36");
        options.setKeepAlive(true);
        webClientHandler = new WebClientHandler(vertx, options);
        webClientHandler.setCookies("_uuid=FBCF9D49-E1E8-95F2-F19B-0C1E05E8110906508infoc; buvid3=1D004197-A9D5-495B-AE3A-011801AD3A7353919infoc; LIVE_BUVID=AUTO1215797626982902; sid=8uwlv0hx; CURRENT_FNVAL=16; rpdid=|(k|k)JYJY~J0J'ul~m~YYmJJ; im_notify_type_36033035=0; CURRENT_QUALITY=112; INTVER=1; DedeUserID=36033035; DedeUserID__ckMd5=2b5eb2631560e507; SESSDATA=4c87b07d%2C1583128119%2Cf4cfd121; bili_jct=652090addf447f842aacbb63863b88a5; bp_t_offset_36033035=350898351343116101; _dfcaptcha=7ffb7a96ad14a4034200f27f4993cfba");

        commonHeaders = new CaseInsensitiveHeaders();
        commonHeaders.set("Accept", "application/json, text/plain, */*");
        commonHeaders.set("Sec-Fetch-Site", "same-site");
        commonHeaders.set("Sec-Fetch-Mode", "cors");
        commonHeaders.set("Referer", "https://t.bilibili.com/?spm_id_from=333.851.b_696e7465726e6174696f6e616c486561646572.30");
        commonHeaders.set("Accept-Encoding", "gzip, deflate, br");
        commonHeaders.set("Accept-Language", "zh-CN,zh;q=0.9");

        runtimeParams = new RuntimeParams();
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
        webClientHandler.sendGetRequest(headers,queryParams, ConstParam.DYNAMIC_HEADER_JS,
                ar -> {
                    if (ar.succeeded()) {
                        HttpResponse<Buffer> response = ar.result();
                        String restr = CommonUtil.gzipReplyAsString(response.body());
                        String startStr = "data:{uid:this.uid,type:";
                        System.out.println(restr);
                        int startIndex = restr.indexOf(startStr);
                        int endIndx = restr.indexOf(",",startIndex+startStr.length());
                        runtimeParams.setDynamicType(restr.substring(startIndex+startStr.length(),endIndx));
                        test1();
                    } else {
                        System.out.println("Something went wrong " + ar.cause().getMessage());
                    }
                });
    }

    public static void main(String[] args) {
        Vertx.vertx().deployVerticle(new Application());
    }
}

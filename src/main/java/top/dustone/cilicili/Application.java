package top.dustone.cilicili;

import io.vertx.core.*;
import io.vertx.core.http.CaseInsensitiveHeaders;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClientOptions;
import top.dustone.cilicili.bean.RuntimeParams;
import top.dustone.cilicili.component.BilibiliWebClient;
import top.dustone.cilicili.component.JinkelaCommander;
import top.dustone.cilicili.util.CommonUtil;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class Application extends AbstractVerticle {
    private BilibiliWebClient bilibiliWebClient;
    private JinkelaCommander jinkelaCommander;

    public Application(){
    }
    @Override
    public void start() throws Exception {
        vertx.executeBlocking(future -> {
            init();
        }, res -> {});
//        WebClientOptions options = new WebClientOptions()
//                .setUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36");
//        options.setKeepAlive(true);
//        bilibiliWebClient = new BilibiliWebClient(vertx,options);
    }

    private void init() {
        printLogo();
        jinkelaCommander = JinkelaCommander.getInstance();
        jinkelaCommander.setCommandRouter(this::commandRouter);
        File configFile = new File("config.json");
        RuntimeParams runtimeParams = null;
        if (configFile.exists()) {
            try {
                runtimeParams = (RuntimeParams) CommonUtil.loadObjectFromJsonFile(configFile, RuntimeParams.class, false);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (runtimeParams == null) {
                jinkelaCommander.println("解析配置文件失败，请检查config.json或者干脆干掉它后再试。");
                System.exit(1);
            }
        } else {
            runtimeParams = new RuntimeParams();
            jinkelaCommander.println("欢迎使用Bilibili for Shell");
            String requestHeaders = jinkelaCommander.readFromWindow();
            runtimeParams.setBaseRequestHeaders(CommonUtil.resolveRequestHeaders(requestHeaders));
        }
        loadConfigs(runtimeParams);
    }

    private void loadConfigs(RuntimeParams runtimeParams) {
        WebClientOptions options = new WebClientOptions()
                .setUserAgent(runtimeParams.getBaseRequestHeaders().get("User-Agent"));
        options.setKeepAlive(true);
        bilibiliWebClient = new BilibiliWebClient(vertx,options,runtimeParams);
    }

    public void commandRouter(String command){
        jinkelaCommander.println(command);
        //jinkelaCommander
    }

    private void printLogo() {
        String logo = "                     //\n" +
                "         \\\\         //\n" +
                "          \\\\       //\n" +
                "    ##DDDDDDDDDDDDDDDDDDDDDD##\n" +
                "    ## DDDDDDDDDDDDDDDDDDDD ##\n" +
                "    ## DDDDDDDDDDDDDDDDDDDD ##   ________   ___   ___        ___   ________   ___   ___        ___\n" +
                "    ## hh                hh ##   |\\   __  \\ |\\  \\ |\\  \\      |\\  \\ |\\   __  \\ |\\  \\ |\\  \\      |\\  \\\n" +
                "    ## hh    //    \\\\    hh ##   \\ \\  \\|\\ /_\\ \\  \\\\ \\  \\     \\ \\  \\\\ \\  \\|\\ /_\\ \\  \\\\ \\  \\     \\ \\  \\\n" +
                "    ## hh   //      \\\\   hh ##    \\ \\   __  \\\\ \\  \\\\ \\  \\     \\ \\  \\\\ \\   __  \\\\ \\  \\\\ \\  \\     \\ \\  \\\n" +
                "    ## hh                hh ##     \\ \\  \\|\\  \\\\ \\  \\\\ \\  \\____ \\ \\  \\\\ \\  \\|\\  \\\\ \\  \\\\ \\  \\____ \\ \\  \\\n" +
                "    ## hh      wwww      hh ##      \\ \\_______\\\\ \\__\\\\ \\_______\\\\ \\__\\\\ \\_______\\\\ \\__\\\\ \\_______\\\\ \\__\\\n" +
                "    ## hh                hh ##       \\|_______| \\|__| \\|_______| \\|__| \\|_______| \\|__| \\|_______| \\|__|\n" +
                "    ## MMMMMMMMMMMMMMMMMMMM ##\n" +
                "    ##MMMMMMMMMMMMMMMMMMMMMM##                             Bilibili in Shell --Dustone\n" +
                "         \\/            \\/";
        System.out.println(logo);
    }

    public static void main(String[] args) {
        VertxOptions vertxOptions = new VertxOptions();
        vertxOptions.setBlockedThreadCheckInterval(999999999999L);
        Vertx.vertx(vertxOptions).deployVerticle(new Application());
    }
}

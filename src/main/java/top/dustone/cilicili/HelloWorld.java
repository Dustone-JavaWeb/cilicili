package top.dustone.cilicili;

import io.webfolder.cdp.Launcher;
import io.webfolder.cdp.event.network.LoadingFinished;
import io.webfolder.cdp.event.network.ResponseReceived;
import io.webfolder.cdp.session.Session;
import io.webfolder.cdp.session.SessionFactory;
import io.webfolder.cdp.type.network.*;
import top.dustone.cilicili.util.CommonUtil;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static io.webfolder.cdp.event.Events.NetworkLoadingFinished;
import static io.webfolder.cdp.event.Events.NetworkResponseReceived;
import static java.awt.Desktop.getDesktop;
import static java.awt.Desktop.isDesktopSupported;
import static java.nio.file.Files.createTempFile;


public class HelloWorld {

    public static void main(String[] args) throws InterruptedException, IOException {
        Launcher launcher = new Launcher();
        List<String> arguments = new ArrayList<String>();
        arguments.add("--headless");
        SessionFactory factory = launcher.launch();
        Session session = factory.create();
        if (new File("test.json").exists()) {
            List<Cookie> cookies = (List<Cookie>) CommonUtil.loadObjectFromJsonFile(new File("test.json"), Cookie.class, true);
            session.getCommand().getNetwork().setCookies(CommonUtil.convertCookies(cookies));
        }
        // 配置session
//        RequestPattern requestPattern = new RequestPattern();
//        requestPattern.setUrlPattern("https://api.vc.bilibili.com/dynamic_svr/v1/dynamic_svr/dynamic_new.*?");
//        requestPattern.setInterceptionStage(InterceptionStage.HeadersReceived);
//        List<RequestPattern> requestPatterns = new ArrayList<RequestPattern>();
//        requestPatterns.add(requestPattern);
//        session.getCommand().getNetwork().setRequestInterception(requestPatterns);

        Set finished = new HashSet<>();
        session.getCommand().getNetwork().enable();
        session.addEventListener((e, d) -> {
            if (NetworkLoadingFinished.equals(e)) {
                LoadingFinished lf = (LoadingFinished) d;
                finished.add(lf.getRequestId());
            }
            if (NetworkResponseReceived.equals(e)) {
                ResponseReceived rr = (ResponseReceived) d;
                Response response = rr.getResponse();
//                if (!response.getUrl().startsWith("https://api.vc.bilibili.com/dynamic_svr/v1/dynamic_svr/dynamic_new") &&
//                        !response.getUrl().startsWith("https://api.vc.bilibili.com/dynamic_svr/v1/dynamic_svr/dynamic_history")) {
//                    return;
//                }
                System.out.println("----------------------------------------");
                System.out.println("URL       : " + response.getUrl());
                System.out.println("Status    : HTTP " + response.getStatus().intValue() + " " + response.getStatusText());
                System.out.println("Mime Type : " + response.getMimeType());
                if (true/*finished.contains(rr.getRequestId()) && ResourceType.Document.equals(rr.getType())*/) {
                    GetResponseBodyResult rb = session.getCommand().getNetwork().getResponseBody(rr.getRequestId());
                    if (rb != null) {
                        String body = rb.getBody();
                        System.out.println("Content   : " + body.substring(0, body.length() > 1024 ? 1024 : body.length()));
                    }
                }
//                try {
//                    Thread.sleep(5000);
//                } catch (InterruptedException ex) {
//                    ex.printStackTrace();
//                }
//                session.evaluate("function moveToLast(){var t = document.body.clientHeight;window.scroll({ top: t, left: 0});}\n");
//                session.callFunction("moveToLast");
            }
        });


        session.navigate("https://t.bilibili.com/347899871470731304?tab=2");
        session.waitDocumentReady();
        Thread.sleep(5000);
        List<Cookie> cookies = session.getCommand().getNetwork().getCookies();
        File file = new File("test.json");
        CommonUtil.storeObjInJson(file, cookies);
        session.getCommand().getNetwork();


//        session.close();
//        factory.close();
        //launcher.kill();
    }
}

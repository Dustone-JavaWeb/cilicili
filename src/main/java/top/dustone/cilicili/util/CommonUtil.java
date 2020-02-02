package top.dustone.cilicili.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import io.vertx.core.buffer.Buffer;
import io.webfolder.cdp.type.network.Cookie;
import io.webfolder.cdp.type.network.CookieParam;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;

public class CommonUtil {
    public static File storeObjInJson(File file, Object object) throws IOException {
        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write(JSON.toJSONString(object, SerializerFeature.PrettyFormat));
        fileWriter.flush();
        fileWriter.close();
        return file;
    }

    public static Object loadObjectFromJsonFile(File file, Class clazz, boolean isList) throws IOException {
        FileReader fileReader = new FileReader(file);
        char[] chars = new char[(int) file.length()];
        fileReader.read(chars);
        fileReader.close();
        ;
        String json = new String(chars);
        Object resilt = null;
        if (isList) {
            resilt = JSON.parseArray(json, clazz);
        } else {
            resilt = JSON.parseObject(json, clazz);
        }
        return resilt;
    }

    public static List<CookieParam> convertCookies(List<Cookie> cookies) {
        List<CookieParam> cookieParams = new ArrayList<CookieParam>();
        for (Cookie cookie : cookies) {
            CookieParam cookieParam = new CookieParam();
            cookieParam.setDomain(cookie.getDomain());
            cookieParam.setExpires(cookie.getExpires());
            cookieParam.setHttpOnly(cookie.isHttpOnly());
            cookieParam.setName(cookie.getName());
            cookieParam.setPath(cookie.getPath());
            cookieParam.setSameSite(cookie.getSameSite());
            cookieParam.setSecure(cookie.isSecure());
            cookieParam.setValue(cookie.getValue());
            cookieParams.add(cookieParam);
        }
        return cookieParams;
    }

    public static String gzipReplyAsString(Buffer buffer) {
        StringBuffer stringBuffer = new StringBuffer();
        try {
            InputStreamReader inputStreamReader = new InputStreamReader(new GZIPInputStream(new ByteArrayInputStream(buffer.getBytes())));
            char[] content = new char[500];
            while (true) {
                int count = inputStreamReader.read(content);
                if (count == -1) {
                    break;
                } else {
                    stringBuffer.append(content, 0, count);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuffer.toString();
    }
}

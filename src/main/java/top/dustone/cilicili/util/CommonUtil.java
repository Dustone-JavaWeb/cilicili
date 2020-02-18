package top.dustone.cilicili.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import io.vertx.core.MultiMap;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.CaseInsensitiveHeaders;
import io.webfolder.cdp.type.network.Cookie;
import io.webfolder.cdp.type.network.CookieParam;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    public static Map<String, String> resolveRequestHeaders(String input) {
        Map<String, String> result = new HashMap<String, String>();
        String mark = ": ";
        String[] arrs = input.split("\\n");
        for (String str : arrs) {
            int index = str.indexOf(mark);
            if (index == -1) {
                continue;
            }
            result.put(str.substring(0, index), str.substring(index + mark.length(), str.length()));
        }
        return result;
    }

    public static void main(String[] args) {
        Map<String, String> result = resolveRequestHeaders("GET /dynamic_svr/v1/dynamic_svr/dynamic_new?uid=36033035&type=268435455&from=header HTTP/1.1\n" +
                "Host: api.vc.bilibili.com\n" +
                "Connection: keep-alive\n" +
                "Pragma: no-cache\n" +
                "Cache-Control: no-cache\n" +
                "Accept: application/json, text/plain, */*\n" +
                "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36\n" +
                "Origin: https://t.bilibili.com\n" +
                "Sec-Fetch-Site: same-site\n" +
                "Sec-Fetch-Mode: cors\n" +
                "Referer: https://t.bilibili.com/?spm_id_from=333.851.b_696e7465726e6174696f6e616c486561646572.30\n" +
                "Accept-Encoding: gzip, deflate, br\n" +
                "Accept-Language: zh-CN,zh;q=0.9\n" +
                "Cookie: l=v; _uuid=FBCF9D49-E1E8-95F2-F19B-0C1E05E8110906508infoc; buvid3=1D004197-A9D5-495B-AE3A-011801AD3A7353919infoc; LIVE_BUVID=AUTO1215797626982902; sid=8uwlv0hx; CURRENT_FNVAL=16; rpdid=|(k|k)JYJY~J0J'ul~m~YYmJJ; im_notify_type_36033035=0; Hm_lvt_8a6e55dbd2870f0f5bc9194cddf32a02=1579917522,1579926687; Hm_lvt_8dabccb80a103a16cdfecde99700b220=1579917522,1579926687; CURRENT_QUALITY=112; DedeUserID=36033035; DedeUserID__ckMd5=2b5eb2631560e507; SESSDATA=c8b6effa%2C1583207228%2C4cd59921; bili_jct=a0417e73c93acf875d1e605672ff12df; INTVER=1; bp_t_offset_36033035=351275153118918144");
        for(String str:result.keySet()){
            System.out.println("Key: "+str+" value: "+result.get(str));
        }
    }

}

package top.dustone.cilicili.util;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class FreeMarkerUtil {
    private static Configuration cfg;

    static {
        cfg = new Configuration(Configuration.VERSION_2_3_29);
        cfg.setClassForTemplateLoading(FreeMarkerUtil.class, "/template");
        cfg.setDefaultEncoding("UTF-8");
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
    }

    public static boolean printTemplate(Object data, String templateName, String outputDir) {
        Writer out = null;
        try {
            out = new OutputStreamWriter(new FileOutputStream(new File(outputDir)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if(out == null){
            return false;
        }
        return printTemplate(data,templateName,out);
    }

    public static boolean printTemplate(Object data, String templateName, Writer wtiter) {
        Template template = null;
        FileOutputStream fileOutputStream = null;
        try {
            template = cfg.getTemplate(templateName);
            template.process(data, wtiter);
            fileOutputStream.close();
            wtiter.close();
        } catch (IOException | TemplateException e) {
            try {
                fileOutputStream.close();
                wtiter.close();
            } catch (IOException ex) {
            }
            return false;
        }
        return true;
    }

    public static void main(String[] args) throws ParseException {
        SimpleDateFormat linuxTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        linuxTimeFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date  = new Date();
        System.out.println(linuxTimeFormat.format(date));
    }
}

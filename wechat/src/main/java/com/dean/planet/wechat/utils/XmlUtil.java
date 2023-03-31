package com.dean.planet.wechat.utils;

import com.alibaba.fastjson2.JSON;
import com.google.common.collect.Maps;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.core.util.QuickWriter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.XppDriver;
import com.thoughtworks.xstream.security.AnyTypePermission;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.io.Writer;
import java.util.List;
import java.util.Map;

/**
 * @author dean
 * @since 2023/3/24 9:13
 */
@Slf4j
public class XmlUtil {
    private XmlUtil() {
    }

    /**
     * 对所有xml节点的转换都增加CDATA标记
     */
    private static final boolean CDATA = true;

    /**
     * 扩展xStream，使其支持CDATA块
     */
    private static final XStream XSTREAM = new XStream(new XppDriver() {
        @Override
        public HierarchicalStreamWriter createWriter(Writer out) {
            return new PrettyPrintWriter(out) {
                @Override
                protected void writeText(QuickWriter writer, String text) {
                    if (CDATA) {
                        writer.write("<![CDATA[");
                        writer.write(text);
                        writer.write("]]>");
                    } else {
                        writer.write(text);
                    }
                }
            };
        }
    });

    /**
     * xml转换为对象(从InputStream中读取)
     * @param inputStream inputStream
     * @param cls 类
     * @return 对象
     * @param <T> 泛型
     */
    @SuppressWarnings("unchecked")
    public static <T> T XmlToEntity(InputStream inputStream, Class<T> cls) {
        try {
            XSTREAM.addPermission(AnyTypePermission.ANY);
            //忽略不需要的节点
            XSTREAM.ignoreUnknownElements();
            //对指定的类使用Annotations 进行序列化
            XSTREAM.processAnnotations(cls);
            T result = (T) XSTREAM.fromXML(inputStream);
            log.info("xmlToEntity:{}", JSON.toJSONString(result));
            return result;
        } catch (Exception e) {
            log.error("xml转换为bean失败:{}", e.getMessage());
        }
        return null;
    }

    /**
     * xml转换为map(从InputStream中读取)
     * @param inputStream inputStream
     * @return Map
     */
    public static Map<String, String> XmlToMap(InputStream inputStream) {
        // 将解析结果存储在HashMap中
        Map<String, String> map = Maps.newHashMap();

        try {
            // 从request中取得输入流
            // 读取输入流
            SAXReader saxReader = SAXReader.createDefault();
            Document document = saxReader.read(inputStream);
            // 得到xml根元素
            Element root = document.getRootElement();
            // 得到根元素的所有子节点

            List<Element> elementList = root.elements();

            // 遍历所有子节点
            for (Element e : elementList) {
                map.put(e.getName(), e.getText());
            }
        } catch (Exception e) {
            log.error("xml转换为bean失败:{}", e.getMessage());
        } finally{
            try {
                inputStream.close();
            } catch (Exception e) {
                log.error("inputStream close失败:{}", e.getMessage());
            }
        }
        log.info("xmlToMap:{}", JSON.toJSONString(map));
        return map;
    }

    /**
     * 对象转换成xml
     * @param entity 对象
     * @return xml
     */
    public static <T> String entityToXml(T entity) {
        XSTREAM.alias("xml", entity.getClass());
        XSTREAM.processAnnotations(entity.getClass());
        String result = XSTREAM.toXML(entity);
        log.info("entityToXml:{}", result);
        return result;
    }
}

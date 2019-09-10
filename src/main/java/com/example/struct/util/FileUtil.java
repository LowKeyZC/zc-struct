package com.example.struct.util;

import com.alibaba.fastjson.util.IOUtils;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.http.client.utils.URLEncodedUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FileUtil {
  private static Logger logger = LoggerFactory.getLogger(FileUtil.class);

  public static void main(String[] args) throws UnsupportedEncodingException, MalformedURLException, URISyntaxException {
//    System.out.println(URLEncoder.encode("https://public.fuyoukache" +
//        ".com/d2507024-50f1-44bd-909e-762780bb7578---2.png .jpg", "UTF-8").replaceAll("\\+", "%20"));
//    System.out.println(getUrlFileByteArr("https://public.fuyoukache.com/d2507024-50f1-44bd-909e-762780bb7578---2.png .jpg").length);
//    System.out.println(getUrlFileByteArr("https://public.fuyoukache" +
//        ".com/d2507024-50f1-44bd-909e-762780bb7578---2.png%20.jpg").length);
//    getUrlFileByteArr("https%3A%2F%2Fpublic.fuyoukache" +
//        ".com%2Fd2507024-50f1-44bd-909e-762780bb7578---2.png%20.jpg");

    String urlStr = "https://public.fuyoukache.com/d2507024-50f1-44bd-909e-762780bb7578---2.png .jpg";
    URL url = new URL(urlStr);
    URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef());
    url = uri.toURL();
    System.out.println(url.toString());
  }

  /**
   * 网络文件地址获取字节数组
   */
  public static byte[] getUrlFileByteArr(String url) {
    List<Map.Entry<String, String>> headers = new ArrayList<>(1);
    Map.Entry<String, String> header1 = new AbstractMap.SimpleEntry<>(
        HttpUtilHelper.HEADER_CONTENT_TYPE, "application/octet-stream;charset=utf8");
    headers.add(header1);
    ResultStatus ret = new HttpUtilHelper().httpGet(url, true, headers, true);
    return ret.getByteResponse();
  }

  /**
   * 获取文件内容
   */
  public static String getFileContent(String relatPath) {
    //读取文件
    BufferedReader br = null;
    StringBuilder sb = new StringBuilder();
    try (FileInputStream fis = new FileInputStream(new ClassPathResource(relatPath).getFile())){
      br = new BufferedReader(new InputStreamReader(fis, StandardCharsets.UTF_8));
      String line;
      while ((line = br.readLine()) != null) {
        sb.append(line);
      }
    } catch (Exception e) {
      logger.error("getFileContent", e);
    } finally {
      IOUtils.close(br);
    }
    return sb.toString();
  }

  /**
   * 获取网络文件后缀
   */
  public static String getUrlSuffix(String fileUrl) {
    return fileUrl.substring(fileUrl.lastIndexOf(".") + 1);
  }

  /**
   * 字节数组转文件
   */
  public static void byteArrToFile(byte[] bytes, String filePath, String fileName) {
    File dir = new File(filePath);
    if (!dir.exists() && dir.isDirectory()) {
      dir.mkdirs();
    }
    File file = new File(filePath + "\\" + fileName);
    try (FileOutputStream fos = new FileOutputStream(file);
         BufferedOutputStream bos = new BufferedOutputStream(fos)) {
      bos.write(bytes);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * 压缩图片
   */
  public static byte[] compressImg(String fileUrl) throws IOException {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    Thumbnails.of(new URL(fileUrl)).scale(1).outputQuality(0.8).outputFormat(getUrlSuffix(fileUrl)).toOutputStream(out);
    return out.toByteArray();
  }

  /**
   * 本地文件获取字节数组
   */
  public static byte[] getLocalFileByteArr(String localPath) {
    try (BufferedInputStream in = new BufferedInputStream(new FileInputStream(localPath));
         ByteArrayOutputStream out = new ByteArrayOutputStream(1024);){
      byte[] temp = new byte[1024];
      int size = 0;
      while ((size = in.read(temp)) != -1) {
        out.write(temp, 0, size);
      }
      return out.toByteArray();
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }

  /**
   * 压缩图片2
   */
  public static byte[] convertImageToByteArr2(byte[] context, Integer sizeLimit) {
    //默认上限为500k
    if (sizeLimit == null) {
      sizeLimit = 500;
    }
    sizeLimit = sizeLimit * 1024;
    if (logger.isDebugEnabled()) {
      logger.debug("compress before size:{}", context.length);
    }
    if (context.length <= sizeLimit) {
      return context;
    }
    ByteArrayInputStream inputStream = null;
    try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()){
      //将图片数据还原为图片
      inputStream = new ByteArrayInputStream(context);
      BufferedImage image = ImageIO.read(inputStream);
      int imageSize = context.length;
      int type = image.getType();
      int height = image.getHeight();
      int width = image.getWidth();

      BufferedImage tempImage;
      //判断文件大小是否大于size,循环压缩，直到大小小于给定的值
      while (imageSize > sizeLimit) {
        //将图片长宽压缩到原来的90%
        height = new Double(height * 0.9).intValue();
        width = new Double(width * 0.9).intValue();
        tempImage = new BufferedImage(width, height, type);
        // 绘制缩小后的图
        tempImage.getGraphics().drawImage(image, 0, 0, width, height, null);
        //重新计算图片大小
        outputStream.reset();
        ImageIO.write(tempImage, "JPEG", outputStream);
        imageSize = outputStream.toByteArray().length;
      }

      byte[] bytes = outputStream.toByteArray();
      if (logger.isDebugEnabled()) {
        logger.debug("compress after size:{}", bytes.length);
      }
      return bytes;
    } catch (Exception e) {
      //抛出异常
      logger.error("convertImageToByteArr error", e);
      return null;
    } finally {
      IOUtils.close(inputStream);
    }
  }
}
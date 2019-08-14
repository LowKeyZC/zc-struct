package com.example.struct.util;

import com.alibaba.fastjson.JSON;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.X509EncodedKeySpec;
import java.util.Enumeration;

public class RsaUtil {

  private static Logger logger = LoggerFactory.getLogger(RsaUtil.class);

  private static final String PRIVATE_KEY = "files/server.pfx";
  public static final String PUBLIC_KEY = FileUtil.getFileContent("files/server-key-pub.pem")
      .replaceAll("-----BEGIN PUBLIC KEY-----", "").replaceAll("-----END PUBLIC KEY-----", "")
      .replaceAll("\n", "");
  private static final String PASSWORD = "1qa2ws";
  private static final String ALGORITHM = "MD5withRSA";

  /**
   * 验签方法
   */
  public static boolean verifyMethod(String data, String sign) {
    try {
      byte[] signStr = Base64.decodeBase64(sign);
      X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64.decodeBase64(PUBLIC_KEY));
      KeyFactory factory = KeyFactory.getInstance("RSA");
      PublicKey publicKey = factory.generatePublic(keySpec);
      Signature signature = Signature.getInstance(ALGORITHM);
      signature.initVerify(publicKey);
      signature.update(data.getBytes());
      return signature.verify(signStr);
    } catch (Exception e) {
      logger.error("validate SignString Exception:{}", JSON.toJSONString(e));
    }
    return false;
  }

  /**
   * 签名
   */
  public static String generate(String body) {
    byte[] signedInfo = null;
    try (FileInputStream fis = new FileInputStream(new ClassPathResource(PRIVATE_KEY).getFile())) {
      KeyStore ks = KeyStore.getInstance("PKCS12");
      char[] nPassword = null;
      nPassword = PASSWORD.toCharArray();
      ks.load(fis, nPassword);
      fis.close();
      Enumeration enuml = ks.aliases();
      String keyAlias = null;
      if (enuml.hasMoreElements()) {
        keyAlias = (String) enuml.nextElement();
        logger.info("alias=[{}]", keyAlias);
      }
      logger.info("is key entry = {}", ks.isKeyEntry(keyAlias));
      PrivateKey prikey = (PrivateKey) ks.getKey(keyAlias, nPassword);
      logger.info("keystore type = {}", ks.getType());
      Signature signature = Signature.getInstance(ALGORITHM);
      signature.initSign(prikey);
      //Read the string into a buffer
      byte[] dataInBytes = body.getBytes(StandardCharsets.UTF_8);
      //update signature with data to be signed
      signature.update(dataInBytes);
      //sign the data
      signedInfo = signature.sign();
    } catch (Exception e) {
      logger.error("yunxin sign failed, error stack: ", e);
      return null;
    }
    return Base64.encodeBase64String(signedInfo);
  }
}

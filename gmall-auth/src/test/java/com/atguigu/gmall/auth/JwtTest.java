package com.atguigu.gmall.auth;

import com.atguigu.gmall.common.utils.JwtUtils;
import com.atguigu.gmall.common.utils.RsaUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.HashMap;
import java.util.Map;

/**
 * @author huima9527
 * @create 2021-02-26 19:33
 */
@SpringBootTest
public class JwtTest {
    // 别忘了创建D:\\project\rsa目录
    private static final String pubKeyPath = "D:\\JAVA_dev\\code\\rsa\\rsa.pub";
    private static final String priKeyPath = "D:\\JAVA_dev\\code\\rsa\\rsa.pri";

    private PublicKey publicKey;

    private PrivateKey privateKey;

    @Test
    public void testRsa() throws Exception {
        RsaUtils.generateKey(pubKeyPath, priKeyPath, "234");
    }

    @BeforeEach
    public void testGetRsa() throws Exception {
        this.publicKey = RsaUtils.getPublicKey(pubKeyPath);
        this.privateKey = RsaUtils.getPrivateKey(priKeyPath);
    }

    @Test
    public void testGenerateToken() throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("id", "11");
        map.put("username", "liuyan");
        // 生成token
        String token = JwtUtils.generateToken(map, privateKey, 5);
        System.out.println("token = " + token);
    }

    @Test
    public void testParseToken() throws Exception {
        String token = "eyJhbGciOiJSUzI1NiJ9.eyJpZCI6IjExIiwidXNlcm5hbWUiOiJsaXV5YW4iLCJleHAiOjE2MTQzMzk4Mzd9.IkQ_zHTCmyXaE27XHsmPG-_TgYG--Pmcy3j__lFDLRvU6OMEUI7yLjBGzNfcQpNNV-YOCDnGq4u3yJEODHDSZOF70CeZXBnUfxBzQwt0tk__6GISpp6h2qZ-qoOWwH_1cFOhBzU3Gi5o0pHrpJ-Brpj8SKgGTbFDOfwOsbVZ6bSYapUKIyxUqhe60VCCSmjkWzBC99g4ja_MuM0VNGDyXO8oislc1ZxI_aaUlSkW6a7EP9D2rXxzAuVGbgM1Uax9rBV9dirWwKOitR8NQljb_bR17GLMS2hTrsyh6hVUl5vg0e95RplQW0ecoEQD-qsC3jzvJCZDVp0eiku76fD38w";
        // 解析token
        Map<String, Object> map = JwtUtils.getInfoFromToken(token, publicKey);
        System.out.println("id: " + map.get("id"));
        System.out.println("userName: " + map.get("username"));
    }
}

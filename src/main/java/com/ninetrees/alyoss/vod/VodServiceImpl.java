package com.ninetrees.alyoss.vod;

import com.aliyun.oss.HttpMethod;
import com.aliyun.oss.common.utils.StringUtils;
import com.aliyun.oss.model.GeneratePresignedUrlRequest;
import com.aliyun.vod.upload.impl.UploadVideoImpl;
import com.aliyun.vod.upload.req.UploadStreamRequest;
import com.aliyun.vod.upload.resp.UploadStreamResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Classname:VodServiceImpl
 * Description:
 */
@Service
@Slf4j
public class VodServiceImpl implements VodService {


    @Override
    public Map<String, Object> uploadMusicToVod(MultipartFile file) {
        String originalName = file.getOriginalFilename();
        String title = originalName.substring(0, originalName.lastIndexOf("."));
        Map<String, Object> res = new HashMap<>();
        res.put("title", title);
        try (InputStream inputStream = file.getInputStream()) {
            UploadStreamRequest request = new UploadStreamRequest(ConstantVodUtils.ACCESS_KEY_ID, ConstantVodUtils.ACCESS_KEY_SECRET, title, originalName, inputStream);
//            request.setRequestId("c15abfee3e485ecd15f3f042e477e7d7");
            UploadVideoImpl uploader = new UploadVideoImpl();
            UploadStreamResponse response = uploader.uploadStream(request);
            String musicId = "";
            if (response.isSuccess()) {
                musicId = response.getVideoId();
                res.put("musicId", musicId);
            } else {
                musicId = response.getVideoId();
                res.put("musicId", musicId);
            }
//            res.put("coverUrl",null);
            return res;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    //为 OSS 对象生成临时的带签名的访问 URL。
    //      @param objectKeyOrUrl 对象的 Key (例如 "snapshots/image.jpg") 或可能包含域名的基础 URL
//    public String generatePresignedUrl(String objectKeyOrUrl) throws ClientException {
//        DefaultAcsClient client = InitVodClient.initVodClient(ConstantVodUtils.ACCESS_KEY_ID, ConstantVodUtils.ACCESS_KEY_SECRET);
//        if (StringUtils.isNullOrEmpty(objectKeyOrUrl)) {
//            log.warn("尝试为 null 或空的 objectKey 生成签名 URL。");
//            return null; // 输入为空，直接返回 null
//        }
//
//        // 尝试从输入中提取纯粹的 Object Key
//        String objectKey = extractObjectKey(objectKeyOrUrl);
//        if (StringUtils.isNullOrEmpty(objectKey)) {
//            log.error("无法从输入 '{}' 中提取有效的 Object Key。", objectKeyOrUrl);
//            return null;
//        }
//
//        Date expiration = new Date(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(ConstantVodUtils.EXPIRE_MINUTES));
//        // 创建请求，指定 Bucket、Key 和 HTTP 方法 (GET)
//        GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest("edu-2233", ConstantVodUtils.ACCESS_KEY_ID, HttpMethod.GET);
//        request.setExpiration(expiration);
//
//        // 生成签名 URL
//        client.generatePresignedUrl(request);
//
//        return "";
//    }


}

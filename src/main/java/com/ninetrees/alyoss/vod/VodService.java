package com.ninetrees.alyoss.vod;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * Classname:VodService
 * Description:
 */
@Service
public interface VodService {

    Map<String,Object> uploadMusicToVod(MultipartFile file);
//    String generatePresignedUrl(String objectKey);

}

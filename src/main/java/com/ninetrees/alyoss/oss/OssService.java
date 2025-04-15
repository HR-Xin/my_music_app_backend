package com.ninetrees.alyoss.oss;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * Classname:OssService
 * Description:
 */
public interface OssService {
    String uploadAvatar(MultipartFile file) throws IOException;

    String uploadCover(MultipartFile file) throws IOException;
}

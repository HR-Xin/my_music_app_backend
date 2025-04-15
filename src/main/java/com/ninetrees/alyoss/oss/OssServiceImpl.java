package com.ninetrees.alyoss.oss;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

/**
 * Classname:OssServiceImpl
 * Description:
 */
@Service
public class OssServiceImpl implements OssService {
    @Override
    public String uploadAvatar(MultipartFile file) throws IOException {
        //获取oss几个接口数据
        String endpoint = ConstantPropertiesUtils.END_POINT;
        String keyId = ConstantPropertiesUtils.ACCESS_KEY_ID;
        String keySecret = ConstantPropertiesUtils.ACCESS_KEY_SECRET;
        String bucketName = ConstantPropertiesUtils.BUCKET_NAME;

        try {
            //注册oss cli实例
            OSS ossClient = new OSSClientBuilder().build(endpoint, keyId, keySecret);

            //获取输入流,用于以字节流读取图片文件内容,并可以进一步上传
            InputStream inputStream = file.getInputStream();
            //原fileName拼接上uuid生成新的文件名
            String fileName = file.getOriginalFilename();
            String uuid = UUID.randomUUID().toString().replace("-", "");
            fileName = uuid + fileName;

            //按时间对上传文件进行分类
            //引入了日期joda依赖
            //实现aa/bb/123.jpg
            String datePath = new DateTime().toString("yyyy/MM/dd");
            fileName = "avatar/" + datePath + "/" + fileName;
            //调用oss方法实现上传
            //第一个参数bucket名称
            //第二个参数 上传到oss文件路径和文件名称
            //第三个参数 上传文件输入流
            ossClient.putObject(bucketName, fileName, inputStream);

            //关闭OssClient
            ossClient.shutdown();
            //将上传路径进行返回,需要把阿里云oss路径手动拼接出来
            //https://edu-2233.oss-cn-beijing.aliyuncs.com/235004-1685980204f695.jpg
            String url = null;
            url = "https://" + bucketName + "." + endpoint + "/" + fileName;

            return url;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String uploadCover(MultipartFile file) throws IOException {
        //获取oss几个接口数据
        String endpoint = ConstantPropertiesUtils.END_POINT;
        String keyId = ConstantPropertiesUtils.ACCESS_KEY_ID;
        String keySecret = ConstantPropertiesUtils.ACCESS_KEY_SECRET;
        String bucketName = ConstantPropertiesUtils.BUCKET_NAME;

        try {
            //注册oss cli实例
            OSS ossClient = new OSSClientBuilder().build(endpoint, keyId, keySecret);

            //获取输入流,用于以字节流读取图片文件内容,并可以进一步上传
            InputStream inputStream = file.getInputStream();
            //原fileName拼接上uuid生成新的文件名
            String fileName = file.getOriginalFilename();
            String uuid = UUID.randomUUID().toString().replace("-", "");
            fileName = uuid + fileName;

            //按时间对上传文件进行分类
            //引入了日期joda依赖
            //实现cover/2025/3/1/123.jpg
            String datePath = new DateTime().toString("yyyy/MM/dd");
            fileName = "cover/" + datePath + "/" + fileName;
            //调用oss方法实现上传
            //第一个参数bucket名称
            //第二个参数 上传到oss文件路径和文件名称
            //第三个参数 上传文件输入流
            ossClient.putObject(bucketName, fileName, inputStream);

            //关闭OssClient
            ossClient.shutdown();
            //将上传路径进行返回,需要把阿里云oss路径手动拼接出来
            //https://edu-2233.oss-cn-beijing.aliyuncs.com/235004-1685980204f695.jpg
            String url = null;
            url = "https://" + bucketName + "." + endpoint + "/" + fileName;

            return url;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

package com.ninetrees.alyoss.oss;

import com.ninetrees.musicapp.common.R;
import com.ninetrees.musicapp.entity.Users;
import com.ninetrees.musicapp.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * Classname:OssController
 * Description:
 */
@RestController
//@CrossOrigin(origins = "http://47.120.34.75")
@RequestMapping("/api/oss")
public class OssController {
    @Autowired
    private OssService ossService;
    @Autowired
    private UsersService usersService;

    @PostMapping("coverupload/{userId}")
    //requestParam参数应该与前端传来的formData对象中追加的键一样
    //formData.append('avatar', file);
    //formData.append('id', user.value.id);
    public R uploadCover(@RequestParam("avatar") MultipartFile file
            , @PathVariable String userId) throws IOException {
        String coverUrl = ossService.uploadCover(file);
//        usersService.updateById(new Users().)
        return R.ok().data("coverUrl", coverUrl);
    }

    @PostMapping("cover")
    public R uploadCover(@RequestParam("file")MultipartFile file) throws IOException {
        System.out.println("封面文件名  " + file);
        if (file == null || file.isEmpty()) {
            System.out.println("⚠️ 接收到的文件为空！");

            return R.error().message("文件为空"); // ⚠️ 直接返回错误，确保 Vue 看到的是 error
        }
        System.out.println(" 接收到的文件不为空！");
        String coverUrl = ossService.uploadCover(file);
        return R.ok().data("coverUrl", coverUrl);
    }

    @PostMapping("avatarupload/{userId}")
    public R uploadAvatar(@RequestParam("avatar") MultipartFile avatar, @PathVariable String userId) throws IOException {
        System.out.println(avatar.getOriginalFilename() + userId);
        String avatarUrl = ossService.uploadAvatar(avatar);
        Users user = new Users();
        user.setAvatar(avatarUrl);
        user.setId(userId);
        usersService.updateById(user);
        return R.ok().data("avatarUrl", avatarUrl);
    }

    @PostMapping("test")
    public R test() {
//        System.out.println(file.getOriginalFilename());
        return R.ok().message("可以访问后端接口");
    }

}

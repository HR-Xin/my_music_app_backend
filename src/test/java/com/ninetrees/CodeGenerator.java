package com.ninetrees;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import org.junit.Test;

/**
 * @author
 * @since 2018/12/13
 * 开发环境用于生成代码,生成环境不需要,就可以在test包下,
 * 无需创建在java包里(会发布到线上)
 */
public class CodeGenerator {

    @Test
    public void run() {

        // 1、创建代码生成器
        AutoGenerator mpg =new AutoGenerator();

        // 2、全局配置
        GlobalConfig gc = new GlobalConfig();
        String projectPath = System.getProperty("user.dir");
        //绝对路径+相对路径的方式找到代码生成目录
        gc.setOutputDir("C:\\code\\javaWeb\\my_music_app" + "/src/main/java");

        gc.setAuthor("hr_xin");
        gc.setOpen(false); //生成后是否打开资源管理器
        //重新生成代码不覆盖原有代码,放置误覆盖已写代码
        gc.setFileOverride(false); //重新生成时文件是否覆盖

        //   IUserServie->UserServie
        gc.setServiceName("%sService");	//去掉Service接口的首字母I

        //id类型 long:ID_WORKER string/char(xx):ID_WORKER_STR
        gc.setIdType(IdType.ID_WORKER_STR); //主键策略
        //时间是datetime类型
        gc.setDateType(DateType.ONLY_DATE);//定义生成的实体类中日期类型
        gc.setSwagger2(true);//开启Swagger2模式

        mpg.setGlobalConfig(gc);

        // 3、数据源配置(与mp不通用一个,须独自配置)
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl("jdbc:mysql://localhost:3306/music_app?serverTimezone=GMT%2B8");
        dsc.setDriverName("com.mysql.cj.jdbc.Driver");
        dsc.setUsername("root");
        dsc.setPassword("326425xin");
        dsc.setDbType(DbType.MYSQL);
        mpg.setDataSource(dsc);

        // 4、包配置
        PackageConfig pc = new PackageConfig();
        pc.setModuleName("musicapp"); //总包名
        //包  service_edu.com.ninetrees.eduservice
        pc.setParent("com.ninetrees");
        //包  com.atguigu.eduservice.controller
        pc.setController("controller");
        pc.setEntity("entity");
        pc.setService("service");
        pc.setMapper("mapper");
        mpg.setPackageInfo(pc);

        // 5、策略配置 逆向工程:由表生成java对象
        StrategyConfig strategy = new StrategyConfig();



        //对哪张表进行生成,这个
        strategy.setInclude("songs");




        //驼峰命名转化换(表名)
        strategy.setNaming(NamingStrategy.underline_to_camel);//数据库表映射到实体的命名策略
        strategy.setTablePrefix(pc.getModuleName() + "_"); //生成实体时去掉表前缀
//        驼峰命名转化换(字段名)
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);//数据库表字段映射到实体的命名策略
        strategy.setEntityLombokModel(true); // lombok 模型 @Accessors(chain = true) setter链式操作

        strategy.setRestControllerStyle(true); //restful api风格控制器
        strategy.setControllerMappingHyphenStyle(true); //url中驼峰转连字符

        mpg.setStrategy(strategy);


        // 6、执行
        mpg.execute();
    }
}

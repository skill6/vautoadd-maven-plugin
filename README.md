# vautoadd-maven-plugin
maven中执行此插件，当前工程及其子工程在当前版本基础上版本号自动+1，可指定version x.y.z 中的任意一个。


## 如何操作

- 1、在pom.xml中添加插件
```xml
<plugin>
    <groupId>cn.skill6.plugin</groupId>
    <artifactId>vautoadd-maven-plugin</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <configuration>
        <currentVersion>${project.version}</currentVersion>
        <projectDirectory>${basedir}</projectDirectory>
    </configuration>
</plugin>
```

- 2、在项目根目录下执行以下命令
`mvn vautoadd:vadd`

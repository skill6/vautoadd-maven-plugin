# vautoadd-maven-plugin
maven中执行此插件，当前工程及其子工程在当前版本基础上版本号自动+1，可指定version x.y.z 中的任意一个。

**如项目及其所有子模块当前版本为0.0.1-SNAPSHOT，执行完之后，所有模块pom里的版本自动更新为0.0.2-SNAPSHOT**

**每执行一次小版本就会+1**

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

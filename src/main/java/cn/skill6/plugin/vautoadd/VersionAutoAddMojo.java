package cn.skill6.plugin.vautoadd;

import java.io.IOException;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

/**
 * 版本自动增加插件
 *
 * @author 何明胜
 * @version 1.0
 * @since 2018年10月2日 上午12:49:49
 */
@Mojo(name = "vadd")
public class VersionAutoAddMojo extends AbstractMojo {

  // 版本格式为 0.0.1-SNAPSHOT
  @Parameter private String currentVersion;

  // 工程目录路径
  @Parameter private String projectDirectory;

  @Override
  public void execute() throws MojoExecutionException, MojoFailureException {
    if (currentVersion == null) {
      throw new MojoFailureException("currentVersion is null");
    }

    int beginIndex = currentVersion.lastIndexOf(".");
    int endIndex = currentVersion.indexOf("-");

    if (beginIndex == -1 || endIndex == -1 || endIndex - beginIndex < 1) {
      throw new MojoFailureException("currentVersion fornat is error");
    }

    String versionPefix = currentVersion.substring(0, beginIndex + 1);
    int smallVersion = Integer.parseInt(currentVersion.substring(beginIndex + 1, endIndex));
    // 版本自增
    smallVersion++;
    String versionSuffix = currentVersion.substring(endIndex);

    String newVersion =
        new StringBuilder()
            .append(versionPefix)
            .append(smallVersion)
            .append(versionSuffix)
            .toString();

    // 调用cmd执行mvn更新版本命令
    Runtime runtime = Runtime.getRuntime();
    String execCommand =
        new StringBuilder()
            .append("cmd /k cd ")
            .append(projectDirectory)
            .append(" && mvn versions:set -DnewVersion=")
            .append(newVersion)
            .toString();

    try {
      runtime.exec(execCommand);
    } catch (IOException e) {
      e.printStackTrace();

      throw new MojoExecutionException("execution exception");
    }
  }
}

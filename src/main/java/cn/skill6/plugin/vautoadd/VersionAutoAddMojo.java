package cn.skill6.plugin.vautoadd;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import cn.skill6.plugin.vautoadd.util.OsPlatform;

/**
 * 版本自动增加插件
 *
 * @author 何明胜
 * @version 1.1
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

    // 1、获取当前小版本
    int beginIndex = currentVersion.lastIndexOf(".");
    int endIndex = currentVersion.indexOf("-");

    if (beginIndex == -1 || endIndex == -1 || endIndex - beginIndex < 1) {
      throw new MojoFailureException("currentVersion format is error");
    }

    String versionPefix = currentVersion.substring(0, beginIndex + 1);
    int smallVersion = Integer.parseInt(currentVersion.substring(beginIndex + 1, endIndex));

    // 2、版本自增
    smallVersion++;
    String versionSuffix = currentVersion.substring(endIndex);

    String newVersion =
        new StringBuilder()
            .append(versionPefix)
            .append(smallVersion)
            .append(versionSuffix)
            .toString();

    // 3、调用cmd或者sh执行mvn更新版本命令
    Runtime runtime = Runtime.getRuntime();

    // 3.1 windows平台
    if (OsPlatform.getCurrentOs() == OsPlatform.WINDOWS) {
      String command =
          new StringBuilder()
              .append("cmd /c cd ")
              .append(projectDirectory)
              .append(" && mvn versions:set -DnewVersion=")
              .append(newVersion)
              .toString();

      try {
        Process process = runtime.exec(command);
        outputProcess(process);
      } catch (IOException | InterruptedException e) {
        e.printStackTrace();

        throw new MojoExecutionException("execution exception");
      }

      return;
    }

    // 3.2 linux平台
    String command =
        new StringBuilder()
            .append("cd ")
            .append(projectDirectory)
            .append("; mvn versions:set -DnewVersion=")
            .append(newVersion)
            .toString();

    try {
      Process process = runtime.exec(new String[] {"/bin/sh", "-c", command});
      outputProcess(process);
    } catch (IOException | InterruptedException e) {
      e.printStackTrace();

      throw new MojoExecutionException("execution exception");
    }
  }

  /**
   * 输出执行过程
   *
   * @param process
   * @throws IOException
   * @throws InterruptedException
   */
  public void outputProcess(Process process) throws IOException, InterruptedException {
    InputStream inputStream = process.getInputStream();
    InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
    LineNumberReader lineNumberReader = new LineNumberReader(inputStreamReader);
    String line;

    while ((line = lineNumberReader.readLine()) != null) {
      System.out.println(line);
    }

    lineNumberReader.close();
    inputStreamReader.close();
    inputStream.close();
  }
}

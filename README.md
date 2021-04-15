# turtle

## 项目说明

本项目的主要目的，是提供一个生成API文档和测试用例的工具。

## 如何使用

### 安装依赖

- [安装并配置gradle](https://docs.gradle.org/current/userguide/installation.html)
- [安装并配置aglio](https://www.npmjs.com/package/aglio)

### 手动编译

```sh
gradle build
```

- 使用上述脚本编译后，在./build/distributions目录下，可以看到编译出来的压缩包`turtle-1.0.2.tar`和`turtle-1.0.2.zip`。
- 将任意一个压缩包放到你想放到的任意目录下。
- 解压并配置环境变量：

如果是Mac系统，将以下内容添加到`~/.bash_profile`配置文件中：

```sh
export TURTLE_HOME=你的目录/turtle
export PATH=$PATH:$TURTLE_HOME/bin
```

重新加载配置：

```sh
source  ~/.bash_profile
```

- 在你的项目目录中执行如下脚本，即可将项目的API文档导出到`./out`目录下。

```sh
turtle
```

## 代码架构

![image](https://github.com/burgeon-0/turtle/blob/master/assets/architecture.png)

- 通过命令行或IDE的Plugin获取源代码信息；
- 通过Processor进行处理，其中Analyzer进行源数据分析、Collector进行API数据收集，得到完整的API信息，再由Notifier发出通知；
- 最后由Exporter导出不同的API文档（或测试用例），或将API信息（或测试用例）导出到不同的工具、系统中。

## 问题列表

1. 调试IntelliJ IDEA Plugin的时候，日志为何没有打印？

选择`Edit Configurations...` -> 点击`Logs` -> 点击`+` -> `Log File Location:`找到`当前项目目录/build/idea-sandbox/system/log/idea.log` -> 点击`OK` -> 点击`Apply`，配置成功即可看到打印的日志。

[IntelliJ IDEA官方解析](https://intellij-support.jetbrains.com/hc/en-us/community/posts/203855890-com-intellij-openapi-diagnostic-Logger-debug-statements-are-not-visible-in-the-console)
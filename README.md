# turtle

## 项目说明

本项目的主要目的，是提供一个生成API文档和测试用例的工具，其总体架构如下：

![image](https://github.com/burgeon-0/turtle/blob/master/assets/architecture.png)

- 通过IDE的Plugin获取源代码信息；
- 通过Processor进行处理，其中Analyser进行源数据分析、Collector进行API数据收集，得到完整的API信息，再由Notifier通知Exporter进行导出；
- 最后由Exporter导出不同的API文档（或测试用例），或将API信息（或测试用例）导出到不同的工具、系统中。

## 问题列表

1. 调试IntelliJ IDEA Plugin的时候，日志为何没有打印？

选择`Edit Configurations...` -> 点击`Logs` -> 点击`+` -> `Log File Location:`找到`当前项目目录/build/idea-sandbox/system/log/idea.log` -> 点击`OK` -> 点击`Apply`，配置成功即可看到打印的日志。

[IntelliJ IDEA官方解析](https://intellij-support.jetbrains.com/hc/en-us/community/posts/203855890-com-intellij-openapi-diagnostic-Logger-debug-statements-are-not-visible-in-the-console)
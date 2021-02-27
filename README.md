# turtle

## 项目说明

该项目的主要目的是要提供一个自动生成API文档和测试用例的应用。

## 问题列表

1. 调试IDEA Plugin的时候，日志为何没有打印？

选择`Edit Configurations...` -> 点击`Logs` -> 点击`+` -> `Log File Location:`找到`当前项目目录/build/idea-sandbox/system/log/idea.log` -> 点击`Ok` -> 点击`Apply`，配置成功即可看到打印的日志。

[IDEA官方解析](https://intellij-support.jetbrains.com/hc/en-us/community/posts/203855890-com-intellij-openapi-diagnostic-Logger-debug-statements-are-not-visible-in-the-console)
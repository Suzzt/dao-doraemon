<p align="center">
  <img src="https://github.com/user-attachments/assets/81bfaf1c-37d0-46f3-b684-91484c99fb0e" width="300"/>
</p>
<p align="center">
  <img src="https://img.shields.io/badge/Java-8+-orange?logo=openjdk" />
  <img src="https://img.shields.io/github/license/Suzzt/dao-doraemon" />
</p>

**企业级Java解决方案组件** - 为您的Spring Boot应用服务提供开箱即用的能力(这个组件框架不挑战Hutool、Canva等工具类, 二者定位不同).

## 🎯 功能特性

### 🛡️ Excel处理
在web项目中，有些Excel导入或者处理数据逻辑，我们作为业务开发者，应该重点关注业务逻辑处理，而不是把是时间花在这些通用功能上。比如：一些导入前端对接接入沟通？校验excel文件格式？并发处理excel数据？导入处理过程中发现异常数据时，如何返回用户错误结果集？提供错误数据原因以及生成错误文件下载链接？等等这些非业务额外的excel对接开发成本。

### 🌱 日志拦截
对于企业中针对一些日志规范输出做出拦截逻辑处理，帮助你的项目中的日志脱敏、加密等处理。

### 🏭 响应脱敏
在给前端的接口返回结果做统一字段脱敏，通过配置化一键搞定指定字段的脱敏，同时框架会提供统一的明文查询，做到绝对的安全脱敏。这个处理器还提供了自定义实现脱敏的逻辑及脱敏解明文的逻辑，可以通过实现处理器来自定义！

### 🔒数据库
提供访问数据库对字段加密处理，当然我们集成mybatis了。

### 📦下载中心
建立一个项目中所有的文件存放处理的下载中心，用户统一来下载中心探取文件，同时异步处理后端任务文件的导入与生成，提供处理实际状态。

## 🚀 快速集成
Excel：https://blog.csdn.net/baidu_39290372/article/details/146915544?spm=1001.2014.3001.5502
日志拦截: 


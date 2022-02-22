# CUFIR-Kit 
![welcome_bg](https://user-images.githubusercontent.com/97862260/155096544-d6f1bf11-3438-4b97-bc33-e7c5df3b2644.png)

### 介绍

CUFIR Kit开发工具是一个轻量级的报文建模工具，它提供了丰富的建模和架构设计的方法，通过开发工具可以方便的定义报文结构，以及报文标准数据类型等。

1. 支持windows系统。
2. 数据库使用的是Apache Derby嵌入式数据，体积小随应用程序启动关闭对CPU占用低。
3. 离线工具从根本上避免了黑客的SQL注入。
4. 使用SWT框架对UI界面化实现，操作简单易懂。
5. 编辑后的报文，使用dom4j框架导出xsd等格式，完全符合iso20022规范。

### 安装

[CUFIR Kit开发工具下载地址](https://www.cufir.org.cn/cufir/developmentTool.html)

### 使用

#### 启动

下载并解压CUFIR Kit，双击cufir.exe启动。

#### 电子库

- 电子储存库下载地址https://www.iso20022.org/iso20022-repository/e-repository

导入iso20022电子储存库更新线下库

浏览器访问电子储存库地址，点击如下图所示红框内的蓝色箭头下载电子储存库的压缩包
![image-20220222133621275](https://user-images.githubusercontent.com/97862260/155097952-c7327a47-0874-47aa-82df-bfe232dd6c5e.png)

#### 导入

- 导入电子库到CUFIR Kit线下工具

打开CUFIR Kit点击左上角的File，打开后点击菜单中的Open...，选择解压后的电子储存库，进行导入操作。
![image-20220222133050627](https://user-images.githubusercontent.com/97862260/155098237-0488be17-401f-47a9-a3df-d398b7a46c56.png)

#### Data Types

支持报文使用的所有基本数据类型如（code Sets、Text、Boolean、Indicator、Decimal、Rate、Amount、Quantity、Time、Binary、Schema Types、User Defined）
![image-20220222150104320](https://user-images.githubusercontent.com/97862260/155098444-0f8bd526-06d8-4903-aee5-c164f16ba9ce.png)
右键点击二级树节点，例如Code Sets可新建子节点，双击三级节点可进行查看或编辑，默认展开Summary界面
![image-20220222151047552](https://user-images.githubusercontent.com/97862260/155098526-03e1bb1c-afb3-42b2-96e5-e77426259b69.png)
选择Content界面可添加编辑删除当前节点的子节点数据
![image-20220222151210892](https://user-images.githubusercontent.com/97862260/155098585-1382bbbc-7ddc-4b14-ab69-4895ccd0a9d7.png)
选择Impact界面可查看Message Sets、Message Definitions、Message Component三个模块中的这个数据类型的所属情况
![image-20220222151556231](https://user-images.githubusercontent.com/97862260/155098686-1eceed98-4778-4341-a3e8-6f385ea61d14.png)

#### Business Components

右键点击Business Components可新建子节点，双击其子节点可进行查看或编辑，默认展开Summary界面
![image-20220222152435520](https://user-images.githubusercontent.com/97862260/155098767-1e672e19-94c9-4f93-959e-bb2bcd9abe4d.png)
选择Content界面可添加编辑删除当前节点的子节点数据
![image-20220222152606412](https://user-images.githubusercontent.com/97862260/155098842-22f2c1ad-aafb-40d6-82e3-d47799b5da5a.png)
选择Impact界面可查看Message Sets、Message Definitions、Message Component三个模块中的这个业务组件的所属情况
![image-20220222152632242](https://user-images.githubusercontent.com/97862260/155098904-586ecc58-9650-41cb-b709-3420d35c877b.png)
选择Impact Analysis界面可查看其他Business Component中对这个业务组件的管所属情况

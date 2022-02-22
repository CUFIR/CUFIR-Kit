# CUFIR-Kit 
![welcome_bg](https://user-images.githubusercontent.com/97862260/155096544-d6f1bf11-3438-4b97-bc33-e7c5df3b2644.png)

### 介绍

CUFIR Kit开发工具是一个轻量级的报文建模工具，它提供了丰富的建模和架构设计的方法，通过开发工具可以方便的定义报文结构，以及报文标准数据类型等。

1. 系统支持：Windows
2. 功能支持：消息报文模板创建、自定义数据类型、报文元素、报文组件等
3. 规范支持：ISO20022、CUFIR
4. 技术实现：Eclipse Plugin/RCP、Derby、SWT、ECORE……

### 安装

[CUFIR Kit开发工具下载地址](https://www.cufir.org.cn/cufir/developmentTool.html)

### 使用

#### 启动

下载并解压CUFIR Kit，双击cufir.exe启动。

![image-20220222111729053](https://user-images.githubusercontent.com/97862260/155104587-2173971d-c81f-43a2-9509-0f05d322e724.png)


#### 电子库

[iso20022电子储存库下载地址](https://www.iso20022.org/iso20022-repository/e-repository)

导入iso20022电子储存库,更新线下库

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

![image-20220222155110923](https://user-images.githubusercontent.com/97862260/155100683-53ba5ac7-01a2-4c25-b83e-09c8e90a466c.png)

#### Message Components

右键点击Message Components可新建子节点，双击其子节点可进行查看或编辑，默认展开Summary界面

![image-20220222152912039](https://user-images.githubusercontent.com/97862260/155100795-f7bfc07d-c532-470c-b795-d0e040c5c1b4.png)

选择Content界面可添加编辑删除当前节点的子节点数据（例如Data Tape、Message Component）

![image-20220222152931479](https://user-images.githubusercontent.com/97862260/155100867-234ec8bd-9e20-4182-8284-6a2dc8b491e8.png)

选择Business Trace界面可查看绑定的相关Business Components、Business Element

![image-20220222153020090](https://user-images.githubusercontent.com/97862260/155100928-b131d246-c05a-42d8-9ffc-00d8ad7243c4.png)

选择Impact Anyalysis界面可查看Message Sets、Message Definitions、Message Component三个模块中的这个报文组件的所属情况

![image-20220222153045829](https://user-images.githubusercontent.com/97862260/155101027-16a1153e-3ef7-4d07-8e0e-d75c58ad495b.png)

选择Version/Subsets界面可查看版本关联

![image-20220222153106048](https://user-images.githubusercontent.com/97862260/155101107-bc1dfb3e-659f-4316-9996-c4a5e983572e.png)

#### Business Areas

右键点击Business Areas可新建子节点，双击其子节点可进行查看或编辑，默认展开Summary界面

![image-20220222153716113](https://user-images.githubusercontent.com/97862260/155102519-961c5f4b-5bc6-4033-8820-450f098d6262.png)

选择Content界面可添加编辑删除当前节点的子节点数据（例如Message Definition）

![image-20220222153804532](https://user-images.githubusercontent.com/97862260/155102565-aa412dc5-21c0-4345-944e-72f1f5c051c2.png)

#### Message Sets

右键点击Message Sets可新建子节点，双击其子节点可进行查看或编辑，默认展开Summary界面

![image-20220222154015901](https://user-images.githubusercontent.com/97862260/155102608-e52a8441-f4cb-4dfd-b047-b57e9483aa36.png)

选择Content界面可添加编辑删除当前节点的子节点数据（例如Message Definition）

![image-20220222154052664](https://user-images.githubusercontent.com/97862260/155102655-70af625b-8253-4524-b233-8d5da73f4397.png)

#### Message Definitions

右键点击Message Definitions可新建子节点，双击其子节点可进行查看或编辑，默认展开Summary界面

![image-20220222154221822](https://user-images.githubusercontent.com/97862260/155102792-221ad450-ea7a-4977-9aee-d695480b3bd5.png)

选择Content界面可添加编辑删除当前节点的子节点数据（例如Data Tape、Message Component）

![image-20220222154300114](https://user-images.githubusercontent.com/97862260/155102837-3b5b322c-1658-4438-85af-7e423bf77cdb.png)

选择Business Trace可查看子节点的追溯信息

![image-20220222154544733](https://user-images.githubusercontent.com/97862260/155102940-ff5a0dc1-1959-46b8-b172-b783cafbdbab.png)

选择Impact Analysis界面可查看Message Sets、Message Areas两个模块中的这个报文的所属情况

![image-20220222154617343](https://user-images.githubusercontent.com/97862260/155102990-d72c275d-855e-4d45-b723-2f0501c26bdf.png)

选择Version/Subsets界面可查看版本关联

![image-20220222154907246](https://user-images.githubusercontent.com/97862260/155103063-dc3d401f-af68-4508-9d84-d8327764a8bc.png)

右键点击报文可导出xsd、excel、MDR3，三种格式的报文供使用

![image-20220222155844078](https://user-images.githubusercontent.com/97862260/155103145-7caf3a24-d0b4-428b-ab9a-34bd53e74a42.png)

### 组件

![1645525248](https://user-images.githubusercontent.com/97862260/155115926-ab839862-df14-4f7c-b734-adfe402a724d.jpg)


### 用户

- #### [CFETS](https://www.chinamoney.com.cn/)

- #### [CIPS](https://www.cips.com.cn/)

# CUFIR-Kit 
![welcome_bg](https://user-images.githubusercontent.com/97862260/155096544-d6f1bf11-3438-4b97-bc33-e7c5df3b2644.png)

### Introduce

CUFIR Kit The development tool is a lightweight message modeling tool, which provides rich modeling and architecture design methods. Through the development tool, the message structure and message standard data type can be easily defined.

1. Environmental support: Windows
2. Function support: message template creation, user-defined data type, message element, message component, etc
3. Specification support：ISO20022、CUFIR
4. Technical realization：Eclipse Plugin/RCP、Derby、SWT、ECORE……

### Install

[CUFIR Kit Download address of development tools](https://www.cufir.org.cn/cufir/developmentTool.html)

### Use

#### Startup

Download and unzip CUFIR Kit，double-click cufir.exe Startup.

![image-20220222111729053](https://user-images.githubusercontent.com/97862260/155104587-2173971d-c81f-43a2-9509-0f05d322e724.png)


#### Electronic Library

[iso20022 Electronic repository download address](https://www.iso20022.org/iso20022-repository/e-repository)

Import the download address of the electronic repository into the iso20022 electronic repository and update the offline repository.

The browser accesses the address of the electronic repository, and click the blue arrow in the red box as shown in the figure below to download the compressed package of the electronic repository.
![image-20220222133621275](https://user-images.githubusercontent.com/97862260/155097952-c7327a47-0874-47aa-82df-bfe232dd6c5e.png)

#### Import

- Import electronic library to CUFIR Kit offline tool

Open CUFIR Kit click file in the upper left corner, and then click Open... In the menu, Select the extracted electronic repository to import.
![image-20220222133050627](https://user-images.githubusercontent.com/97862260/155098237-0488be17-401f-47a9-a3df-d398b7a46c56.png)

#### Data Types

Support all basic data types used by the message, such as（code Sets、Text、Boolean、Indicator、Decimal、Rate、Amount、Quantity、Time、Binary、Schema Types、User Defined）.

![image-20220222150104320](https://user-images.githubusercontent.com/97862260/155098444-0f8bd526-06d8-4903-aee5-c164f16ba9ce.png)

Right click the secondary tree node. For example, code sets can create a new child node. Double click the tertiary node to view or edit it. The Summary interface is expanded by default.

![image-20220222151047552](https://user-images.githubusercontent.com/97862260/155098526-03e1bb1c-afb3-42b2-96e5-e77426259b69.png)

Select the Content interface to add, edit and delete the child node data of the current node.

![image-20220222151210892](https://user-images.githubusercontent.com/97862260/155098585-1382bbbc-7ddc-4b14-ab69-4895ccd0a9d7.png)

Select Impact interface to view Message Sets、Message Definitions、Message Component the data type of the three modules belongs to.

![image-20220222151556231](https://user-images.githubusercontent.com/97862260/155098686-1eceed98-4778-4341-a3e8-6f385ea61d14.png)

#### Business Components

Right click Business Components to create a new child node. Double click its child node to view or edit it. The Summary interface is expanded by default.

![image-20220222152435520](https://user-images.githubusercontent.com/97862260/155098767-1e672e19-94c9-4f93-959e-bb2bcd9abe4d.png)

Select the Content interface to add, edit and delete the child node data of the current node.

![image-20220222152606412](https://user-images.githubusercontent.com/97862260/155098842-22f2c1ad-aafb-40d6-82e3-d47799b5da5a.png)

Select Impact interface to view Message Sets、Message Definitions、Message Component the business component of the three modules belongs to

![image-20220222152632242](https://user-images.githubusercontent.com/97862260/155098904-586ecc58-9650-41cb-b709-3420d35c877b.png)

Select the Impact Analysis interface to view other Business Component the management status of this business component in

![image-20220222155110923](https://user-images.githubusercontent.com/97862260/155100683-53ba5ac7-01a2-4c25-b83e-09c8e90a466c.png)

#### Message Components

Right click Message Components to create a new child node. Double click its child node to view or edit it. The Summary interface is expanded by default

![image-20220222152912039](https://user-images.githubusercontent.com/97862260/155100795-f7bfc07d-c532-470c-b795-d0e040c5c1b4.png)

Select the Content interface to add, edit and delete the child node data of the current node（例如Data Tape、Message Component）.

![image-20220222152931479](https://user-images.githubusercontent.com/97862260/155100867-234ec8bd-9e20-4182-8284-6a2dc8b491e8.png)

Select the Business Trace interface to view the related information of the binding Business Components、Business Element

![image-20220222153020090](https://user-images.githubusercontent.com/97862260/155100928-b131d246-c05a-42d8-9ffc-00d8ad7243c4.png)

Select Impact Analysis interface to view Message Sets、Message Definitions、Message Component the status of this message component in the three modules

![image-20220222153045829](https://user-images.githubusercontent.com/97862260/155101027-16a1153e-3ef7-4d07-8e0e-d75c58ad495b.png)

Select Version/Subsets version association can be viewed in the interface

![image-20220222153106048](https://user-images.githubusercontent.com/97862260/155101107-bc1dfb3e-659f-4316-9996-c4a5e983572e.png)

#### Business Areas

Right click Business Areas to create a new child node. Double click its child node to view or edit it. The Summary interface is expanded by default

![image-20220222153716113](https://user-images.githubusercontent.com/97862260/155102519-961c5f4b-5bc6-4033-8820-450f098d6262.png)

Select the Content interface to add, edit and delete the child node data of the current node（例如Message Definition）.

![image-20220222153804532](https://user-images.githubusercontent.com/97862260/155102565-aa412dc5-21c0-4345-944e-72f1f5c051c2.png)

#### Message Sets

右键点击Message Sets可新建子节点，双击其子节点可进行查看或编辑，默认展开Summary界面

![image-20220222154015901](https://user-images.githubusercontent.com/97862260/155102608-e52a8441-f4cb-4dfd-b047-b57e9483aa36.png)

选择Content界面可添加编辑删除当前节点的子节点数据（例如Message Definition）

![image-20220222154052664](https://user-images.githubusercontent.com/97862260/155102655-70af625b-8253-4524-b233-8d5da73f4397.png)

#### Message Definitions

右键点击Message Definitions可新建子节点，双击其子节点可进行查看或编辑，默认展开Summary界面

![image-20220222154221822](https://user-images.githubusercontent.com/97862260/155102792-221ad450-ea7a-4977-9aee-d695480b3bd5.png)

Select the Content interface to add, edit and delete the child node data of the current node（例如Data Tape、Message Component）.

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


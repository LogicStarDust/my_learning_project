# 一、Docker 简介

## 1. 镜像和容器(image and container)

* 镜像  
   一种轻量级、可执行的独立**软件包**，它包含运行某个软件所需的所有内容，包括到吗、运行时、库、环境变量和配置文件。
* 容器  
   镜像运行时候的**实例**，实际运行镜像的时候内存（即具有状态或用户进程的镜像）。

## 2. Docker与虚拟机的区别

传统虚拟机是虚拟出一套硬件后，在其上运行一套完整的操作系统，然后基于系统运行应用。而docker的容器直接运行于宿主机的内核，容器没有自己的内核且没有硬件虚拟，所以比传统虚拟机轻便。

## 3. Docker的优点

* 灵活：可以把复杂的应用和环境封装一起。
* 轻量：容器共享主机内核。
* 易用：可以直接部署更新和升级。
* 便携：可以本地构建，在任意地方运行。
* 扩展：可以增加和自动化分发容器副本。
* 堆叠：可以直接堆叠垂直服务。
  
## 4. Docker安装（略）

## 5. Docker镜像使用

### 5.1 获取镜像

* 获取  
    命令示例：`docker pull registry.api.weibo.com/dingzk/docker-web`  
    命令解释：`docker pull [选项] [Docker Registry 地址[:端口号]/]仓库名[:标签]`
* 运行  
    命令示例：`docker run -it --rm ubuntu:18.04 bash`  
    命令解释：-it 代表交互终端模式，--rm 代表退出容器后销毁

### 5.2 列出镜像

* 列出命令  
    所有：`docker image ls`  
    根据仓库：`docker image ls ubuntu`  
    根据标签：`docker image ls ubuntu:18.04`  
    过滤查询：`docker image ls -f since=mongo:3.2`  
    过滤查询中since表示之后，替换成before表示之前，也可以使用构建对象时候定义的label过滤，把sine替换成label即可。  
    返回结果包含了 `仓库名`、`标签`、`镜像 ID`、`创建时间` 以及 `所占用的空间`。

    ```bash
    $ docker image ls
    REPOSITORY           TAG                 IMAGE ID            CREATED                SIZE
    <none>               <none>              00285df0df87        5 days ago             342 MB
    ubuntu               18.04               f753707788c5        4 weeks ago            127 MB
    ubuntu               latest              f753707788c5        4 weeks ago            127 MB
    ```

    `镜像 ID`是镜像的唯一标识，一个镜像有多个`标签`
* 镜像体积  
    注意镜像在上传和下载过程中是压缩状态，而`docker image ls`列出的是解压缩后的大小。另外Docker使用Union FS，由于镜像是多层结构的，Docker对不同镜像的相同的层只保留一份。通过df命令查看磁盘占用:```docker system df```。
* 虚悬镜像  
    注意上面的镜像列表中\<none>标记，代表是虚悬镜像，查询命令：```docker image ls -f dangling=true```。是因为有了新的同名镜像，旧镜像被取消名称。建议删除，命令：```docker image prune```
* 中间层镜像  
  为了加速镜像构建、重复利用资源，Docker 会利用 中间层镜像。所以在使用一段时间后，可能会看到一些依赖的中间层镜像。

### 5.3 删除镜像

* 命令  
    `docker image rm [选项] <镜像1> [<镜像2> ...]`  
    命令中的`<镜像>`可以是镜像短id、长id、镜像名或者镜像摘要。id只需要取能够区分镜像的前n位即可。如命令：`docker image rm 501`，删除501开头的本地镜像。  
    注意，本命令可以和`ls`命令相结合，批量删除镜像：`docker image rm $(docker image ls -q redis)`

### 5.4 定制镜像(commit)

* 命令  

    ```bash
    $docker commit
        --author "Tao Wang <twang2218@gmail.com>" \
        --message "修改了默认网页" \
        webserver \
        nginx:v2
    sha256:07e33465974800ce65751acc279adc6ed2dc5ed4e0838f8b86f0c87aa1795214
    ```

    核心就是commit命令，先使用run把容器启动起来，然后进入容器，修改内部文件后使用commit命令就会把修改后的容器生成一个定制镜像，定制镜像包含修改内容。  
    注意：不建议使用commit定制镜像在实际线上使用。因为是黑箱操作，依赖每次修改后人工维护修改内容。一旦修改未记录，时间长了以后，将很难还原修改的目的。

### 5.5 定制镜像(Dockerfile)

Dockerfile是一个文本文件，其中包含一条条指令(instruction),每条指令构建一层，因此每一条指令的内容就是描述该层如何构建。

* FROM命令  
    FROM制定需要的镜像基础，一般使用库里直接封装好的镜像作为基础镜像。docker另外提供一个空白镜像：scratch，如果以此为基础，即表示直接以下面的指令作为第一层镜像。
* RUN命令  
    RUN命令有两种格式：  
  * shell格式：

    ```Dockerfile
    RUN echo '<h1>Hello, Docker!</h1>' > /usr/share/nginx/html/index.html
    ```

    注意:如果你在指定玩基础镜像后，进行了若干命令(比如安装三个软件)，请不要使用多个RUN，这样会导致生成很多层，尽量在一层也就是一个RUN中使用`&&`串联多个命令。另外，构建完毕，请删除无关的文件，减少镜像臃肿。
  * exec格式：  
    `RUN ["可执行文件", "参数1", "参数2"]`，这更像是函数调用中的格式。

* build命令  
    执行`docker build`来构建镜像，如`docker build -t nginx:v3 .`表示构建tag为v3的nginx镜像。

  * 镜像构建的上下文  
        注意build命令`docker build -t nginx:v3 .`最后的"."，表示当前目录，是在指定上下文路径。  
        我们执行docker命令，均不是在本机执行的，而是通过本机的Docker Remote API执行在Docker引擎上的。而执行`docker build`命令的时候，会把上下文路径打包，上传给Docker引擎。在Docekrfile中如果执行COPY命令，针对的即是上下文路径。
* 其他命令
  * copy：复杂文件
  * add：添加文件
  * cmd：执行命令
  * entrypoint：执行命令，支持启动镜像的时候传参数到这个命令
  * env：设置环境变量
  * arg：设置环境变量，但是在容器运行时是不存在的，可以在启动镜像的时候覆盖
  * volume:挂载卷
  * expose:声明暴露端口，注意是声明
  * workdir:切换当前目录，注意RUN后面跟随cd命令，对下一个RUN来说，还是cd前的目录
  * user:指定当前用户
  * healthcheck:健康检查
  * onbuild:可以指定，当且仅当本镜像作为基础镜像的时候执行的命令。

### 5.6 构建镜像

* 全部放入Dockerfile：把所有的构建过程写到一个Dockerfile文件中
* 分散多个Dockerfile：例如，一个镜像复杂构建个编译程序，一个镜像复杂运行服务。第一个构建和编译完成后可以删除掉。大大减少最终镜像的大小
* 多阶段构建：v17.5以上支持
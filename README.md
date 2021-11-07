# VariousCases
个人学习使用到的Case
## [JVM相关](https://github.com/lhj502819/VariousCases/blob/main/CasesForJVM)
* [字节码解读](https://github.com/lhj502819/VariousCases/blob/main/CasesForJVM/src/main/java/cn/onenine/jvm/bytecode/Hello.java)
* [类加载器](https://github.com/lhj502819/VariousCases/blob/main/CasesForJVM/src/main/java/cn/onenine/jvm/classloader)
    - [自定义类加载器](https://github.com/lhj502819/VariousCases/blob/main/CasesForJVM/src/main/java/cn/onenine/jvm/classloader/customclassloader)
    - [运行期加载类](https://github.com/lhj502819/VariousCases/blob/main/src/main/java/cn/onenine/jvm/classloader/JVMAppClassLoaderAddURL.java)
    - [打印类加载器加载了哪些类](https://github.com/lhj502819/VariousCases/blob/main/src/main/java/cn/onenine/jvm/classloader/JVMClassLoaderPrintPath.java)
* [String常量池](https://www.yuque.com/lihongjian/fx3n4r/yuqzoi)
* [JDK动态代理Demo](https://github.com/lhj502819/VariousCases/blob/main/CasesForJVM/src/main/java/cn/onenine/jvm/dynamicproxy)
* [finalize分析](https://github.com/lhj502819/VariousCases/blob/main/src/main/java/cn/onenine/jvm/gc/FinalizeEscapeGC.java)
       博客：https://www.yuque.com/lihongjian/fx3n4r/qwlcnh
* [伪共享分析](https://github.com/lhj502819/VariousCases/blob/main/src/main/java/cn/onenine/jvm/gc/FalseSharingDemo.java)
      伪共享分析博客：https://www.yuque.com/lihongjian/fx3n4r/cnhs3h#RSAqI
* [引用类型](https://github.com/lhj502819/VariousCases/blob/main/CasesForJVM/src/main/java/cn/onenine/jvm/reference)
* [自定义插入式注解，校验代码命名格式](https://github.com/lhj502819/VariousCases/blob/main/CasesForJVM/src/main/java/cn/onenine/jvm/annotatiomprocesser)
    * [测试案例](https://github.com/lhj502819/VariousCases/blob/main/CasesForJVM/CodeNameCheckTest)
      1. 引用自定义插入式注解jar包，maven compile即可
## [Netty权威指南](https://github.com/lhj502819/VariousCases/tree/main/CaseForNetty)

* [BIO代码示例](https://github.com/lhj502819/VariousCases/tree/main/CaseForNetty/src/main/java/cn/znnine/netty/bio/v1)

* [伪异步I/O代码示例](https://github.com/lhj502819/VariousCases/tree/main/CaseForNetty/src/main/java/cn/znnine/netty/bio/v1)

* [Java NIO 代码示例](https://github.com/lhj502819/VariousCases/tree/main/CaseForNetty/src/main/java/cn/znnine/netty/nio)

### NIO编程的优点
1. 客户端发起的连接操作是异步的，可以通过在多路复用器注册OP_CONNECT等待后续结果，不需要像之前的客户端那样被同步阻塞
2. SocketChannel的读写操作都是异步的，如果没有可读写的数据它不会同步等待，直接返回，这样I/O
通信线程就可以处理其他链路，不需要同步等待这个链路可用
3. 线程模型的优化：由于JDK的Selector在Linux等主流操作系统上通过epoll实现，没有连接句柄数的限制（只受限于操作系统的最大句柄数
   或者对单个进程的句柄限制），这意味着一个Selector线程可用同时处理成千上万个客户端连接，而且性能不会随着客户端的增加而线性下降。因此非常适合做高性能、高负载的网络服务器。
-----------------------------------------------------------------------
* [Java AIO 代码示例](https://github.com/lhj502819/VariousCases/tree/main/CaseForNetty/src/main/java/cn/znnine/netty/aio)

* [Netty Server、Client简单示例](https://github.com/lhj502819/VariousCases/tree/main/CaseForNetty/src/main/java/cn/znnine/netty/nio/netty/v1)

* [Netty模拟TCP粘包问题](https://github.com/lhj502819/VariousCases/tree/main/CaseForNetty/src/main/java/cn/znnine/netty/nio/netty/v2)

* [Netty解决TCP粘包问题](https://github.com/lhj502819/VariousCases/tree/main/CaseForNetty/src/main/java/cn/znnine/netty/nio/netty/v3)

* [DelimiterBasedFrameDecoder的使用](https://github.com/lhj502819/VariousCases/tree/main/CaseForNetty/src/main/java/cn/znnine/netty/nio/netty/delimiter)
* [FixedLengthFrameDecoder的使用](https://github.com/lhj502819/VariousCases/tree/main/CaseForNetty/src/main/java/cn/znnine/netty/nio/netty/fixedlength)

## 编解码相关
### MessagePack
* [MessagePack相关代码示例](https://github.com/lhj502819/VariousCases/tree/main/CaseForNetty/src/main/java/cn/znnine/netty/codec/msgpack)
### Google Protobuf
将数据结构以.proto文件进行描述，通过代码生成工具可以生成对应数据结果的POJO对象和Protobuf相关的方法和属性
#### 优点
* 在谷歌内部长期使用，产品成熟度高
* 跨语言、支持多种语言，包括C++、Java和Python
* 编码后的消息更小，更加有利于存储和传输
* 编解码的性能非常高
* 支持不通协议版本的向后兼容
* 支持定义可选和必选字段
#### [Google Protobuf使用示例](https://github.com/lhj502819/VariousCases/tree/main/CaseForNetty/src/main/java/cn/znnine/netty/codec/protobuf)
* 可以使用protoc.exe通过.proto文件生成.java文件，命令为：protoc.exe --java_out=.\ .\SubscribeReq.proto，文件格式可看[代码](https://github.com/lhj502819/VariousCases/tree/main/CaseForNetty/src/main/java/cn/znnine/netty/codec/protobuf/proto)

#### 使用的注意事项 
ProtobufDecoder仅仅负责解码，它不支持读半包。因此在ProtobufDecoder前面，一定要有能有处理读半包的解码器，有以下三种方式可以选择
  1. 使用Netty提供的ProtobufVarint32FrameDecoder，它可以处理半包消息
  2. 继承Netty提供的通用半包解码器LengthFieldBasedFrameDecoder；
  3. 继承ByteToMessageDecoder类，自己处理半包消息
    
### JBoss Marshalling
JBoss Marshalling是一个Java对象的序列化API包，修正了JDK自带的序列化包的很多问题，但又保持跟java.io.Serializable接口的兼容。
同时增加了一些可调的参数和附加特性，并且这些参数和特性可通过工厂类进行配置
#### 优点
* 可插拔的类解析器，提供更加便捷的类加载定制策略，通过一个接口即可实现定制
* 可插拔的对象替换技术，不需要通过继承的方式
* 可插拔的预定义类缓存表，可以减少序列化的字节数组长度，提升常用类型的对象序列化性能
* 无须实现java.io.Serializable接口，即可实现Java序列化
* 通过缓存技术提升对象的序列化性能
#### 缺点
相比于前边介绍的两种编码框架，JBoss Marshalling更多的是JBoss内部使用，应用范围有限

#### [代码示例](https://github.com/lhj502819/VariousCases/tree/main/CaseForNetty/src/main/java/cn/znnine/netty/codec/marshalling)

## 基于Netty开发Http文件服务器
#### [代码示例](https://github.com/lhj502819/VariousCases/tree/main/CaseForNetty/src/main/java/cn/znnine/netty/http/file)
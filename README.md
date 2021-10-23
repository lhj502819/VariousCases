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
## [Netty相关](https://github.com/lhj502819/VariousCases/tree/main/CaseForNetty)

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
* [MessagePack相关代码示例](https://github.com/lhj502819/VariousCases/tree/main/CaseForNetty/src/main/java/cn/znnine/netty/msgpack)
* [Google Protobuf使用示例](https://github.com/lhj502819/VariousCases/tree/main/CaseForNetty/src/main/java/cn/znnine/netty/protobuf)


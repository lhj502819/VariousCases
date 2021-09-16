package cn.onenine.jvm.bytecode;

/**
 * 字节码分析
 *   本地变量表如下
 *       LocalVariableTable:
 *         Start  Length  Slot  Name   Signature
 *             0      85     0  args   [Ljava/lang/String;
 *             2      83     1  num1   I
 *             6      79     2  num2   D
 *            11      74     4  num3   J
 *            14      71     6  num4   B
 *            54      30     7     i   I
 *
 * @author lihongjian
 * @since 2021/8/29
 */
public class Hello {
    public static void main(String[] args) {
        /**
         * iconst_1 将int型1推送至栈顶
         * istore_1 将栈顶int型数值存入第二个本地变量
         */
        int num1 = 1; //字面量1

        /**
         * ldc2_w 将long或double型常量值从常量池中推送至栈顶(宽索引)
         * dstore_2  将栈顶double型数值存入第三个本地变量
         */
        double num2 = 2.0D; //大小写D都可以

        /**
         *  ldc2_w       --->  将long或double型常量值从常量池中推送至栈顶(宽索引)
         *  lstore  4    --->  将栈顶long型数值存入指定本地变量
         */
        long num3 = 3L; //大小写L都可以

        /**
         * iconst_4     --->  将int型4推送至栈顶
         * lstore  6    --->  将栈顶long型数值存入指定本地变量（第6个）
         */
        byte num4 = 4; //可以直接赋予[-128,127]范围内的字面量

        /**
         * ldc         将int,float或String型常量值从常量池中推送至栈顶 ，在文章《String与字符串常量池的恩怨情仇》中已经讲过{@link https://www.yuque.com/lihongjian/fx3n4r/yuqzoi#6wQBX}
         * invokevirtual #22  // Method java/lang/String.length:()I 调用String实例的方法length
         * bipush     将单字节的常量值(-128~127)推送至栈顶(10)
         * if_icmpgt  比较栈顶两int型数值大小, 当结果大于0时跳转 (if)
         */
        if("".length() <= 10){
            //错误用法：num2 + num3 = 2.03
            /**
             * getstatic // Field java/lang/System.out:Ljava/io/PrintStream;   获取指定类的静态域, 并将其压入栈顶
             * new   // class java/lang/StringBuilder  创建一个对象, 并将其引用引用值压入栈顶
             * dup 复制栈顶数值并将复制值压入栈顶 (这里复制的就是刚刚创建的StringBuilder对象堆内存的引用)
             * ldc  #36  // String 错误用法: num2 + num 3 =
             *  invokespecial #38  // Method java/lang/StringBuilder."<init>":(Ljava/lang/String;)V
             *  dload_2 将第三个double型本地变量推送至栈顶(num2)
             *  invokevirtual #41  // Method java/lang/StringBuilder.append:(D)Ljava/lang/StringBuilder;
             *  lload 4   将指定的long型本地变量推送至栈顶（第四个num3）
             *  invokevirtual #45                 // Method java/lang/StringBuilder.append:(J)Ljava/lang/StringBuilder;调StringBuilder#append拼接字符串
             *  invokevirtual #48                 // Method java/lang/StringBuilder.toString:()Ljava/lang/String;调用StringBuilder#toString生成String对象
             *  invokevirtual #52                 // Method java/io/PrintStream.println:(Ljava/lang/String;)V 调用PrintStream#println输出
             */
            System.out.println("错误用法: num2 + num 3 = " + num2 + num3);
        }

        /**
         * iconst_0  将int类型0推到栈顶
         * istore 7  将栈顶int型数值存入指定本地变量（这里的第七个本地变量是临时的）
         * goto 78 无条件跳转
         * getstatic 获取指定类的静态域, 并将其压入栈顶
         * ldc   // String 四则运算：num1 * num4 =
         *  invokevirtual // Method java/io/PrintStream.println:(Ljava/lang/String;)V
         *  getstatic     // Field java/lang/System.out:Ljava/io/PrintStream;
         *  iload_1 将第二个int型本地变量推送到栈顶(num1)
         *  iload  6 将指定 的int型本地变量推送到栈顶（i）
         *  imul 将栈顶两int型数值相乘并将结果压入栈顶
         *  invokevirtual  // Method java/io/PrintStream.println:(I)V
         *  iinc 7,1 将第七个本地变量+1
         *  iload 7，将第七个本地变量推送到栈顶
         *  if_icmplt  比较栈顶两int型数值大小, 当结果小于0时跳转（临时变量和num1）
         */
        for (int i = 0; i < num1 ; i++){
            //四则运算：num1 * num4 = 4
            System.out.println("四则运算：num1 * num4 = " );
            System.out.println(num1 * num4);
        }

        /**
         * bipush   将单字节的常量值100(-128~127)推送至栈顶
         *
         * invokestatic  #62   // Method java/lang/Integer.valueOf:(I)Ljava/lang/Integer; 拆箱
         *
         * istore        7
         *
         */
        Integer num5 = 100;

        /**
         *  bipush        101
         *
         *  istore        8
         *  对比可看到少了一步拆箱,尽量使用int类型
         */
        int num6 = 101;

        System.out.println(num5 + num6);

    }

}

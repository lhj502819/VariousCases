package cn.znnine.jvm.annotatiomprocesser;

/**
 * 自定义代码命名检查测试
 *
 * @author lihongjian
 * @since 2021/9/4
 */
public class NameCheckerTest {
    enum colors{
        red,blue,green;
    }

    static final  int _FORTY_TWO = 42;

    public static int NOT_A_CONSTANT= _FORTY_TWO;
    protected void FALSEMETHOD(){
        return;
    }
    public void NOTcamelCASEmethodName(){
        return;
    }

    public static void main(String[] args) {
        System.out.println(1);
    }
}

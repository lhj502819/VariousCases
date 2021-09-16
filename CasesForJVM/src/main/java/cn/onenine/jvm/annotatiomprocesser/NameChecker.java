package cn.onenine.jvm.annotatiomprocesser;

import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.*;
import javax.lang.model.util.ElementScanner8;
import javax.tools.Diagnostic;

import java.util.EnumSet;

import static javax.lang.model.element.ElementKind.*;
import static javax.lang.model.element.Modifier.*;

/**
 * 程序命名规范编译器插件
 * 如果程序命名不合规范，将会输出一个编译器的WARBING信息
 *
 * @author lihongjian
 * @since 2021/9/4
 */
public class NameChecker {

    private final Messager messager;

    NameCheckScanner nameCheckScanner = new NameCheckScanner();

    NameChecker(ProcessingEnvironment processingEnv) {
        this.messager = processingEnv.getMessager();
    }

    /**
     * 对Java程序命名进行检查，根据《Java语言规范》第三版第6.8节的要求，Java程序命名应当符合下列各式：
     * 类或接口：符合驼峰式命名法，首字母大写
     * 方法：符合驼峰式命名法，首字母小写
     * 字段：
     * 类、实例变量：符合驼峰式命名法，首字母小写
     * 常量：要求全部大写
     *
     * @param element
     */
    public void checkName(Element element) {
        nameCheckScanner.scan(element);
    }

    /**
     * 名称检查器实现类，继承JDK8中新提供的ElementScanner8，将会以Visitor模式访问抽象语法树中的元素
     */
    private class NameCheckScanner extends ElementScanner8<Void, Void> {

        /**
         * 此方法用于检查Java类
         */
        @Override
        public Void visitType(TypeElement e, Void p) {
            scan(e.getTypeParameters(), p);
            checkCamelCase(e, true);
            super.visitType(e, p);
            return null;
        }

        /**
         * 检查方法命名是否合法
         */
        @Override
        public Void visitExecutable(ExecutableElement e, Void p) {
            if (e.getKind() == METHOD) {
                Name name = e.getSimpleName();
                if (name.contentEquals(e.getEnclosingElement().getSimpleName())) {
                    messager.printMessage(Diagnostic.Kind.WARNING, "一个普通方法" + name + "不应当与类名重复，避免与构造函数产生混淆", e);
                    checkCamelCase(e, false);
                }
            }
            super.visitExecutable(e, p);
            return null;
        }

        /**
         * 检查变量命名是否合法
         */
        @Override
        public Void visitVariable(VariableElement e, Void p) {
            //如果这个Variable是枚举或者是常量，则按大写命名检查，否则按照驼峰式命名规则检查
            if (e.getKind() == ENUM_CONSTANT || e.getConstantValue() != null || checkIsConstant(e)) {
                checkAllCaps(e);
            } else {
                checkCamelCase(e, false);
            }
            super.visitVariable(e, p);
            return null;
        }

        /**
         * 判断一个变量是否是常量
         *
         * @param e
         * @return
         */
        private boolean checkIsConstant(VariableElement e) {
            if (e.getEnclosingElement().getKind() == INTERFACE) {
                return true;
            } else if (e.getKind() == FIELD && e.getModifiers().containsAll(EnumSet.of(PUBLIC, STATIC, FINAL))) {
                return true;
            } else {
                return false;
            }
        }

        /**
         * 检查输入的Element是否符合驼峰式命名法，如果不符合，则输出警告信息
         *
         * @param initialCaps ：是否需要首字母大写判断
         */
        private void checkCamelCase(Element e, boolean initialCaps) {
            String name = e.getSimpleName().toString();
            boolean previousUpper = false;
            boolean conventional = true;
            int firstCodePoint = name.codePointAt(0);

            if (Character.isUpperCase(firstCodePoint)) {
                previousUpper = true;
                if (!initialCaps) {
                    messager.printMessage(Diagnostic.Kind.WARNING, "名称" + name + "应当以小写字母开头", e);
                }
            } else if (Character.isLowerCase(firstCodePoint)) {
                if (initialCaps) {
                    messager.printMessage(Diagnostic.Kind.WARNING, "名称" + name + "应当以大写字母开头", e);
                }
            } else {
                conventional = false;
            }
            if (conventional) {
                int cp = firstCodePoint;
                for (int i = Character.charCount(cp); i < name.length(); i += Character.charCount(cp)) {
                    cp = name.codePointAt(i);
                    if (Character.isUpperCase(cp)) {
                        if (previousUpper) {
                            conventional = false;
                            break;
                        }
                        previousUpper = true;
                    } else {
                        previousUpper = false;
                    }
                }
            }

            if (!conventional) {
                messager.printMessage(Diagnostic.Kind.WARNING, "名称" + name + "应当符合驼式命名法（Camel Case Names）", e);
            }
        }

        private void checkAllCaps(Element e) {
            String name = e.getSimpleName().toString();
            boolean conventional = true;

            int firstCodePoint = name.codePointAt(0);

            if(Character.isUpperCase(firstCodePoint)){
                conventional = false;
            }else {
                boolean previouseUnderscore = false;
                int cp = firstCodePoint;
                for (int i = Character.charCount(cp); i < name.length(); i += Character.charCount(cp)) {
                    cp = name.codePointAt(i);
                    if(cp == (int) '_'){
                        if (previouseUnderscore) {
                            conventional = false;
                            break;
                        }
                        previouseUnderscore = true;
                    }else {
                        previouseUnderscore = false;
                        if(!Character.isUpperCase(cp) && !Character.isDigit(cp)){
                            conventional = false;
                            break;
                        }
                    }
                }
            }
            if(!conventional){
                messager.printMessage(Diagnostic.Kind.WARNING,"常量" + name + "应当全部以大写字母或下划线命名，并且以字母开头");
            }
        }
    }


}

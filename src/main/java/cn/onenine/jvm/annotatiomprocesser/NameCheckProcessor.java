package cn.onenine.jvm.annotatiomprocesser;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.util.Set;

/**
 * Description：命名规则检查校验处理器
 *
 * @author li.hongjian
 * @email lhj502819@163.com
 * @since 2021/9/3
 */
//支持所有的注解类型
@SupportedAnnotationTypes("*")
//只支持JDK8的Java代码
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class NameCheckProcessor extends AbstractProcessor{
    private NameChecker nameChecker;
    @Override
    public void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        nameChecker = new NameChecker(processingEnv);
    }

    /*** 对输入的语法树的各个节点进行名称检查 */
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (!roundEnv.processingOver()) {
            for (Element element : roundEnv.getRootElements()) {
                nameChecker.checkName(element);
            }
        }
        return false;
    }
}

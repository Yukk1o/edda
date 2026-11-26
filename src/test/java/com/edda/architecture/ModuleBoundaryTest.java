package com.edda.architecture;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.slices;

/**
 * 模块边界门禁
 *
 * <p>模块化单体的边界如果没有自动化约束,三个月后一定腐化
 * 某个临时的 import 会让两个领域缠在一起,等到想拆服务时才发现拆不动
 *
 * <p>这些规则在 mvn verify 里跑,失败即构建失败
 * 新增允许的依赖时必须同时改本文件和对应的 package-info,并说明理由
 *
 * <p>注意本测试只守显式依赖,即 import 与调用
 * 通过 SQL 字符串,共享 DTO 演化产生的隐式耦合抓不到
 * 只能靠 common 包保持极薄,加上评审
 *
 * @author Yukk1o
 * @since 2026-11-26
 */
@AnalyzeClasses(packages = "com.edda", importOptions = ImportOption.DoNotIncludeTests.class)
class ModuleBoundaryTest {

    private static final String[] DOMAIN_PACKAGES = {
            "..auth..", "..course..", "..coupon..", "..stock..",
            "..order..", "..payment..", "..enroll..", "..comment.."
    };

    /**
     * 除 auth 之外的领域,auth 自己的类互相引用是正常的
     */
    private static final String[] OTHER_DOMAINS = {
            "..course..", "..coupon..", "..stock..",
            "..order..", "..payment..", "..enroll..", "..comment.."
    };

    /**
     * 营销域不得依赖交易域
     * 券不知道订单的存在,只记得自己被哪张单核销
     */
    @ArchTest
    static final ArchRule couponMustNotDependOnOrder =
            noClasses().that().resideInAPackage("..coupon..")
                    .should().dependOnClassesThat().resideInAPackage("..order..")
                    .because("券若依赖订单,退款退券时就会成环")
                    .allowEmptyShould(true);

    /**
     * 账号域是依赖图的叶子,不得依赖任何其他领域
     */
    @ArchTest
    static final ArchRule authMustNotDependOnOtherDomains =
            noClasses().that().resideInAPackage("..auth..")
                    .should().dependOnClassesThat().resideInAnyPackage(OTHER_DOMAINS)
                    .because("auth 一旦依赖业务域,依赖图立刻出现环")
                    .allowEmptyShould(true);

    /**
     * common 只能被依赖,不能依赖领域包,否则它会变成什么都能塞的大杂烩
     */
    @ArchTest
    static final ArchRule commonMustNotDependOnDomains =
            noClasses().that().resideInAPackage("..common..")
                    .should().dependOnClassesThat().resideInAnyPackage(DOMAIN_PACKAGES)
                    .because("common 一旦依赖领域,所有领域都会被迫互相依赖")
                    .allowEmptyShould(true);

    /**
     * 领域之间不得出现循环依赖,A 到 B 再到 A 这种必须当场发现
     */
    @ArchTest
    static final ArchRule domainsMustBeFreeOfCycles =
            slices().matching("com.edda.(*)..")
                    .should().beFreeOfCycles()
                    .because("循环依赖是拆分服务时最难解的一种纠缠")
                    .allowEmptyShould(true);

    /**
     * domain 不得依赖框架,换存储与换 Web 框架时领域层不该动
     */
    @ArchTest
    static final ArchRule domainMustNotDependOnFrameworks =
            noClasses().that().resideInAPackage("..domain..")
                    .should().dependOnClassesThat()
                    .resideInAnyPackage("org.springframework..", "com.baomidou..", "jakarta..", "cn.dev33..")
                    .because("领域层一旦出现框架注解,换实现就要改业务代码")
                    .allowEmptyShould(true);

    /**
     * domain 是最内层,不得依赖任何外层
     */
    @ArchTest
    static final ArchRule domainMustNotDependOnOuterLayers =
            noClasses().that().resideInAPackage("..domain..")
                    .should().dependOnClassesThat().resideInAnyPackage("..api..", "..app..", "..infra..")
                    .because("依赖方向只能是 api 到 app 到 domain,infra 实现 domain 的端口")
                    .allowEmptyShould(true);

    /**
     * app 只依赖端口,不依赖具体实现
     */
    @ArchTest
    static final ArchRule appMustNotDependOnInfra =
            noClasses().that().resideInAPackage("..app..")
                    .should().dependOnClassesThat().resideInAPackage("..infra..")
                    .because("app 一旦依赖 infra,拆服务时业务逻辑搬不走")
                    .allowEmptyShould(true);

    /**
     * api 只做协议适配,不得绕过 app 直接碰实现
     */
    @ArchTest
    static final ArchRule apiMustNotDependOnInfra =
            noClasses().that().resideInAPackage("..api..")
                    .should().dependOnClassesThat().resideInAPackage("..infra..")
                    .because("绕过 app 等于绕过业务规则")
                    .allowEmptyShould(true);

    /**
     * infra 只实现端口,不得反向依赖接口层
     */
    @ArchTest
    static final ArchRule infraMustNotDependOnApi =
            noClasses().that().resideInAPackage("..infra..")
                    .should().dependOnClassesThat().resideInAPackage("..api..")
                    .because("infra 若依赖 api,适配器就绑死了某个接口形态")
                    .allowEmptyShould(true);
}

package com.lhy.interview.comprehensive;

import java.lang.reflect.Field;
import java.util.Arrays;

public class StringTest {

    // Helper: reflectively get the private 'value' field from String (char[] or byte[])
    private static Object getValueArray(String s) {
        try {
            Field valueField = String.class.getDeclaredField("value");
            valueField.setAccessible(true);
            return valueField.get(s);
        } catch (Throwable t) {
            System.out.println("无法通过反射访问 String.value: " + t);
            return null;
        }
    }

    private static void inspect(String label, String s) {
        System.out.println("--- " + label + " ---");
        System.out.println("字符串内容: " + s);
        System.out.println("对象 identityHashCode: " + System.identityHashCode(s));
        Object val = getValueArray(s);
        if (val != null) {
            Class<?> compType = val.getClass().getComponentType();
            System.out.println("内部数组类型: " + compType + " (类名: " + val.getClass().getName() + ")");
            System.out.println("内部数组 identityHashCode: " + System.identityHashCode(val));
            if (val instanceof byte[]) {
                System.out.println("内部数组 bytes: " + Arrays.toString((byte[]) val));
            } else if (val instanceof char[]) {
                System.out.println("内部数组 chars: " + Arrays.toString((char[]) val));
            } else {
                System.out.println("内部数组 toString: " + val);
            }
        }
    }

    public static void main(String[] args) {
        System.out.println("运行 String 测试（注意：结果依赖于运行时的 JDK 版本，不同 JDK 行为有所差异，例如 6/7/8/9+）。");
        System.out.println("JVM 版本: " + System.getProperty("java.version") + ", 规范版本: " + System.getProperty("java.specification.version"));

        // Case A: literal exists in class constant pool (because we use the literal here)
        String literal = "abc";
        String newFromLiteral = new String("abc");

        inspect("字面量 abc", literal);
        inspect("通过字面量后 new String(\"abc\")", newFromLiteral);

        System.out.println("字面量 == newFromLiteral : " + (literal == newFromLiteral));
        System.out.println("字面量 == newFromLiteral.intern() : " + (literal == newFromLiteral.intern()));
        System.out.println("newFromLiteral == newFromLiteral.intern() : " + (newFromLiteral == newFromLiteral.intern()));

        // Case B: create a String without using the literal "xyz" in the class constant pool
        // Use char[] constructor so the content was not a compile-time literal in the class
        String builtNoLiteral = new String(new char[] {'x','y','z'});
        inspect("通过 char[] 构造（非字面量 xyz）", builtNoLiteral);

        String interned = builtNoLiteral.intern();
        inspect("调用 intern() 后的 xyz", interned);

        System.out.println("builtNoLiteral == interned : " + (builtNoLiteral == interned));
        System.out.println("interned == \"xyz\" : " + (interned == "xyz"));

        // Case C: show behaviour when calling intern on a new String created from an existing literal
        String sLiteral = "hello_interview_example";
        String s2 = new String("hello_interview_example");
        System.out.println("sLiteral == s2 : " + (sLiteral == s2));
        System.out.println("sLiteral == s2.intern() : " + (sLiteral == s2.intern()));

        // Summary / hints for interviewer
        System.out.println("\n结果解读提示：");
        System.out.println("- 在 JDK 6（PermGen）中，字面量保存在永久代；new String(\"abc\") 会在堆上创建一个新的 String，对象的内部数组在某些实现中可能与常量池共享（指向相同的 char[]）。");
        System.out.println("- 在 JDK 7/8 中，字符串常量池移动到堆；new String(\"abc\") 仍会创建堆对象，内部数组是否共享取决于具体实现（实现细节）。");
        System.out.println("- 在 JDK 9+（Compact Strings）中，String 改用 byte[] 存储；new String(\"abc\") 会复制字节，内部数组通常不会与常量池共享（是不同的对象）。");
        System.out.println("- 关于 intern()：在现代 JDK 中，常量池在堆上，intern() 若常量池中不存在会将引用放入池中并返回该引用；在旧的 JDK 中，intern() 可能会将内容复制到永久代。");
    }
}

package com.sdu.springframework.core.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 类操作工具类
 */
@Slf4j
public class ClassUtil {

    public static ClassLoader getClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }

    /**
     * 加载类
     * @param className 类名
     * @param initialized 是否初始化
     * @return 加载到的类
     */
    public static Class<?> loadClass(String className, boolean initialized) {
        Class<?> clazz;
        try {
            // 使用类加载器加载类
            clazz = Class.forName(className, initialized, getClassLoader());
        } catch (ClassNotFoundException e) {
            log.error("Fail to load class: {}", className, e);
            throw new RuntimeException(e);
        }
        return clazz;
    }

    /**
     * 加载初始化的类
     */
    public static Class<?> loadClass(String className) {
        return loadClass(className, true);
    }

    /**
     * 获取指定包名下的所有类
     * @param packageName 包名
     * @return Set<Class<?>> 包名下的所有类
     */
    public static Set<Class<?>> getClasses(String packageName) {
        Set<Class<?>> classSet = new HashSet<>();
        try {
            // 获取包名对应的URL
            URL resource = getClassLoader().getResource(packageName.replace(".", "/"));
            if (null != resource) {
                String protocol = resource.getProtocol();
                // 如果是文件类型的类资源
                if ("file".equals(protocol)) {
                    String packagePath = resource.getPath().replaceAll("%20", " ");
                    // 递归添加类到classSet
                    addClass(classSet, packagePath, packageName);
                } else if ("jar".equals(protocol)) {
                    // 如果是JAR文件
                    JarURLConnection jarURLConnection = (JarURLConnection) resource.openConnection();
                    if (jarURLConnection != null) {
                        JarFile jarFile = jarURLConnection.getJarFile();
                        if (jarFile != null) {
                            Enumeration<JarEntry> jarEntries = jarFile.entries();
                            while (jarEntries.hasMoreElements()) {
                                JarEntry jarEntry = jarEntries.nextElement();
                                String jarEntryName = jarEntry.getName();
                                if (jarEntryName.endsWith(".class")) {
                                    // 从JAR文件中提取类名
                                    String className = jarEntryName.substring(0, jarEntryName.lastIndexOf(".")).replaceAll("/", ".");
                                    // 加载类但不初始化，然后添加到classSet
                                    doAddClass(classSet, className);
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error("Fail to get class set", e);
            throw new RuntimeException(e);
        }
        return classSet;
    }

    private static void addClass(Set<Class<?>> classSet, String packagePath, String packageName) {
        File[] files = new File(packagePath).listFiles(file -> (file.isFile() && file.getName().endsWith(".class")) || file.isDirectory());
        if (null == files) return;
        for (File file : files) {
            String fileName = file.getName();
            if (file.isFile()) {
                String className = fileName.substring(0, fileName.lastIndexOf("."));
                if (StringUtils.isNoneEmpty(className)) {
                    className = packageName + "." + className;
                }
                doAddClass(classSet, className);
            } else {
                String subPackagePath = packagePath + "/" + fileName;
                String subPackageName = packageName + "." + fileName;
                addClass(classSet, subPackagePath, subPackageName);
            }
        }
    }

    private static void doAddClass(Set<Class<?>> classSet, String className) {
        Class<?> clazz = loadClass(className, false);
        classSet.add(clazz);
    }
}

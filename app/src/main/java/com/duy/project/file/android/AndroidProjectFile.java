package com.duy.project.file.android;

import com.android.annotations.Nullable;
import com.duy.project.file.java.JavaProjectFile;
import com.google.common.base.MoreObjects;

import java.io.File;
import java.io.IOException;

/**
 * Created by Duy on 05-Aug-17.
 */
@SuppressWarnings("ResultOfMethodCallIgnored")
public class AndroidProjectFile extends JavaProjectFile {
    /* Output */
    private final File apkUnsigned;
    private final File apkUnaligned;
    public File xmlManifest;
    public File resourceFile;
    private KeyStore keystore;
    /* PROJECT */
    private File dirRes;
    private File dirAssets;
    private File classR;
    private File dirOutApk;

    public AndroidProjectFile(File dirRoot,
                              @Nullable String mainClassName,
                              @Nullable String packageName,
                              String projectName,
                              String classpath) {
        super(dirRoot, mainClassName, packageName, projectName, classpath);

        dirRes = new File(dirSrcMain, "res");
        dirAssets = new File(dirSrcMain, "assets");
        xmlManifest = new File(dirSrcMain, "AndroidManifest.xml");

        dirOutApk = new File(dirOutput, "apk");
        apkUnsigned = new File(dirOutput, "app-unsigned-debug.apk");
        apkUnaligned = new File(dirOutput, "app-unaligned-debug.apk");

        if (packageName != null) {
            classR = new File(dirJava,
                    packageName.replace(".", File.separator) + File.separator + "R.java");
        }

        resourceFile = new File(dirBuild, "resources.res");
        dexedClassesFile = new File(dirBuild, "classes.dex");
        keystore = new KeyStore(new File(dirProject, "keystore.jks"), "1234567".toCharArray(), "android", "1234567".toCharArray());
    }

    public KeyStore getKeyStore() {
        return keystore;
    }

    public File getXmlManifest() throws IOException {
        if (!xmlManifest.exists()) {
            xmlManifest.getParentFile().mkdirs();
            xmlManifest.createNewFile();
        }
        return xmlManifest;
    }

    public File getApkUnaligned() throws IOException {
//        if (!apkUnaligned.exists()) {
//            apkUnaligned.getParentFile().mkdirs();
//            apkUnaligned.createNewFile();
//        }
        return apkUnaligned;
    }

    public File getResourceFile() throws IOException {
        if (!resourceFile.exists()) {
            resourceFile.getParentFile().mkdirs();
            resourceFile.createNewFile();
        }
        return resourceFile;
    }

    public void clean() {
        super.clean();
    }


    public void mkdirs() {
        super.mkdirs();
        if (!dirRes.exists()) dirRes.mkdirs();
        if (!dirAssets.exists()) dirAssets.mkdirs();
        if (!dirDexedLibs.exists()) dirDexedLibs.mkdirs();
        dirRes.setReadable(true, true);
        dirAssets.setReadable(true, true);
        dirDexedLibs.setReadable(true, true);

        File menu = new File(dirRes, "menu");
        if (!menu.exists()) menu.mkdirs();
        File layout = new File(dirRes, "layout");
        if (!layout.exists()) layout.mkdirs();
        File drawable = new File(dirRes, "drawable");
        if (!drawable.exists()) drawable.mkdirs();
        drawable = new File(dirRes, "drawable-hdpi");
        if (!drawable.exists()) drawable.mkdirs();
        drawable = new File(dirRes, "drawable-mdpi");
        if (!drawable.exists()) drawable.mkdirs();
        drawable = new File(dirRes, "drawable-xhdpi");
        if (!drawable.exists()) drawable.mkdirs();
        File value = new File(dirRes, "values");
        if (!value.exists()) value.mkdirs();
    }


    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .toString();
    }

    public File getApkUnsigned() throws IOException {
        if (!apkUnsigned.exists()) {
            apkUnsigned.getParentFile().mkdirs();
            apkUnsigned.createNewFile();
        }
        return apkUnsigned;
    }

    public File getDirRes() {
        if (!dirRes.exists()) {
            dirRes.mkdirs();
            dirRes.setReadable(true, true);
        }
        return dirRes;
    }

    public File getDirAssets() {
        if (!dirAssets.exists()) {
            dirAssets.mkdirs();
            dirAssets.setReadable(true, true);
        }
        return dirAssets;
    }

    public File getClassR() throws IOException {
        if (!classR.exists()) {
            classR.getParentFile().mkdirs();
            classR.createNewFile();
            classR.setReadable(true, true);
        }
        return classR;
    }

    public File getDirLayout() {
        File file = new File(getDirRes(), "layout");
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }
}

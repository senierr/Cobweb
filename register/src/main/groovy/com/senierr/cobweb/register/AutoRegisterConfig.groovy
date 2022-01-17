package com.senierr.cobweb.register

import org.gradle.api.Project

/**
 * 配置信息
 *
 * @author billy.qi
 */
class AutoRegisterConfig {

    public ArrayList<Map<String, Object>> registerInfo = []

    ArrayList<RegisterInfo> list = new ArrayList<>()

    Project project
    def cacheEnabled = true

    AutoRegisterConfig() {}

    void convertConfig() {
        // 应用入口
        Map<String, Object> applicationMap = new HashMap<String, Object>()
        applicationMap.put("scanInterface", "com.senierr.cobweb.component.IApplicationDelegate")
        applicationMap.put("codeInsertToClassName", "com.senierr.cobweb.Cobweb")
        applicationMap.put("codeInsertToMethodName", "initialize")
        applicationMap.put("registerMethodName", "registerApplicationDelegate")
        registerInfo.add(applicationMap)
        // 路由
        Map<String, Object> routerMap = new HashMap<String, Object>()
        routerMap.put("scanInterface", "com.senierr.cobweb.component.IRouter")
        routerMap.put("codeInsertToClassName", "com.senierr.cobweb.Cobweb")
        routerMap.put("codeInsertToMethodName", "initialize")
        routerMap.put("registerMethodName", "registerRouter")
        registerInfo.add(routerMap)
        // 降级
        Map<String, Object> degradeMap = new HashMap<String, Object>()
        degradeMap.put("scanInterface", "com.senierr.cobweb.component.IDegrade")
        degradeMap.put("codeInsertToClassName", "com.senierr.cobweb.Cobweb")
        degradeMap.put("codeInsertToMethodName", "initialize")
        degradeMap.put("registerMethodName", "registerDegrade")
        registerInfo.add(degradeMap)
        // 路由
        Map<String, Object> interceptorMap = new HashMap<String, Object>()
        interceptorMap.put("scanInterface", "com.senierr.cobweb.component.IInterceptor")
        interceptorMap.put("codeInsertToClassName", "com.senierr.cobweb.Cobweb")
        interceptorMap.put("codeInsertToMethodName", "initialize")
        interceptorMap.put("registerMethodName", "registerInterceptor")
        registerInfo.add(interceptorMap)

        registerInfo.each { map ->
            RegisterInfo info = new RegisterInfo()
            info.interfaceName = map.get('scanInterface')
            def superClasses = map.get('scanSuperClasses')
            if (!superClasses) {
                superClasses = new ArrayList<String>()
            } else if (superClasses instanceof String) {
                ArrayList<String> superList = new ArrayList<>()
                superList.add(superClasses)
                superClasses = superList
            }
            info.superClassNames = superClasses
            info.initClassName = map.get('codeInsertToClassName') //代码注入的类
            info.initMethodName = map.get('codeInsertToMethodName') //代码注入的方法（默认为static块）
            info.registerMethodName = map.get('registerMethodName') //生成的代码所调用的方法
            info.registerClassName = map.get('registerClassName') //注册方法所在的类
            info.include = map.get('include')
            info.exclude = map.get('exclude')
            info.init()
            if (info.validate())
                list.add(info)
            else {
                project.logger.error('auto register config error: scanInterface, codeInsertToClassName and registerMethodName should not be null\n' + info.toString())
            }

        }
        
        if (cacheEnabled) {
            checkRegisterInfo()
        } else {
            deleteFile(AutoRegisterHelper.getRegisterInfoCacheFile(project))
            deleteFile(AutoRegisterHelper.getRegisterCacheFile(project))
        }
    }

    private void checkRegisterInfo() {
        def registerInfo = AutoRegisterHelper.getRegisterInfoCacheFile(project)
        def listInfo = list.toString()
        def sameInfo = false

        if (!registerInfo.exists()) {
            registerInfo.createNewFile()
        } else if(registerInfo.canRead()) {
            def info = registerInfo.text
            sameInfo = info == listInfo
            if (!sameInfo) {
                project.logger.error("auto-register registerInfo has been changed since project(':$project.name') last build")
            }
        } else {
            project.logger.error('auto-register read registerInfo error--------')
        }
        if (!sameInfo) {
            deleteFile(AutoRegisterHelper.getRegisterCacheFile(project))
        }
        if (registerInfo.canWrite()) {
            registerInfo.write(listInfo)
        } else {
            project.logger.error('auto-register write registerInfo error--------')
        }
    }

    private void deleteFile(File file) {
        if (file.exists()) {
            //registerInfo 配置有改动就删除緩存文件
            file.delete()
        }
    }

    void reset() {
        list.each { info ->
            info.reset()
        }
    }

    @Override
    String toString() {
        StringBuilder sb = new StringBuilder(RegisterPlugin.EXT_NAME).append(' = {')
                .append('\n  cacheEnabled = ').append(cacheEnabled)
                .append('\n  registerInfo = [\n')
        def size = list.size()
        for (int i = 0; i < size; i++) {
            sb.append('\t' + list.get(i).toString().replaceAll('\n', '\n\t'))
            if (i < size - 1)
                sb.append(',\n')
        }
        sb.append('\n  ]\n}')
        return sb.toString()
    }
}
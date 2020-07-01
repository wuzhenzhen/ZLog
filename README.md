# ZLog、ZFtpServer


---
## Gradle

```groovy
dependencies {
    implementation 'com.kgd.tools:ZLog:1.0.0'
    implementation 'com.kgd.tools:ZFtpServer:1.0.0'
}
```

使用方法可参考示例代码：
=======================ZLog=====================================

        //初始化Logcat 配置更多信息
        Builder builder = ZLog.newBuilder();
        builder.logSavePath("/sdcard/EBSB_SDCardLog"); //设置Log 保存的文件夹
        builder.logCatLogLevel(ZLog.SHOW_ALL_LOG); //控制台输出日志等级
        builder.fileLogLevel(ZLog.SHOW_ALL_LOG);   //保存文件日志等级
        builder.dbFlowStatistics(true);  //开启流量打印
        builder.fileOutFormat(ZLog.LOG_OUTPUT_FORMAT_3);
        ZLog.initialize(this, builder.build());
        
        //只控制台打印
        ZLog.v("The is verbose log");
        
        //只写入Log 文件
        ZLog.vv("file: The is verbose log");
        
        //控制台+写入Log 文件
        ZLog.vvv("All: The is verbose log");
        
        ZLog.iii("---flow="+ZFlow.getSumFlowYesterday());  //打印昨天的流量数据 
        ZLog.iii("---flow="+ZFlow.getSumFlow(System.currentTimeMillis())); //打印某一天的流量数据
        
=======================ZFtpServer=====================================

        if(FsSettings.getUser("ftp2") != null){
            FsSettings.addUser(new FtpUser("ftp2","ftp2","\\")); //添加FTP用户名
        }else{
            ZLog.eee("====ftp=====openFtp=======already=exist=user===");
        }
        FsService.start(this.getApplicationContext(), new FtpListener() {
            @Override
            public void openFtp(int type, String message) {
                ZLog.eee("==ftp==openFtp="+type+"--"+message);
            }

            @Override
            public void closeFtp() {
                ZLog.eee("==ftp==closeFtp=");
            }

            @Override
            public void login(int type, String message) {
                ZLog.eee("==ftp==login="+type+"--"+message);
            }

            @Override
            public void addFile(String file) {
                ZLog.eee("==ftp==addFile="+file);
            }

            @Override
            public void delFile(String file) {
                ZLog.eee("==ftp==delFile="+file);
            }

            @Override
            public void log(String log) {
                ZLog.ddd("##log##"+log);
            }
        });


## License

```
Copyright  2019 kgd

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```


Ref:  https://github.com/iflove/Logcat
    

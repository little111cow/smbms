# smbms
超市订单管理系统
基于servlet、jsp、mysql数据库、ajax、js、Html以及tomcat服务器搭建的超市订单管理系统

# 本地部署运行
* 启动mysql服务，通过smbms.sql数据库文件建立数据库
* 在IDEA通过maven自动导入项目，修改数据库配置文件db.properties中的用户名和密码为自己的用户名和密码
* 在IDEA中配置tomcat，启动运行
* 浏览器访问 [http://localhost:8080/smbms/](http://localhost:8080/smbms/)
* 登录用户名：admin 密码：1234567
# 常见问题：
如果在编辑器里测试通过而部署到服务器发生classnotfound错误，将maven导入的相应jar包拷贝到tomcat服务器下的lib目录。
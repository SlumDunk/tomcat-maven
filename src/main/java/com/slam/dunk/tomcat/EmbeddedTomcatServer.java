package com.slam.dunk.tomcat;

import org.apache.catalina.Context;
import org.apache.catalina.Host;
import org.apache.catalina.Wrapper;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;

/**
 * 代码中创建tomcat
 */
public class EmbeddedTomcatServer {

    public static void main(String[] args) throws Exception {
        //把目录的绝对的路径获取到
        String classpath = System.getProperty("user.dir");
        System.out.println(classpath);
        //我们new一个Tomcat
        Tomcat tomcat = new Tomcat();


        //设置Tomcat的端口
        //tomcat.setPort(9090);
        //会创建 StandardServer, StandardService, Connector[用于处理Htttp请求]
        Connector connector = tomcat.getConnector();
        connector.setPort(9091);
        //设置StandardEngine和StandardHost
        Host host = tomcat.getHost();
        //我们会根据xml配置文件来
        host.setName("localhost");
        host.setAppBase("webapps");
        //前面的那个步骤只是把Tomcat起起来了，但是没啥东西
        //要把class加载进来,把启动的工程加入进来了, 相当于把webapps下的某个应用加进来
        Context context = tomcat.addContext(host, "/home", classpath);
        //加载具体应用下的Servlet, Listener, Filter 等组件
        if (context instanceof StandardContext) {
            StandardContext standardContext = (StandardContext) context;
            //加载Tomcat默认的Servlet--JspServlet, DefaultServlet, MIME-type
            standardContext.setDefaultContextXml("/Users/liuzhongda/IdeaProjects/apache-tomcat-8.5.23/conf/web.xml");
            //我们要把Servlet加载到指定的上下文
            Wrapper wrapper = tomcat.addServlet("/home", "DemoServlet", new DemoServlet());
            //http://localhost:9091/home/hello
            wrapper.addMapping("/hello");
        }
        //Tomcat跑起来
        tomcat.start();
        //强制Tomcat server等待，避免main线程执行结束后关闭
        tomcat.getServer().await();

    }

}

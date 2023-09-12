package com.sdu.springframework.core.servlet;

import com.alibaba.fastjson2.JSONObject;
import com.sdu.springframework.core.bean.Data;
import com.sdu.springframework.core.bean.Handler;
import com.sdu.springframework.core.bean.Param;
import com.sdu.springframework.core.bean.View;
import com.sdu.springframework.core.manager.*;
import com.sdu.springframework.core.util.ReflectUtil;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * 请求转发器
 * 该Servlet将会在Web容器启动时加载
 */
@WebServlet(urlPatterns = "/test", loadOnStartup = 0)
public class DispatcherServlet extends HttpServlet {

    @Override
    public void init(ServletConfig servletConfig) {
        // 初始化相关的manager类
        ManagerLoader.init();
        // 获取ServletContext对象，用于注册Servlet
        ServletContext servletContext = servletConfig.getServletContext();
        // 注册处理jsp和静态资源的servlet
        registerServlet(servletContext);
    }

    /**
     * DefaultServlet和JspServlet都是由Web容器创建
     * org.apache.catalina.servlets.DefaultServlet
     * org.apache.jasper.servlet.JspServlet
     */
    private void registerServlet(ServletContext servletContext) {
        // 动态注册处理JSP的Servlet
        ServletRegistration jspServlet = servletContext.getServletRegistration("jsp");
        jspServlet.addMapping(ConfigManager.getAppJspPath() + "*");

        // 动态注册处理静态资源的默认Servlet
        ServletRegistration defaultServlet = servletContext.getServletRegistration("default");
        defaultServlet.addMapping("/favicon.ico");
        defaultServlet.addMapping(ConfigManager.getAppAssetPath() + "*");
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String requestMethod = req.getMethod().toUpperCase();
        String requestPath = req.getPathInfo();
        // 根据请求获取处理器
        Handler handler = ControllerManager.getHandler(requestMethod, requestPath);
        if (null != handler) {
            // 拿到映射Controller中的类和对应的实例bean，这里能拿到是因为初始化时扫描了@Controller注解
            Class<?> controllerClass = handler.getControllerClass();
            Object controllerBean = BeanManager.getBean(controllerClass);
            // 创建请求参数
            Param param = RequestManager.createParam(req);
            // 调用与请求对应的方法
            Object result;
            Method actionMethod = handler.getControllerMethod();
            if (null == param || param.isEmpty()) {
                result = ReflectUtil.invokeMethod(controllerBean, actionMethod);
            } else {
                result = ReflectUtil.invokeMethod(controllerBean, actionMethod, param);
            }
            if (result instanceof View) {
                handleViewResult((View) result, req, resp);
            } else if (result instanceof Data) {
                handleDataResult((Data) result, resp);
            }
        }
    }

    /**
     * 跳转页面
     */
    private void handleViewResult(View view, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String path = view.getPath();
        if (path != null && !path.isEmpty()) {
            if (path.startsWith("/")) { //重定向
                response.sendRedirect(request.getContextPath() + path);
            } else { //请求转发
                Map<String, Object> model = view.getModel();
                for (Map.Entry<String, Object> entry : model.entrySet()) {
                    request.setAttribute(entry.getKey(), entry.getValue());
                }
                request.getRequestDispatcher(ConfigManager.getAppJspPath() + path).forward(request, response);
            }
        }
    }

    /**
     * 返回JSON数据
     */
    private void handleDataResult(Data data, HttpServletResponse response) throws IOException {
        Object model = data.getModel();
        if (null != model) {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            PrintWriter writer = response.getWriter();
            String resp = JSONObject.toJSONString(model);
            writer.write(resp);
            writer.flush();
            writer.close();
        }
    }
}

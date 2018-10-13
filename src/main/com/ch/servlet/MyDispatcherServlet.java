package com.ch.servlet;

import com.ch.annotation.Controller;
import com.ch.annotation.RequestMapping;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.*;
import java.net.URL;
import java.util.*;

/**
 * @Auther: pch MVC前端控制器 核心servlet
 * @Date: 2018/10/12 21:32
 * @Description:
 */
public class MyDispatcherServlet extends HttpServlet {

	// 为了得到参数的名称 引入了Spring的类
	private static LocalVariableTableParameterNameDiscoverer parameterNameDiscoverer = new LocalVariableTableParameterNameDiscoverer();

	// 配置文件
	private Properties properties = new Properties();

	// 扫描指定包下的所有类
	private List<String> classNames = new ArrayList<>();

	//模拟ioc容器
	private Map<String, Object> ioc = new HashMap<>();

	// 请求的URL对应的方法
	private Map<String, Method> handlerMapping = new  HashMap<>();

	// 请求的url对应的控制层实例
	private Map<String, Object> controllerMap  =new HashMap<>();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		this.doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 处理请求
		resp.setContentType("text/html; charset=utf-8");
		doDispatch(req, resp);
	}

	private void doDispatch(HttpServletRequest req, HttpServletResponse resp){
		if (handlerMapping.isEmpty()) {
			return;
		}
		// 获取请求url
		String url = req.getRequestURI();
		String contextPath = req.getContextPath();
		url = url.replaceAll(contextPath, "").replaceAll("/+", "/");
		// 请求url 不在 映射器内
		if (!this.handlerMapping.containsKey(url)) {
			try {
				resp.getWriter().print("网页未找到...");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		Method method = handlerMapping.get(url);
		String[] methodParameterNames = parameterNameDiscoverer.getParameterNames(method);
		// 获取方法的参数列表
		Class<?>[] methodParameterTypes = method.getParameterTypes();
		Parameter[] parameters = method.getParameters();
		// 获取请求的参数
		Map<String, String[]> reqParameterMap = req.getParameterMap();
		// 保存参数值
		Object [] methodParamValues= new Object[methodParameterTypes.length];
		//方法的参数列表
		Class parameterType;
		for (int i = 0; i<parameters.length; i++){
			//得到 方法参数类型
			parameterType = methodParameterTypes[i];
			// requese和response
			if (parameterType == HttpServletRequest.class){
				methodParamValues[i]=req;
				continue;
			}
			if (parameterType == HttpServletResponse.class){
				methodParamValues[i]=resp;
				continue;
			}
			if (reqParameterMap.get(methodParameterNames[i])!=null && reqParameterMap.get(methodParameterNames[i])[0]!=null) {
				if (parameterType == int.class) {
					methodParamValues[i] = (int)Integer.parseInt(reqParameterMap.get(methodParameterNames[i])[0]);
				}else if(parameterType == Integer.class){
					methodParamValues[i] = Integer.parseInt(reqParameterMap.get(methodParameterNames[i])[0]);
				} else if (parameterType == Float.class) {
					methodParamValues[i] = Float.valueOf(reqParameterMap.get(methodParameterNames[i])[0]);
				}else if (parameterType == Double.class) {
					methodParamValues[i] = Double.valueOf(reqParameterMap.get(methodParameterNames[i])[0]);
				}else{
					methodParamValues[i] = reqParameterMap.get(methodParameterNames[i])[0];
				}
			}else{
				try {
					methodParamValues[i]=parameterType.newInstance();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
					methodParamValues[i] = 0;
				} catch (InstantiationException e) {
					e.printStackTrace();
					methodParamValues[i] = 0;
				}
			}
		}
		Object o = controllerMap.get(url);
		try {
			method.invoke(o,methodParamValues);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void destroy() {
		System.err.println("===============servlet关闭了===============");
		super.destroy();
	}

	@Override
	public void init(ServletConfig config) throws ServletException {
		System.err.println("===============servlet启动了===============");
		// 1.加载配置文件
		doLoadConfig(config.getInitParameter("contextConfigLocation"));

		//2.扫描用户设定的包下面所有的类 将类的 全限定名 存储到classNames
		doScanner(properties.getProperty("scanPackage"));

		//3.拿到扫描到的类,通过反射机制,实例化,并且放到ioc容器中(k-v  beanName-bean) beanName默认是首字母小写
		doInstance();

		//4.初始化HandlerMapping(将url和method对应上)
		initHandlerMapping();
	}


	// 加载配置文件
	private void doLoadConfig(String location){
		//把web.xml中的contextConfigLocation对应value值的文件加载到流里面
		InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(location);
		//用Properties文件加载文件里的内容
		try {
			properties.load(inputStream);
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			try {
				inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	// 扫描用户设定的包下面所有的类 将完整类名 存储到classNames
	private void doScanner(String packageName){
		//把所有的.替换成/
		Class c = this.getClass();
		ClassLoader classLoader = c.getClassLoader();
		URL resource = classLoader.getResource("/");
		URL resource1 = classLoader.getResource("/" + packageName.replaceAll("\\.", "/"));
		URL url = this.getClass().getClassLoader().getResource("/" + packageName.replaceAll("\\.", "/"));
		File dir = new File(url.getFile());
		for (File file : dir.listFiles()) {
			if(file.isDirectory()){
				//递归读取包
				doScanner(packageName+"."+file.getName());
			}else{
				String className =packageName +"." +file.getName().replace(".class", "");
				classNames.add(className);
			}
		}
	}

	// 实例化 扫描到的类 放到map容器(IOC)中
	private void doInstance(){
		if (classNames.isEmpty()) {
			return;
		}
		Class c;
		for (String className : classNames) {
			try {
				c = Class.forName(className);
				// 只有加了 Controller注解的 才实例化
				if (c.isAnnotationPresent(Controller.class)) {
					ioc.put(toLowerFirstWord(c.getSimpleName()), c.newInstance());
				}else{
					continue;
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			}

		}

	}

	// 初始化请求 将请求的URL和 方法对应上
	private void initHandlerMapping(){
		if (ioc.isEmpty()) {
			return;
		}
		RequestMapping requestMapping;
		for (Map.Entry<String, Object> entry : ioc.entrySet()) {
			Class<? extends Object> clazz = entry.getValue().getClass();
			// 再一次 过滤 没有controller注解的类
			if(!clazz.isAnnotationPresent(Controller.class)){
				continue;
			}
			//拼url时,是controller头的url拼上方法上的url
			String baseUrl ="";
			if (clazz.isAnnotationPresent(RequestMapping.class)) {
				requestMapping = clazz.getAnnotation(RequestMapping.class);
				// 获取 类的 基础路径
				baseUrl = requestMapping.value();
			}
			// 获取类的所有方法
			Method[] methods = clazz.getMethods();
			for (Method method : methods) {
				// 判断 方法头 是否有RequestMapping注解
				if (!method.isAnnotationPresent(RequestMapping.class)) {
					continue;
				}
				requestMapping = method.getAnnotation(RequestMapping.class);
				String url = requestMapping.value();
				//url = (baseUrl + url).replaceAll("/+", "/");
				url = baseUrl + url;
				// 将请求url 和 对应的方法 放到map中
				handlerMapping.put(url,method);
				try {
					// 请求url 和 对象实例
					controllerMap.put(url,clazz.newInstance());
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
				System.out.println(url+","+method);
			}


		}
	}

	// 将类名的首字母 转换成小写
	private String toLowerFirstWord(String name){
		char[] charArray = name.toCharArray();
		charArray[0] += 32;
		return String.valueOf(charArray);
	}

}

package com.atguigu.springcloud.controller;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.atguigu.springcloud.entities.Dept;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
public class DeptController_Consumer
{

	//private static final String REST_URL_PREFIX = "http://localhost:8001";
	//getForEntity的第一个参数为我要调用的服务的地址，这里我调用了服务提供者提供的/hello接口，注意这里是通过服务名调用而不是服务地址，如果写成服务地址就没法实现客户端负载均衡了
	private static final String REST_URL_PREFIX = "http://MICROSERVICECLOUD-DEPT";

	/**
	 * 使用 使用restTemplate访问restful接口非常的简单粗暴无脑。 (url, requestMap,
	 * ResponseBean.class)这三个参数分别代表 REST请求地址、请求参数、HTTP响应转换被转换成的对象类型。
	 */
	@Autowired
	private RestTemplate restTemplate;

	@RequestMapping(value = "/consumer/dept/add")
	public boolean add(Dept dept)
	{
		return restTemplate.postForObject(REST_URL_PREFIX + "/dept/add", dept, Boolean.class);
	}

	@RequestMapping(value = "/consumer/dept/get/{id}")
	public Dept get(@PathVariable("id") Long id)
	{

		ResponseEntity<Dept> forEntity = restTemplate.getForEntity(REST_URL_PREFIX + "/test" + id, Dept.class);
		Dept dept = forEntity.getBody();
		ResponseEntity<String> forEntity1 = restTemplate.getForEntity(REST_URL_PREFIX + "?name={1}", String.class, Dept.class);
		String body1 = forEntity1.getBody();
		Map map = new HashMap();
		map.put("name","A");
		ResponseEntity forEntity2 = restTemplate.getForEntity(REST_URL_PREFIX + "?name={name}", String.class, map);
		restTemplate.delete(REST_URL_PREFIX + "?name={name}", 100);
		UriComponents uriComponents = UriComponentsBuilder.fromUriString(REST_URL_PREFIX+"?name={name}").build().expand("A").encode();
		URI uri = uriComponents.toUri();

		ResponseEntity<String> responseEntity = restTemplate.getForEntity(uri, String.class);
		String body = responseEntity.getBody();

		return restTemplate.getForObject(REST_URL_PREFIX + "/dept/get/" + id, Dept.class);
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/consumer/dept/list")
	public List<Dept> list()
	{
		return restTemplate.getForObject(REST_URL_PREFIX + "/dept/list", List.class);
	}

	// 测试@EnableDiscoveryClient,消费端可以调用服务发现
	@RequestMapping(value = "/consumer/dept/discovery")
	public Object discovery()
	{
		return restTemplate.getForObject(REST_URL_PREFIX + "/dept/discovery", Object.class);
	}

}

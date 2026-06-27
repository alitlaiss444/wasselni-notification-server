package com.wasselni.common.supports;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.wasselni.common.model.audit.IncludeAudit;
import com.wasselni.common.utils.audit.AuditUtils;
import com.wasselni.common.utils.audit.jacksonfilter.AntPathPropertyFilter;

import net.sf.json.JSONObject;

public class AttributesResolver {

	public Object matchArgument(Object[] args, Object oldObject) {

		if (!AuditUtils.isArrayUsable(args)) {
			return null;
		}

		return args[0];
	}

	private String getRequestMethod(Method method) {

		// controller method annotations of type @RequestMapping
		RequestMapping[] reqMappingAnnotations = method
				.getAnnotationsByType(org.springframework.web.bind.annotation.RequestMapping.class);
		for (RequestMapping annotation : reqMappingAnnotations) {
			for (RequestMethod reqMethod : annotation.method()) {
				return reqMethod.name();
			}
		}

		// for specific handler methods ( @GetMapping , @PostMapping)
		Annotation[] annos = method.getDeclaredAnnotations();
		for (Annotation anno : annos) {
			if (anno.annotationType()
					.isAnnotationPresent(org.springframework.web.bind.annotation.RequestMapping.class)) {
				reqMappingAnnotations = anno.annotationType()
						.getAnnotationsByType(org.springframework.web.bind.annotation.RequestMapping.class);
				for (RequestMapping annotation : reqMappingAnnotations) {
					for (RequestMethod reqMethod : annotation.method()) {
						return reqMethod.name();
					}
				}
			}
		}

		return null;

	}

	public String objectToJson(ProceedingJoinPoint joinPoint, Object object) {

		Object[] args = joinPoint.getArgs();
		MethodSignature methodSignature = (MethodSignature) joinPoint.getStaticPart().getSignature();
		Method method = methodSignature.getMethod();
		String requestMethod = getRequestMethod(method);

		if (StringUtils.isNotBlank(requestMethod) && requestMethod.equals(RequestMethod.GET.toString())) {
			Annotation[][] parameterAnnotations = method.getParameterAnnotations();

			JSONObject jsonObject = new JSONObject();

			for (int argIndex = 0; argIndex < args.length; argIndex++) {
				for (Annotation annotation : parameterAnnotations[argIndex]) {
					if (!(annotation instanceof RequestParam))
						continue;
					RequestParam requestParam = (RequestParam) annotation;
					jsonObject.put(requestParam.value(), args[argIndex]).toString();
				}
			}

			if (jsonObject.size() > 0) {
				return jsonObject.toString();
			}

		} else if (StringUtils.isNotBlank(requestMethod) && requestMethod.equals(RequestMethod.POST.toString())) {

			if (object != null && !object.getClass().getName().startsWith("java.lang")) {

				Field[] fields = object.getClass().getDeclaredFields();
				List<String> defaultJsonFilterList = new ArrayList<String>();

				if (object.getClass().getSuperclass() != null) {

					// Merge inherited fields
					List<Field> list = new ArrayList<Field>(Arrays.asList(fields));
					list.addAll(Arrays.asList(object.getClass().getSuperclass().getDeclaredFields()));
					fields = list.toArray(new Field[0]);

					for (Annotation annotation : object.getClass().getSuperclass().getAnnotations()) {
						if (annotation instanceof JsonFilter) {
							JsonFilter jsonFilter = (JsonFilter) annotation;
							defaultJsonFilterList.add(jsonFilter.value());
						}
					}

				}

				List<String> filter = new ArrayList<String>();

				getAllowedFields(filter, fields, "");

				if (filter.isEmpty()) {
					return "";
				}

				ObjectMapper mapper = new ObjectMapper();
				SimpleFilterProvider simpleFilterProvider = new SimpleFilterProvider();

				if (defaultJsonFilterList.isEmpty()) {

					simpleFilterProvider.addFilter("antPathFilter",
							new AntPathPropertyFilter(filter.toArray(new String[0])));

				} else {
					for (String jsonFilterItem : defaultJsonFilterList) {
						SimpleBeanPropertyFilter simpleBeanPropertyFilter = SimpleBeanPropertyFilter
								.filterOutAllExcept(filter.toArray(new String[0]));
						simpleFilterProvider.addFilter(jsonFilterItem, simpleBeanPropertyFilter);
					}
				}

				FilterProvider filterProvider = simpleFilterProvider;
				mapper.setFilterProvider(filterProvider);

				mapper.setSerializationInclusion(Include.NON_NULL);

				try {
					String json = mapper.writeValueAsString(object);

					json = repalceAuditName(json, fields);

					return json;

				} catch (JsonProcessingException e) {
				}
			}

		}

		return null;

	}

	private void getAllowedFields(List<String> filter, Field[] fields, String parent) {

		for (Field f : fields) {

			Annotation includeTag = Arrays.asList(f.getDeclaredAnnotations()).stream()
					.filter(x -> x instanceof IncludeAudit).findFirst().orElse(null);

			if (includeTag != null) {

				IncludeAudit auditAnnotiation = (IncludeAudit) includeTag;

				if (auditAnnotiation.enabled()) {

					filter.add(parent + f.getName());

					Field[] subFields = f.getType().getDeclaredFields();

					if (subFields.length > 0) {

						Boolean allowedAudit = Arrays.asList(subFields).stream()
								.anyMatch(x -> Arrays.asList(x.getDeclaredAnnotations()).stream()
										.anyMatch(y -> y instanceof IncludeAudit && ((IncludeAudit) y).enabled()));

						if (allowedAudit) {
							parent += f.getName() + ".";
							getAllowedFields(filter, subFields, parent);
							parent = parent.replace(f.getName() + ".", "");
						}

					}

				}
			}

		}

	}

	private String repalceAuditName(String json, Field[] fields) {

		for (Field f : fields) {

			Annotation includeTag = Arrays.asList(f.getDeclaredAnnotations()).stream()
					.filter(x -> x instanceof IncludeAudit).findFirst().orElse(null);

			if (includeTag != null) {

				IncludeAudit auditAnnotiation = (IncludeAudit) includeTag;

				if (auditAnnotiation.enabled()) {

					if (!StringUtils.isEmpty(auditAnnotiation.name())) {
						json = json.replace(f.getName(), auditAnnotiation.name());
					}

					Field[] subFields = f.getType().getDeclaredFields();

					if (subFields.length > 0) {

						Boolean allowedAudit = Arrays.asList(subFields).stream()
								.anyMatch(x -> Arrays.asList(x.getDeclaredAnnotations()).stream()
										.anyMatch(y -> y instanceof IncludeAudit && ((IncludeAudit) y).enabled()));

						if (allowedAudit) {
							json = repalceAuditName(json, subFields);
						}

					}

				}
			}

		}

		return json;

	}

}

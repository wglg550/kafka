package distributed.transaction;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import distributed.transaction.redis.RedisClient;
import distributed.transaction.redis.RedisClientImpl;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 入口类
 *
 * @author Dean
 */
@SpringBootApplication
@EnableScheduling
@EnableTransactionManagement
@MapperScan(basePackages = "distributed.transaction.mappers")
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    /**
     * 文件上传配置
     *
     * @return
     */
//    @Bean
//    public MultipartConfigElement multipartConfigElement() {
//        MultipartConfigFactory factory = new MultipartConfigFactory();
//        //单个文件最大
//        factory.setMaxFileSize("102400KB"); //KB,MB
//        /// 设置总上传数据总大小
//        factory.setMaxRequestSize("102400KB");
//        return factory.createMultipartConfig();
//    }

//    @Bean(name = "multipartResolver")
//    public MultipartResolver multipartResolver() {
//        CommonsMultipartResolver resolver = new CommonsMultipartResolver();
//        resolver.setDefaultEncoding("UTF-8");
//        resolver.setResolveLazily(true);
//        resolver.setMaxInMemorySize(40960);
//        resolver.setMaxUploadSize(100 * 1024 * 1024);
//        return resolver;
//    }
//    @Bean
//    public EmbeddedServletContainerCustomizer containerCustomizer() {
//        return new EmbeddedServletContainerCustomizer() {
//            @Override
//            public void customize(ConfigurableEmbeddedServletContainer container) {
//                container.setSessionTimeout(10);//单位为S
//            }
//        };
//    }

//    @Bean
//    public EmbeddedServletContainerCustomizer embeddedServletContainerCustomizer() {
//        return new EmbeddedServletContainerCustomizer() {
//            @Override
//            public void customize(ConfigurableEmbeddedServletContainer container) {
//                //设置session超时时间为1分钟
//                container.setSessionTimeout(1, TimeUnit.MINUTES);
//            }
//        };
//    }
//    @Bean
//    @LoadBalanced
//    public RestTemplate restTemplate() {
//        RestTemplate restTemplate = new RestTemplate();
//        restTemplate.setErrorHandler(new CustomResponseErrorHandler());
//        return restTemplate;
//    }
    @Bean
    public RedisTemplate<String, Object> redisTemplate(
            RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        Jackson2JsonRedisSerializer<?> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(
                Object.class);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
//        redisTemplate.setHashKeySerializer(jackson2JsonRedisSerializer);
//        redisTemplate.setHashValueSerializer(new StringRedisSerializer());
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    @Bean
    @Autowired
    public RedisClient redisClient(RedisTemplate<String, Object> redisTemplate) {
        return new RedisClientImpl(redisTemplate);
    }
}

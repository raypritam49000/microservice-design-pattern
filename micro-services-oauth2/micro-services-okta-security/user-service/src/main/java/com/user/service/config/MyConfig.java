package com.user.service.config;

import com.user.service.config.interceptor.RestClientInterceptor;
import com.user.service.config.interceptor.RestTemplateInterceptor;
import com.user.service.config.interceptor.WebClientInterceptor;
import com.user.service.external.services.HotelRestClient;
import com.user.service.external.services.RatingRestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.client.loadbalancer.reactive.LoadBalancedExchangeFilterFunction;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProvider;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProviderBuilder;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class MyConfig {

    @Autowired
    private LoadBalancedExchangeFilterFunction filterFunction;

    @Autowired
    private ClientRegistrationRepository clientRegistrationRepository;

    @Autowired
    private OAuth2AuthorizedClientRepository oAuth2AuthorizedClientRepository;

    @Autowired
    @Lazy
    private WebClientInterceptor webClientInterceptor;

    @Autowired
    @Lazy
    private RestClientInterceptor restClientInterceptor;


    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
        interceptors.add(new RestTemplateInterceptor(manager(clientRegistrationRepository, oAuth2AuthorizedClientRepository)));
        restTemplate.setInterceptors(interceptors);
        return restTemplate;
    }


    @Bean
    public OAuth2AuthorizedClientManager manager(ClientRegistrationRepository clientRegistrationRepository, OAuth2AuthorizedClientRepository auth2AuthorizedClientRepository) {
        OAuth2AuthorizedClientProvider provider = OAuth2AuthorizedClientProviderBuilder.builder().clientCredentials().build();
        DefaultOAuth2AuthorizedClientManager defaultOAuth2AuthorizedClientManager = new DefaultOAuth2AuthorizedClientManager(clientRegistrationRepository, auth2AuthorizedClientRepository);
        defaultOAuth2AuthorizedClientManager.setAuthorizedClientProvider(provider);
        return defaultOAuth2AuthorizedClientManager;
    }


    @Bean
    @LoadBalanced
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder().filter(webClientInterceptor);
    }

    @Bean
    public WebClient webClient() {
        return webClientBuilder().build();
    }

    @Bean
    @LoadBalanced
    public RestClient.Builder restClientBuilder() {
        return RestClient.builder().requestInterceptor(restClientInterceptor);
    }

    @Bean
    public RestClient restClient() {
        return restClientBuilder().build();
    }

    @Bean
    public HttpServiceProxyFactory httpServiceProxyFactory() {
        WebClient webClient = WebClient.builder().filter(filterFunction).filter(webClientInterceptor).build();
        HttpServiceProxyFactory httpServiceProxyFactory = HttpServiceProxyFactory.builderFor(WebClientAdapter.create(webClient)).build();
        return httpServiceProxyFactory;
    }

    @Bean
    public HotelRestClient hotelRestClient() {
        return httpServiceProxyFactory().createClient(HotelRestClient.class);
    }

    @Bean
    public RatingRestClient ratingRestClient() {
        return httpServiceProxyFactory().createClient(RatingRestClient.class);
    }

}

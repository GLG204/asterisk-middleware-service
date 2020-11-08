package com.example.demo.ari;

import ch.loway.oss.ari4java.ARI;
import ch.loway.oss.ari4java.AriVersion;
import ch.loway.oss.ari4java.generated.ActionEvents;
import ch.loway.oss.ari4java.tools.ARIException;
import ch.loway.oss.ari4java.tools.AriCallback;
import ch.loway.oss.ari4java.tools.RestException;
import ch.loway.oss.ari4java.tools.http.NettyHttpClient;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.URISyntaxException;

@Configuration
public class AsteriskConfiguration {

    @Bean
    public ARI getARI() throws URISyntaxException {
        ARI ari = new ARI();
        NettyHttpClient hc = new NettyHttpClient();
        hc.initialize("http://my-pbx-ip:8088/", "admin", "admin");
        ari.setHttpClient(hc);
        ari.setWsClient(hc);
        ari.setVersion(AriVersion.ARI_0_0_1);
        return ari;
    }

    @Bean
    public ActionEvents getAction(ARI ari, RabbitTemplate rabbitTemplate) throws ARIException {
        ActionEvents ae = ari.getActionImpl(ActionEvents.class);
        ae.eventWebsocket("hello,goodbye", new AriCallback() {
            @Override
            public void onSuccess(Object o) {
                rabbitTemplate.convertAndSend("asterisk", "success", o);
            }

            @Override
            public void onFailure(RestException e) {
                rabbitTemplate.convertAndSend("asterisk", "failure", e);
            }
        });
        return ae;
    }
}

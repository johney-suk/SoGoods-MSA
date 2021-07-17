package org.thesix.gatewayserver.config;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

@Log4j2
public class GatewayExceptionHandler implements ErrorWebExceptionHandler {
    private String errorStatusJSON(int errorStatus,String msg) {
        return "{\"errorStatus\":" + errorStatus +",\"error\":"+msg+"}";
    }

    /**
     * 에러코드를 반환하여 Response에 담아준다
     * 1000 NullPointerException
     * 1001 ExpiredJwtException
     * 1002 MalformedJwtException, SignatureException, UnsupportedJwtException
     * 1003 IllegalArgumentException
     * @param exchange
     * @param throwable
     * @return Mono<Void> with error status in response
     */
    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable throwable) {
        log.warn("GATEWAY EXCEPTION : " + throwable);
        int status = 0;
        if (throwable.getClass() == NullPointerException.class) {
            status = 1000;
        } else if (throwable.getClass() == ExpiredJwtException.class) {
            status = 1001;
        } else if (throwable.getClass() == MalformedJwtException.class || throwable.getClass() == SignatureException.class || throwable.getClass() == UnsupportedJwtException.class) {
            status = 1002;
        } else if (throwable.getClass() == IllegalArgumentException.class) {
            status = 1003;
        } else if (throwable.getClass() == RuntimeException.class) {
            status = 1004;
        } else if (throwable.getClass() == StringIndexOutOfBoundsException.class) {
            status = 1005;
        }
        byte[] bytes = errorStatusJSON(status,throwable.getMessage()).getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);
        return exchange.getResponse().writeWith(Flux.just(buffer));
    }
}

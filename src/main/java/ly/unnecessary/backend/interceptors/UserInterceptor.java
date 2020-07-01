package ly.unnecessary.backend.interceptors;

import io.grpc.Context;
import io.grpc.Contexts;
import io.grpc.Metadata;
import io.grpc.ServerCall;
import io.grpc.ServerCall.Listener;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;

import static io.grpc.Metadata.ASCII_STRING_MARSHALLER;

public class UserInterceptor implements ServerInterceptor {
    public static Context.Key<String> USER_EMAIL = Context.key("x-uly-email");
    public static Context.Key<String> USER_PASSWORD = Context.key("x-uly-password");

    public static Metadata.Key<String> USER_EMAIL_KEY = Metadata.Key.of("x-uly-email", ASCII_STRING_MARSHALLER);
    public static Metadata.Key<String> USER_PASSWORD_KEY = Metadata.Key.of("x-uly-password", ASCII_STRING_MARSHALLER);

    @Override
    public <ReqT, RespT> Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> call, Metadata headers,
            ServerCallHandler<ReqT, RespT> next) {
        var context = Context.current().withValues(USER_EMAIL, headers.get(USER_EMAIL_KEY), USER_PASSWORD,
                headers.get(USER_PASSWORD_KEY));

        return Contexts.interceptCall(context, call, headers, next);
    }
}
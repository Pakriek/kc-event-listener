package com.pat.keycloak.domain;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.jboss.logging.Logger;

import com.google.api.gax.core.NoCredentialsProvider;
import com.google.api.core.ApiFuture;
import com.google.api.gax.core.CredentialsProvider;
import com.google.api.gax.grpc.GrpcTransportChannel;
import com.google.api.gax.rpc.FixedTransportChannelProvider;
import com.google.api.gax.rpc.TransportChannelProvider;
import com.google.cloud.pubsub.v1.Publisher;
import com.google.protobuf.ByteString;
import com.google.pubsub.v1.PubsubMessage;
import com.google.pubsub.v1.TopicName;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class PubSub {
    private static Publisher getPublisher(Logger log, PubSubConfig cfg) throws IOException {

        final TopicName topicName = TopicName.of(cfg.getProjectId(),
                cfg.getAdminEventTopicId());

        final String emulatorHost = System.getenv("PUBSUB_EMULATOR_HOST");
        final boolean isEmulator = StringUtils.isNotBlank(emulatorHost);

        if (isEmulator) {
            log.warn("Starting pubsub in emulator mode.");
            final ManagedChannel channel = ManagedChannelBuilder.forTarget(emulatorHost).usePlaintext()
                    .build();
            final TransportChannelProvider channelProvider = FixedTransportChannelProvider
                    .create(GrpcTransportChannel.create(channel));
            final CredentialsProvider credentialsProvider = NoCredentialsProvider.create();
            return Publisher.newBuilder(topicName)
                    .setChannelProvider(channelProvider)
                    .setCredentialsProvider(credentialsProvider)
                    .build();
        } else {
            return Publisher.newBuilder(topicName)
                    .setCredentialsProvider(cfg.getCredentialsProvider())
                    .build();
        }
    }

    public static void publishMessage(Logger log, final String message, final Map<String, String> attributes,
            PubSubConfig cfg) {
        Publisher publisher = null;
        // log.infof("Message type : %s", message.getAction());
        // log.infof("Message data : %s", message.getData());

        final PubsubMessage pubsubMessage = PubsubMessage.newBuilder().setData(ByteString.copyFromUtf8(message))
                .putAllAttributes(attributes).build();
        try {
            publisher = PubSub.getPublisher(log, cfg);

            final ApiFuture<String> messageIdFuture = publisher.publish(pubsubMessage);
            final String messageId = messageIdFuture.get();
            log.infof(
                    "[PUB] published message with id '%s' and content '%s'", messageId, message);
        } catch (final Exception ex) {
            log.warn(
                    "[PUB] error publishing message with errorMessage: %s", ex.getMessage(),
                    ex);
        } finally {
            if (publisher != null) {
                try {
                    publisher.shutdown();
                    publisher.awaitTermination(10, TimeUnit.SECONDS);
                } catch (final Exception ex) {
                    log.error(
                            "[PUB] error shutting down publisher with errorMessage: %s", ex.getMessage(),
                            ex);
                }
            }
        }
    }
}
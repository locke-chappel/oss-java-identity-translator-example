package io.github.lc.oss.identity.trex;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.github.lc.oss.commons.serialization.JsonMessage;
import io.github.lc.oss.commons.serialization.Message;
import io.github.lc.oss.commons.util.TypedEnumCache;

public class Messages extends JsonMessage {
    public enum Authentication implements io.github.lc.oss.commons.serialization.Message {
        /*
         * Framework
         */
        InvalidCredentials(io.github.lc.oss.commons.api.identity.Messages.Authentication.InvalidCredentials),
        InvalidToken(io.github.lc.oss.commons.api.identity.Messages.Authentication.InvalidToken);

        private static final TypedEnumCache<Messages.Authentication, Messages.Authentication> CACHE = new TypedEnumCache<>(
                Messages.Authentication.class);

        public static Set<Messages.Authentication> all() {
            return Messages.Authentication.CACHE.values();
        }

        public static Messages.Authentication byName(String name) {
            return Messages.Authentication.CACHE.byName(name);
        }

        public static boolean hasName(String name) {
            return Messages.Authentication.CACHE.hasName(name);
        }

        public static Messages.Authentication tryParse(String name) {
            return Messages.Authentication.CACHE.tryParse(name);
        }

        private final Category category;
        private final Severity severity;
        private final int number;

        private Authentication(io.github.lc.oss.commons.api.identity.Messages.Authentication src) {
            this.category = io.github.lc.oss.commons.api.identity.Messages.Categories.Authentication;
            this.severity = src.getSeverity();
            this.number = src.getNumber();
        }

        @Override
        public Category getCategory() {
            return this.category;
        }

        @Override
        public Severity getSeverity() {
            return this.severity;
        }

        @Override
        public int getNumber() {
            return this.number;
        }
    }

    @JsonCreator
    public Messages( //
            @JsonProperty(value = "category", required = true) String cateogry, //
            @JsonProperty(value = "severity", required = true) String severity, //
            @JsonProperty(value = "number", required = true) int number, //
            @JsonProperty(value = "text", required = false) String text //
    ) {
        super(Categories.tryParse(cateogry), Severities.tryParse(severity), number, text);
    }

    public Messages(Message message) {
        super(message.getCategory(), message.getSeverity(), message.getNumber(), message.getText());
    }
}

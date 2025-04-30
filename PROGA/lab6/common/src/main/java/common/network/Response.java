package common.network;

import common.network.enums.ResponseType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

public record Response(ResponseType type, String message, Collection<?> collection) implements Serializable {
    public static class ResponseBuilder {
        private ResponseType type;
        private String message;
        private Collection<?> collection;

        public ResponseBuilder() {
            this.type = ResponseType.OK;
            this.message = "";
            this.collection = new HashSet<>();
        }

        public ResponseBuilder setType(ResponseType type) {
            this.type = type;
            return this;
        }

        public ResponseBuilder setMessage(String message) {
            this.message = message;
            return this;
        }


        public ResponseBuilder setCollection(Collection<?> collection) {
            this.collection = collection;
            return this;
        }

        public Response build() {
            return new Response(type, message, collection);
        }
    }

    public static ResponseBuilder builder() {
        return new ResponseBuilder();
    }
}

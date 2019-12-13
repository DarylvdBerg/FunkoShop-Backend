package com.daryl.core;

import javax.ws.rs.core.Response;

public class Body {
    private Response.Status status;
    private String message;
    private Object content;

    public Body(){
        status = Response.Status.OK;
    }

    public Response.Status getStatus() {
        return status;
    }

    public void setStatus(Response.Status status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public Object getContent() {
        return content;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setContent(Object content) {
        this.content = content;
    }

    public javax.ws.rs.core.Response build(){
        return Response.status(status)
                .entity(this)
                .build();
    }

    public Response buildWithHeader(String headerType, Object headers){
        return Response.status(status)
                .header(headerType, headers)
                .entity(this)
                .build();
    }

    public static Response createResponse(Body body, Response.Status status, String message, Object content){
        if(content != null){
            body.setContent(content);
        }
        body.setStatus(status);
        body.setMessage(message);
        return body.build();
    }

    public static Response createResponseWithHeader(Body body, Response.Status status,
                                                    String message, Object content,
                                                    String headerType, Object headers){
        if(content != null){
            body.setContent(content);
        }
        body.setStatus(status);
        body.setContent(content);
        body.setMessage(message);
        return body.buildWithHeader(headerType, headers);
    }
}

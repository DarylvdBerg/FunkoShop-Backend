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
}

package com.daryl.service;

import com.daryl.FunkoShopApplication;
import com.daryl.api.Cart;
import com.daryl.api.Order;
import com.daryl.api.User;
import com.daryl.core.Body;
import com.daryl.db.OrderDAO;
import com.daryl.util.MessageUtil;
import com.daryl.util.PrivilegeUtil;
import org.jdbi.v3.core.statement.UnableToExecuteStatementException;

import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

public class OrderService {
    private OrderDAO orderDAO;

    public OrderService() {
        orderDAO = FunkoShopApplication.jdbiCon.onDemand(OrderDAO.class);
        orderDAO.createTable();
    }

    public Response getAllOrders(User authUser) {
        Body body = new Body();

        if(!PrivilegeUtil.checkPrivilege(authUser, PrivilegeUtil.SEE_ALL_ORDERS)) {
            return Body.createResponse(body, Response.Status.UNAUTHORIZED, MessageUtil.USER_NOT_ENOUGH_PRIVILEGE, null);
        }

        try {
            ArrayList<Order> orders = orderDAO.getOrders();
            return Body.createResponse(body, Response.Status.OK, MessageUtil.ORDERS_FOUND, orders);
        } catch (UnableToExecuteStatementException e) {
            e.printStackTrace();
            return Body.createResponse(body, Response.Status.BAD_REQUEST, MessageUtil.SOMETHING_WENT_WRONG, null);
        }
    }

    public Response getUserOrders(User authUser, int id) {
        Body body = new Body();
        if(!PrivilegeUtil.checkPrivilege(authUser, PrivilegeUtil.SEE_USER_ORDERS)) {
            return Body.createResponse(body, Response.Status.UNAUTHORIZED, MessageUtil.USER_NOT_ENOUGH_PRIVILEGE, null);
        }
        try {
            ArrayList<Order> orders = orderDAO.getUserOrders(id);
            return Body.createResponse(body, Response.Status.OK, MessageUtil.ORDERS_FOUND, orders);
        } catch (UnableToExecuteStatementException e) {
            return Body.createResponse(body, Response.Status.BAD_REQUEST, MessageUtil.SOMETHING_WENT_WRONG, null);
        }
    }

    public Response createOrder(List<Cart> cartList) {
        Body body = new Body();
        boolean created = false;
        try {
            for(Cart cart: cartList) {
                created = this.orderDAO.createOrder(cart.getProductId(), cart.getUserId());
            }
            return created ? Body.createResponse(body, Response.Status.OK, MessageUtil.ORDER_CREATED, null) :
                    Body.createResponse(body, Response.Status.BAD_REQUEST, MessageUtil.ORDER_CREATED_FAILED, null);
        } catch (UnableToExecuteStatementException e) {
            return Body.createResponse(body, Response.Status.BAD_REQUEST, MessageUtil.SOMETHING_WENT_WRONG, null);
        }
    }
}
